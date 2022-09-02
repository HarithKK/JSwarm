package org.usa.soc.gwo;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class Wolf {

    private double[] minBoundary;
    private double[] maxBoundary;

    private Vector position;

    private double fitnessValue;
    public Wolf(int numberOfDimensions, double[] minBoundary, double[] maxBoundary) {
        this.maxBoundary =maxBoundary;
        this.minBoundary = minBoundary;
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0 , 1);
    }

    public Vector getPosition() {
        return position.getClonedVector();
    }

    public void setPosition(Vector position) {
        this.position.setVector(position, minBoundary, maxBoundary);
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }
}
