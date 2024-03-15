package org.usa.soc.tsoa;

import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;

public class Tree {
    private Vector position;

    private double fitnessValue;
    private double lambda = 0;
    private double distance = 0;

    public Tree(int numberOfDimensions,double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1);
        this.lambda = 1;
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

    public double getLambda() {
        return lambda;
    }

    public double getCalculatedDistance(Vector predicted) {
        this.distance = this.getPosition().operate(Vector.OPERATOR.SUB, predicted).getMagnitude();
        return this.distance;
    }

    public void updateLambda(double totalLabmda, double totalDistance) {
        this.lambda = this.distance/(totalLabmda * totalDistance);
    }
}
