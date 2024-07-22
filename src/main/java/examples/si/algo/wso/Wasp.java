package examples.si.algo.wso;

import org.usa.soc.si.Agent;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Validator;

public class Wasp extends Agent {

    private Vector solution;

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
        this.solution.setVector(v, minBoundary, maxBoundary);
    }

    public void updateDiversity(ObjectiveFunction fn, boolean isGlobalMinima) {

        Double fgbest = fn.setParameters(this.getBestSolution().getPositionIndexes()).call();
        Double fpbest = fn.setParameters(this.getSolution().getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima)){
            this.bestSolution.setVector(this.getSolution(), minBoundary, maxBoundary);
            this.diversitySolution = Mathamatics.calculateHammingDistance(fpbest.doubleValue(), fgbest.doubleValue());
        }

    }

    public Vector getBestSolution() {
        return bestSolution.getClonedVector();
    }

    public void updateForce(double c1, double c2, ObjectiveFunction fn) {
        this.force = c1 * fn.setParameters(this.bestSolution.getPositionIndexes()).call() + c2 * this.diversitySolution;
    }

    public Double getForce() {
        return force;
    }

    public Vector getSolution() {
        return solution.getClonedVector();
    }
}
