package org.usa.soc.si.tsoa;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

public class Tree {
    private Vector position;

    private Vector lBest;

    private double lBestValue;
    private double fitnessValue;
    private double lambda = 0;
    private double distance = 0;

    public Tree(int numberOfDimensions,double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1);
        this.setLambda(1);
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

    public void updateLambda(double p, double totalLabmda, double totalDistance) {
        this.setLambda(Math.pow(this.distance, -p) / ((totalDistance)* totalLabmda));
        //this.setLambda(this.distance/(totalLabmda * totalDistance));
    }

    public Vector getlBest() {
        return lBest;
    }

    public void setlBest(Vector lBest) {
        this.lBest = lBest;
    }

    public double getlBestValue() {
        return lBestValue;
    }

    public void setlBestValue(double lBestValue) {
        this.lBestValue = lBestValue;
    }

    public void setLambda(double lambda) {
        if(!Double.isNaN(lambda)){
            this.lambda = lambda;
        }
    }
}
