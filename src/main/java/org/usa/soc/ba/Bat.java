package org.usa.soc.ba;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class Bat {

    private Vector position;
    private Vector best;
    private Vector velocity;
    private Vector f;
    private double[] minBoundary;
    private double[] maxBoundary;
    private int numberOfDimensions;

    private double alpha;
    private double a;
    private double gamma;
    private double r;
    private double r0;

    private int step;

    public Bat(double[] minBoundary, double[] maxBoundary, int numberOfDimensions, double alpha, double a0, double gamma, double r0) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.alpha = alpha;
        this.a = a0;
        this.gamma = gamma;
        this.r = r0;
        this.r0 = r0;
        this.step =0;

        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1);
        this.velocity = position.getClonedVector();
        this.best = position.getClonedVector();
    }

    public void initiatePulseFrequency(double fMax, double fMin, Vector beta) {
        this.f = beta.operate(Vector.OPERATOR.MULP, (fMax - fMin)).operate(Vector.OPERATOR.ADD, fMin);
    }

    public void updatePulseRates() {
        this.r = this.r * (1 - Math.exp(-(gamma * step)));
        step++;
    }

    public void updateLoudness() {
        if(this.a < 0){
            this.a = 1;
        }else{
            this.a *= alpha;
        }

    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void updatePosition(Vector gBest) {

        this.velocity.setVector(this.position.operate(Vector.OPERATOR.SUB, gBest)
                .operate(Vector.OPERATOR.MULP, f)
                .operate(Vector.OPERATOR.ADD, this.velocity));
        this.position.setVector(this.position.operate(Vector.OPERATOR.ADD, this.velocity), minBoundary, maxBoundary);
    }

    public void updatePBest(ObjectiveFunction objectiveFunction, boolean isLocalMinima, Vector newSolution) {

        if(Randoms.rand(0, r0) < r){
            return;
        }

        Double f1 = objectiveFunction.setParameters(newSolution.getPositionIndexes()).call();
        Double f2 = objectiveFunction.setParameters(getBest().getPositionIndexes()).call();

        if(Validator.validateBestValue(f1, f2, isLocalMinima)){
            this.best.setVector(newSolution);
        }
    }

    public double getA() {
        return a;
    }

    public Vector generateNewSolution(double aavg) {
        return position.operate(Vector.OPERATOR.ADD, (Randoms.rand(-1, 1) * aavg));
    }

    public Vector getBest() {
        return best;
    }
}
