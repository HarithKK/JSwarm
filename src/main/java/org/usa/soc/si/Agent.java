package org.usa.soc.si;

import examples.si.algo.alo.Ant;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Vector;

public abstract class Agent extends AbsAgent {

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

    public double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

}
