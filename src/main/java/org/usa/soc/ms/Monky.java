package org.usa.soc.ms;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class Monky {

    private Vector bestRoot;
    private double[] minBoundary, maxBoundary;
    private int numberOfDimensions, maxHeightOfTheTree;

    private ObjectiveFunction fn;
    public Monky(double[] maxBoundary, double[] minBoundary, int numberOfDimensions, int maxHeightOfTheTree, ObjectiveFunction fn) {
        this.maxBoundary = maxBoundary;
        this.minBoundary = minBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.maxHeightOfTheTree = maxHeightOfTheTree;
        this.fn=fn;
        this.bestRoot = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary,0,1);
    }

    public Vector getBestRoot() {
        return bestRoot;
    }

    public void climbTree(double c1, boolean isLocalMinima) {

        Vector position = this.bestRoot.getClonedVector();
        for(int i=0; i< this.maxHeightOfTheTree;i++){
            Vector v = createRandomRoot(position, c1);
            Vector r1 = position.operate(Vector.OPERATOR.ADD, v);
            Vector r2 = position.operate(Vector.OPERATOR.SUB, v);

            double vr = this.fn.setParameters(position.getPositionIndexes()).call();
            double vr1 = this.fn.setParameters(r1.getPositionIndexes()).call();
            double vr2 = this.fn.setParameters(r2.getPositionIndexes()).call();

            if(Validator.validateBestValue(vr1, vr, isLocalMinima)){
                position = position.operate(Vector.OPERATOR.ADD,r1);
            }

            if(Validator.validateBestValue(vr2, vr, isLocalMinima)){
                position = position.operate(Vector.OPERATOR.ADD,r2);
            }
        }
        this.bestRoot.setVector(position, this.minBoundary, this.maxBoundary);
    }

    private Vector createRandomRoot(Vector position, double c1) {

        double c1r1 = c1 * Randoms.rand(0,1);
        return this.getBestRoot().getClonedVector().operate(Vector.OPERATOR.SUB, position).operate(Vector.OPERATOR.MULP, c1r1);
    }
}
