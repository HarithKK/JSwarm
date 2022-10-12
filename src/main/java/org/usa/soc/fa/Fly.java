package org.usa.soc.fa;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;

public class Fly {

    private Vector position;

    double[] minBoundary;
    double[] maxBoundary;
    int numberOfDimensions;

    private double intensity;

    public Fly(double[] minBoundary, double[] maxBoundary, int numberOfDimensions) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;

        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1);
    }


    public Vector getPosition() {
        return position.getClonedVector();
    }

    public double updateIntensity(ObjectiveFunction f, double gama, double r) {
        return f.setParameters(this.getPosition().getPositionIndexes()).call() * Math.exp(-(gama * Math.pow(r, 2)));
    }

    public void setPosition(Vector position) {
        this.position.setVector(position);
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public double getIntensity() {
        return intensity;
    }
}
