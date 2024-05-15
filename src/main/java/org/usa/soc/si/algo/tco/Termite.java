package org.usa.soc.si.algo.tco;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

public class Termite {

    private Vector position;
    double[] minBoundary;
    double[] maxBoundary;
    int numberOfDimensions;
    private double pValue;

    public Termite(double[] minBoundary, double[] maxBoundary, int numberOfDimensions, double pValue) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.pValue = pValue;

        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1);
    }

    public void updatePheramoneValue(double eRate, ObjectiveFunction<Double> objectiveFunction) {
        double d = objectiveFunction.setParameters(this.getPosition().getPositionIndexes()).call();
        this.pValue = ((1 - eRate) * this.getpValue()) + 1 / (d + 1);
    }

    public Vector getPosition() {
        return position.getClonedVector();
    }

    public void updatePositionByRandomWalk(Vector v) {
        Vector rv = Randoms.getRandomVector(this.position.getClonedVector(), v);
        this.position.setVector(this.position.operate(Vector.OPERATOR.ADD, rv), minBoundary, maxBoundary );
    }

    public double getpValue() {
        return pValue;
    }

    public void updatePosition(double omega, Vector gBest) {
        Vector v = gBest.operate(Vector.OPERATOR.SUB, position)
                .operate(Vector.OPERATOR.MULP,omega)
                .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1))
                .operate(Vector.OPERATOR.ADD, position);
        this.position.setVector(v, minBoundary, maxBoundary);
    }
}
