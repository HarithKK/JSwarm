package org.usa.soc.wso;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Validator;

public class Wasp {

    private double[] minBoundary, maxBoundary;
    private Vector solution;

    private int numberOfDimensions;

    private Vector bestSolution;

    private Integer diversitySolution;
    private Double force;

    public Wasp(double[] minBoundary, double[] maxBoundary, int numberOfDimensions) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;

        this.bestSolution = this.solution = new Vector(this.numberOfDimensions);
        this.diversitySolution = 0;
        this.force = 0.0;
    }

    public void setSolution(Vector v){
        this.solution = v.getClonedVector();
    }

    public void updateDiversity(ObjectiveFunction fn, boolean isLocalMinima) {

        Double fgbest = fn.setParameters(this.getBestSolution().getPositionIndexes()).call();
        Double fpbest = fn.setParameters(this.solution.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.bestSolution = this.solution.getClonedVector();
            this.diversitySolution = Mathamatics.calculateHammingDistance(fpbest.doubleValue(), fgbest.doubleValue());
        }

    }

    public Vector getBestSolution() {
        return bestSolution;
    }

    public void updateForce(double c1, double c2, ObjectiveFunction fn) {
        this.force = c1 * fn.setParameters(this.bestSolution.getPositionIndexes()).call() + c2 * this.diversitySolution;
    }

    public Double getForce() {
        return force;
    }
}