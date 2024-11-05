package examples.multiagent.leader_election.core;

import examples.multiagent.leader_election.testcases.OF;
import examples.si.algo.tsoa.Tree;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.Agent;
import org.usa.soc.si.AgentComparator;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Randoms;

import java.util.ArrayList;
import java.util.List;

class FlyingTree extends Agent{
    private int index;
    private Vector originalPosition;

    private List<FlyingTree> newPopulation = new ArrayList<>();

    private double lambda = 1.0;
    private double w, distance;

    public FlyingTree(Drone drone){
        this.numberOfDimensions = 2;
        this.setPosition(drone.getPosition().getClonedVector());
        this.setOriginalPosition(drone.getPosition().getClonedVector());
        this.setIndex(drone.getIndex());
        setFitnessValue(0);
    }

    public FlyingTree(FlyingTree source){
        this.numberOfDimensions = 2;
        this.setPosition(source.getPosition().getClonedVector());
        this.setOriginalPosition(source.getPosition().getClonedVector());
        this.setIndex(source.getIndex());
        setFitnessValue(0);
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
}

public class LE_TSOA {
    private int stepCount = 0;
    private List<FlyingTree> trees = new ArrayList<>();

    private RealMatrix gc;

    private int d = 2;

    private Vector z;

    private double p = -1, fertileHalf =0.3, c1 = 2, c2 =1;
    private double[] minDispressalRadius= Commons.fill(-2, 2), maxDispressalRadius = Commons.fill(2, 2);

    private int deligator, seedsCount;

    private FlyingTree gbest;

    public LE_TSOA(RealMatrix gc, List<Drone> drones, int count){
        this.stepCount = count;
        this.gc = gc;
        for(Drone d : drones){
            FlyingTree tree = new FlyingTree(d);
            tree.setFitnessValue(f(tree));
            trees.add(tree);
            if(gbest == null || tree.fitnessValue <gbest.fitnessValue) {
                gbest = new FlyingTree(tree);
            }
        }
        z = new Vector(this.d);
        deligator = (int)(trees.size() * fertileHalf);
        seedsCount = 10;
    }

    private double f(FlyingTree t){
        try{
            RealMatrix xf = t.getPosition().getClonedVector().operate(
                    Vector.OPERATOR.ADD, Commons.levyFlightVector(t.numberOfDimensions, 0.5)).toRealMatrix();

            RealMatrix data = xf.transpose().scalarMultiply(MatrixUtils.inverse(gc).getEntry(t.getIndex(), t.getIndex())).multiply(xf);

            double f1 = data.getTrace();
            double f2 = xf.subtract(t.getOriginalPosition().toRealMatrix()).getNorm();

            return Math.min(f1, f2);

        }catch (Exception e){
            return Double.MAX_VALUE;
        }
    }

    public void run(){
        for(int i=0; i<stepCount; i++){
            step(i);
        }
    }

    private void step(int step) {

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
                    newTree.setFitnessValue(f(newTree));
                    newTree.setLambda(1.0);
                }else if(r < 0.6){
                    newTree.setPosition(t.getPosition().operate(Vector.OPERATOR.ADD,
                            Randoms.getRandomGaussianVector(d, minDispressalRadius, maxDispressalRadius, 0,1)
                    ));
                    newTree.setFitnessValue(f(newTree));
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
                    newTree.setFitnessValue(f(newTree));
                    newTree.setLambda(1.0);
                }
                t.addNewPopulation(newTree);
            }
        }

        for(FlyingTree t: trees){
            for(FlyingTree subTree: t.getNewPopulation()){
                if(subTree.fitnessValue < t.fitnessValue){
                    t.setPosition(subTree.getPosition().getClonedVector());
                    t.setFitnessValue(subTree.fitnessValue);
                }
            }

            if(t.fitnessValue < gbest.fitnessValue){
                gbest = new FlyingTree(t);
            }
        }

        for(FlyingTree t: trees){
            t.setNewPopulation(new ArrayList<>());
        }
    }

    public int getBestIndex(){
        return gbest.getIndex();
    }
}
