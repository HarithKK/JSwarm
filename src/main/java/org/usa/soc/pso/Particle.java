package org.usa.soc.pso;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class Particle {

    private Vector position;
    private Vector velocity;

    private Vector pBest;
    private double[] minBoundary, maxBoundary;

    public Particle(double[] minBoundary, double[] maxBoundary, int numberOfDimensions) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;

        // initialize particle
        setPosition(new Vector(numberOfDimensions));
        setVelocity(new Vector(numberOfDimensions));

        pBest = this.getPosition().getClonedVector();

    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void updatePbest(ObjectiveFunction<Double> objectiveFunction, boolean isGlobalMinima) {
        ObjectiveFunction fn = objectiveFunction.setParameters(this.getPBest().getPositionIndexes());
        double pBestVal =fn.call();
        double stepBestVal = objectiveFunction.setParameters(this.position.getPositionIndexes()).call();
        if(Validator.validateBestValue(stepBestVal, pBestVal, isGlobalMinima)){
            this.pBest.setVector(this.position.getClonedVector(), this.minBoundary, this.maxBoundary);
        }
    }

    /*
    \begin{multline*}
        V_i(t) = wV_i(t) + \\
        c_1r_1(pbest_i - P_i(t)) + \\
        c_2r_2(gbest - P_i(t))
    \end{multline*}
     */
    public Vector updateVelocity(Vector gBest, Double c1, Double c2, Double w) {

        double c1r1 = c1 * Randoms.rand(0,1);;
        double c2r2 = c2 * Randoms.rand(0,1);;

        Vector v1 = this.getVelocity().operate(Vector.OPERATOR.MULP, w);
        Vector v2 = this.getPBest().operate(Vector.OPERATOR.SUB, this.getPosition())
                .operate(Vector.OPERATOR.MULP, c1r1);
        Vector v3 =gBest.getClonedVector().operate(Vector.OPERATOR.SUB, this.getPosition())
                .operate(Vector.OPERATOR.MULP, c2r2);

        this.setVelocity(v1.operate(Vector.OPERATOR.ADD, v2).operate(Vector.OPERATOR.ADD, v3).fixVector(minBoundary, maxBoundary));
        return this.getPosition().operate(Vector.OPERATOR.ADD, this.getVelocity());
    }

    public Vector getVelocity() {
        return velocity.getClonedVector();
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Vector getPBest() {
        return pBest.getClonedVector();
    }

    public void setPBest(Vector v) {
        this.pBest = v.getClonedVector();
    }

    public void setBoundaries(double[] minBoundary, double[] maxBoundary) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
    }
}
