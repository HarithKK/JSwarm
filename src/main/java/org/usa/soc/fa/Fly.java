package org.usa.soc.fa;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;

public class Fly {

    private Vector position;
    double[] minBoundary;
    double[] maxBoundary;
    int numberOfDimensions;

    public Fly(double[] minBoundary, double[] maxBoundary, int numberOfDimensions) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;

        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1);
    }


    public Vector getPosition() {
        return position.getClonedVector();
    }

    public double calculateIntensity(ObjectiveFunction<Double> fn, double gama, double r) {
        double I0 = fn.setParameters(this.getPosition().getPositionIndexes()).call();
        double powValue = gama * Math.pow(r, 2);
        return I0 * Math.exp(-powValue);
    }

    public void setPosition(Vector position) {
        this.position.setVector(position);
    }
}
