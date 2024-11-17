package examples.multiagent.leader_election.core;

import org.apache.commons.math3.util.Pair;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.Agent;

import java.util.ArrayList;
import java.util.List;

public class FlyingTree extends Agent {
    private int index;
    private Vector originalPosition;

    private List<FlyingTree> newPopulation = new ArrayList<>();
    protected List<Double> f1 = new ArrayList<>(), f2= new ArrayList<>(), x=new ArrayList<>(), y=new ArrayList<>();

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

