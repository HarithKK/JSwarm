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

        setRandomPosition();

        pBest = this.getPosition().getClonedVector();

    }

    private void setRandomPosition() {
        for(int i = 0; i < getPosition().getNumberOfDimensions(); i++){
            getPosition().setValue(Randoms.rand(this.minBoundary[i], this.maxBoundary[i]), i);
        }
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void updatePbest(ObjectiveFunction<Double> objectiveFunction, boolean isLocalMinima) {
        double pBestVal =objectiveFunction.setParameters(this.getPBest().getPositionIndexes()).call();
        double stepBestVal = objectiveFunction.setParameters(this.position.getPositionIndexes()).call();
        if(Validator.validateBestValue(stepBestVal, pBestVal, isLocalMinima)){
            this.getPBest().setVector(this.position, this.minBoundary, this.maxBoundary);
        }
    }

    /*
    \begin{multline*}
        V_i(t) = wV_i(t) + \\
        c_1r_1(pbest_i - P_i(t)) + \\
        c_2r_2(gbest - P_i(t))
    \end{multline*}
     */
    public void updateVelocityAndPosition(Vector gBest, Double c1, Double c2, Double w) {

        double c1r1 = c1 * Randoms.getDoubleRand();
        double c2r2 = c2 * Randoms.getDoubleRand();

        Vector v1 = this.getVelocity().operate(Vector.OPERATOR.MULP, w);
        Vector v2 = this.getPBest().operate(Vector.OPERATOR.SUB, this.position)
                .operate(Vector.OPERATOR.MULP, c1r1);
        Vector v3 =gBest.operate(Vector.OPERATOR.SUB, this.position)
                .operate(Vector.OPERATOR.MULP, c2r2);

        this.setVelocity(v1.operate(Vector.OPERATOR.ADD, v2).operate(Vector.OPERATOR.ADD, v3));
        this.position.setVector(this.position.operate(Vector.OPERATOR.ADD, this.getVelocity()), this.minBoundary, this.maxBoundary);

    }

    public Vector getVelocity() {
        return velocity.getClonedVector();
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Vector getPBest() {
        return pBest;
    }
}
