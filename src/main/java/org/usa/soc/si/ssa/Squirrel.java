package org.usa.soc.si.ssa;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

public class Squirrel {
    private Vector position;

    private double fitnessValue;
    private double[] minBoundary, maxBoundary;

    public Squirrel(int numberOfDimensions,double[] minBoundary, double[] maxBoundary) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1);
    }

    public Vector getPosition() {
        return position.getClonedVector();
    }

    public void setPosition(Vector position) {
        this.position.setVector(position.fixVector(minBoundary, maxBoundary));
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }
}
