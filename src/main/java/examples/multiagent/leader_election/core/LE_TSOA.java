package examples.multiagent.leader_election.core;

import examples.multiagent.leader_election.testcases.OF;
import examples.si.algo.fa.Fly;
import examples.si.algo.tsoa.Tree;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.Agent;
import org.usa.soc.si.AgentComparator;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Randoms;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class FlyingTree extends Agent{
    private int index;
    private Vector originalPosition;

    private List<FlyingTree> newPopulation = new ArrayList<>();

    private double lambda = 1.0;
    private double w, distance;

    private double fitnessValue2;

    public FlyingTree(Drone drone){
        this.numberOfDimensions = 2;
        this.setPosition(drone.getPosition().getClonedVector());
        this.setOriginalPosition(drone.getPosition().getClonedVector());
        this.setIndex(drone.getIndex());
        setFitnessValue(0);
        setFitnessValue2(0);
    }

    public FlyingTree(FlyingTree source){
        this.numberOfDimensions = 2;
        this.setPosition(source.getPosition().getClonedVector());
        this.setOriginalPosition(source.getPosition().getClonedVector());
        this.setIndex(source.getIndex());
        setFitnessValue(0);
        setFitnessValue2(0);
    }


    public void setFitnessValues(Pair<Double, Double> values) {
        this.fitnessValue = values.getFirst();
        this.fitnessValue2 = values.getSecond();
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    public Vector getOriginalPosition() {
        return originalPosition;
    }

    public void setOriginalPosition(Vector originalPosition) {
        this.originalPosition = originalPosition;
    }

    public List<FlyingTree> getNewPopulation() {
        return newPopulation;
    }

    public void setNewPopulation(List<FlyingTree> newPopulation) {
        this.newPopulation = newPopulation;
    }

    public void addNewPopulation(FlyingTree newPopulationTree) {
        this.newPopulation.add(newPopulationTree);
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getCalculatedDistance(Vector predicted) {
        this.distance = this.getPosition().operate(Vector.OPERATOR.SUB, predicted).getMagnitude();
        return this.distance;
    }

    public void updateLambda(double p, double totalLabmda, double totalDistance) {
        this.setLambda((Math.pow(this.distance, -p) * this.w ) / ((totalDistance)* totalLabmda));
    }

    public void updateWeight(double totalFitnessValue) {
        this.setW(this.fitnessValue / totalFitnessValue);
    }

    public double getFitnessValue2() {
        return fitnessValue2;
    }

    public void setFitnessValue2(double fitnessValue2) {
        this.fitnessValue2 = fitnessValue2;
    }
}

public class LE_TSOA {
    private int stepCount = 0;
    private List<FlyingTree> trees = new ArrayList<>();

    private List<FlyingTree> po;

    private RealMatrix gc;

    private int d = 2;

    private Vector z;

    private double p = -1, fertileHalf =0.3, c1 = 2, c2 =1;
    private double[] minDispressalRadius= Commons.fill(0, 2), maxDispressalRadius = Commons.fill(1, 2);

    private int deligator, seedsCount;

    private FlyingTree gbest;

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
            t.addNewPopulation(t);
            List<FlyingTree> po = findParetoOptimal(t.getNewPopulation());
            if(po != null && po.size() >0){
                FlyingTree o = findOptimal(po);
                t.setPosition(o.getPosition().getClonedVector());
                t.setFitnessValue(o.fitnessValue);
            }
        }

        po = findParetoOptimal(this.trees);
        if(po != null && po.size() >0){
            FlyingTree o = findOptimal(po);
            gbest.setPosition(o.getPosition().getClonedVector());
            gbest.setFitnessValue(o.fitnessValue);
        }

        System.out.println("----- Pareto F"+po.size());

        for(FlyingTree t: trees){
            t.setNewPopulation(new ArrayList<>());
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
                    if (other.getFitnessValue() >= solution.getFitnessValue() && other.getFitnessValue2() >= solution.getFitnessValue2()) {
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
}
