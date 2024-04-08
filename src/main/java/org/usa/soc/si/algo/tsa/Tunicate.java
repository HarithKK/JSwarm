package org.usa.soc.si.algo.tsa;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

public class Tunicate {
    private Vector position;

    private double fitnessValue;

    public Tunicate(int numberOfDimensions,double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1);
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
