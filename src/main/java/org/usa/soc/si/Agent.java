package org.usa.soc.si;

import examples.si.algo.alo.Ant;
import org.usa.soc.core.ds.Vector;

public class Agent {
    public Vector position;
    public double[] minBoundary, maxBoundary;
    public int numberOfDimensions;
    public double fitnessValue;

    public Agent(double[] minBoundary, double[] maxBoundary, int numberOfDimensions, Vector position) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.position.setVector(position);
    }

    public Agent(){}

    @Override
    public Agent clone() throws CloneNotSupportedException {
        return (Agent) super.clone();
    }

    public Vector getPosition() {
        return position.getClonedVector();
    }

    public void setPosition(Vector position) {
        this.position.setVector(position);
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

}
