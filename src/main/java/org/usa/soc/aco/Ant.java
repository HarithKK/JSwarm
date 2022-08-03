package org.usa.soc.aco;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class Ant {
    private Vector position;
    private Vector pbest;

    private double[] minBoundary, maxBoundary;

    public Ant(int numberOfDimensions, double[] minBoundary, double[] maxBoundary) {
        this.setPosition(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary));
        this.pbest = this.position.getClonedVector();
        this.maxBoundary = maxBoundary;
        this.minBoundary = minBoundary;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getPbest() {
        return pbest;
    }

    public void updatePBest(ObjectiveFunction<Double> fn, boolean isLocalMinima) {
        double pBestVal =fn.setParameters(this.pbest.getPositionIndexes()).call();
        double stepBestVal = fn.setParameters(this.position.getPositionIndexes()).call();
        if(Validator.validateBestValue(stepBestVal, pBestVal, isLocalMinima)){
            this.pbest.setVector(this.position, this.minBoundary, this.maxBoundary);
        }
    }

    public void setBoundaries(double[] minBoundary, double[] maxBoundary) {
        this.maxBoundary = maxBoundary;
        this.minBoundary = minBoundary;
    }
}
