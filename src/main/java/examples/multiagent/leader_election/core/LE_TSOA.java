package examples.multiagent.leader_election.core;

import examples.multiagent.leader_election.testcases.OF;
import examples.si.algo.fa.Fly;
import examples.si.algo.tsoa.Tree;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;
import org.bson.Document;
import org.usa.soc.comparators.ParetoComparator;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.Agent;
import org.usa.soc.si.AgentComparator;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.ParetoUtils;
import org.usa.soc.util.Randoms;

import javax.print.Doc;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LE_TSOA {
    private int stepCount = 0;
    private List<FlyingTree> trees = new ArrayList<>();

    List<Double> gbestHistory = new ArrayList<>();

    private Collection<FlyingTree> po;

    private RealMatrix gc;

    private int d = 2;

    private Vector z;

    private double p = -1, fertileHalf =0.3, c1 = 2, c2 =1;
    private double[] minDispressalRadius= Commons.fill(0, 2), maxDispressalRadius = Commons.fill(1, 2);

    private int deligator, seedsCount;

    private FlyingTree gbest;

    private ParetoComparator<FlyingTree> comparators = new ParetoComparator<>();

    public LE_TSOA(StateSpaceModel model, List<Drone> drones, int count){
        this.stepCount = count;
        this.gc = model.calcGcStep(model.getNN(), 1);
        for(Drone d : drones){
            FlyingTree tree = new FlyingTree(d);
            tree.setFitnessValues(f(tree));
            trees.add(tree);
            if(gbest == null || tree.fitnessValue <gbest.fitnessValue) {
                gbest = new FlyingTree(tree);
            }
        }
        z = new Vector(this.d);
        deligator = (int)(trees.size() * fertileHalf);
        seedsCount = 10;

        comparators.add(new Comparator<FlyingTree>() {
            @Override
            public int compare(FlyingTree o1, FlyingTree o2) {
                return Double.compare(o1.getFitnessValue(), o2.getFitnessValue());
            }
        });

        comparators.add(new Comparator<FlyingTree>() {
            @Override
            public int compare(FlyingTree o1, FlyingTree o2) {
                return Double.compare(o1.getFitnessValue2(), o2.getFitnessValue2());
            }
        });
    }

    private Pair<Double, Double> f(FlyingTree t){
        try{
            RealMatrix xf = t.getPosition().getClonedVector().operate(
                    Vector.OPERATOR.ADD, Commons.levyFlightVector(t.numberOfDimensions, 0.5)).toRealMatrix();

            RealMatrix data = xf.transpose().scalarMultiply(MatrixUtils.inverse(gc).getEntry(t.getIndex(), t.getIndex())).multiply(xf);

            double f1 = data.getTrace();
            double f2 = xf.subtract(t.getOriginalPosition().toRealMatrix()).getNorm();

            return new Pair<>(f1, f2);

        }catch (Exception e){
            System.out.println("OF Error");
            return new Pair<>(Double.MAX_VALUE, Double.MAX_VALUE);
        }
    }

    public void run(){
        for(int i=0; i<stepCount; i++){
            step(i);
        }
    }

    private void step(int step) {

        try {
            Thread.sleep(Randoms.rand(10, 50));
        } catch (InterruptedException e) {

        }

        double totalLabmda = 0;
        double totalFitnessValue = 0.0;
        double totalDistanceWithP = 0.0;

        for(int i =0; i <  this.trees.size(); i++){
            FlyingTree t = this.trees.get(i);
            totalLabmda += t.getLambda();
            totalDistanceWithP += Math.pow(t.getCalculatedDistance(z), -p);
            totalFitnessValue += t.getFitnessValue();
        }

        int zSize = (int) Math.round(this.trees.size() - (step + 1) * ((double)(this.trees.size() - 1) / (double)step));

        for(int i=0; i<zSize ; i++){

            FlyingTree t = this.trees.get(i);
            z = z.operate(Vector.OPERATOR.ADD, t.getPosition().operate(Vector.OPERATOR.MULP, t.getLambda()));
            t.updateWeight(totalFitnessValue);
            t.updateLambda(p, totalLabmda, totalDistanceWithP);
        }

        double minimumDistance = Double.MAX_VALUE;
        for(int i =0; i < this.trees.size(); i++){
            FlyingTree t = this.trees.get(i);
            minimumDistance = Math.min(t.getCalculatedDistance(z), minimumDistance);
        }

        for(int i = 0; i < deligator; i++){
            FlyingTree t = this.trees.get(i);

            for(int j =0; j< seedsCount; j++){
                FlyingTree newTree = new FlyingTree(t);
                double r = Randoms.rand();

                if(r < 0.3){
                    newTree.setPosition(Randoms.getRandomVector(2, minDispressalRadius, maxDispressalRadius));
                    newTree.setFitnessValues(f(newTree));
                    newTree.setLambda(1.0);
                }else if(r < 0.6){
                    newTree.setPosition(t.getPosition().operate(Vector.OPERATOR.ADD,
                            Randoms.getRandomGaussianVector(d, minDispressalRadius, maxDispressalRadius, 0,1)
                    ));
                    newTree.setFitnessValues(f(newTree));
                    newTree.setLambda(1.0);
                }else{
                    Vector v1 = z.getClonedVector().operate(Vector.OPERATOR.SUB, t.getPosition())
                            .operate(Vector.OPERATOR.MULP, c1)
                            .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                    Vector v2 = this.gbest.getPosition().getClonedVector()
                            .operate(Vector.OPERATOR.SUB,  t.getPosition())
                            .operate(Vector.OPERATOR.MULP, c2)
                            .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                    Vector vx = v1.operate(Vector.OPERATOR.ADD, v2);
                    newTree.setPosition(
                            t.getPosition()
                                    .operate(Vector.OPERATOR.ADD, vx)
                    );
                    newTree.setFitnessValues(f(newTree));
                    newTree.setLambda(1.0);
                }
                t.addNewPopulation(newTree);
            }
        }

        for(FlyingTree t: trees){
            //t.addNewPopulation(t);
            po = ParetoUtils.getMinimalFrontierOf(t.getNewPopulation(), comparators);
            if(po != null && po.size() >0){
                FlyingTree o = po.iterator().next();
                t.setPosition(o.getPosition().getClonedVector());
                t.setFitnessValue(o.fitnessValue);
            }
        }

        po = ParetoUtils.getMinimalFrontierOf(trees, comparators);
        if(po != null && po.size() >0){
            FlyingTree o = po.iterator().next();
            gbest.setPosition(o.getPosition().getClonedVector());
            gbest.setFitnessValue(o.fitnessValue);
            gbestHistory.add(gbest.getFitnessValue());
        }

        for(FlyingTree t: trees){
            t.setNewPopulation(new ArrayList<>());
            t.f1.add(t.getFitnessValue());
            t.f2.add(t.getFitnessValue2());
            t.x.add(t.getPosition().getValue(0));
            t.y.add(t.getPosition().getValue(1));
        }
    }

    public List<Point2D> getPeretoFront(){
        return po.stream().map(d -> new Point2D() {
            @Override
            public double getX() {
                return d.getFitnessValue();
            }

            @Override
            public double getY() {
                return d.getFitnessValue2();
            }

            @Override
            public void setLocation(double x, double y) {

            }
        }).collect(Collectors.toList());
    }

    public List<FlyingTree> findParetoOptimal(List<FlyingTree> ts) {
        List<FlyingTree> paretoOptimal = new ArrayList<>();

        for (FlyingTree solution : ts) {
            boolean isDominated = false;

            for (FlyingTree other : ts) {
                if (other != solution) {
                    if (other.getFitnessValue() < solution.getFitnessValue() && other.getFitnessValue2() < solution.getFitnessValue2()) {
                        isDominated = true;
                        break;
                    }
                }
            }

            if (!isDominated) {
                paretoOptimal.add(solution);
            }
        }

        return paretoOptimal;
    }

    public FlyingTree findOptimal(List<FlyingTree> ts){
        double pfv = Double.MAX_VALUE;
        FlyingTree pft = null;

        for(FlyingTree t: ts){
            if(pft == null){
                pft = t;
                pfv = Math.sqrt(Math.pow(t.getFitnessValue(), 2) + Math.pow(t.getFitnessValue2(),2));
            }else{
                double v = Math.sqrt(Math.pow(t.getFitnessValue(), 2) + Math.pow(t.getFitnessValue2(),2));
                if(v < pfv){
                    pft = t;
                    pfv = v;
                }
            }
        }
        return pft;
    }
    public int getBestIndex(){
        return gbest.getIndex();
    }

    public Document getHistory(){
        List<Document> nodes = new ArrayList<>();
        for(FlyingTree t : trees){
            nodes.add(new Document()
                    .append("f1", t.f1)
                    .append("f2", t.f2)
                    .append("x", t.x)
                    .append("y", t.y));
        }
        return new Document().append("nodes", nodes).append("best_value", gbestHistory);
    }

    public void gc(){
        gbestHistory = null;
        trees =null;
        po=null;
        comparators = null;
        System.gc();
    }
}
