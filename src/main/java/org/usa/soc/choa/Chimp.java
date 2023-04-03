package org.usa.soc.choa;

import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;

public class Chimp {

    private Vector position;
    private double fitnessValue;
    private double m;
    private double a;
    private double da;
    private double dc;
    private double db;
    private double dd;
    private Vector c;
    private int numberOfDimensions;

    public Chimp(int numberOfDimensions,double[] minBoundary, double[] maxBoundary) {
        this.numberOfDimensions = numberOfDimensions;
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1);
        this.c = new Vector(numberOfDimensions);
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

    public void updateFMAC(double f, Chaotics.type type) {
        c.setVector(Randoms.getRandomVector(numberOfDimensions,0, 1)
                .operate(Vector.OPERATOR.MULP, 2.0));
        m = Chaotics.getChaoticValue(type, f);
        a = f*(2*Randoms.rand(0,1) -1);
    }

    public void updateDValues(Chimp attacker, Chimp chaser, Chimp barrier, Chimp divider) {
        da = attacker.getC().operate(Vector.OPERATOR.MULP, attacker.getPosition())
                .operate(Vector.OPERATOR.SUB, getPosition().operate(Vector.OPERATOR.MULP, m))
                .getMagnitude() * attacker.getA();
        db = barrier.getC().operate(Vector.OPERATOR.MULP, barrier.getPosition())
                .operate(Vector.OPERATOR.SUB, getPosition().operate(Vector.OPERATOR.MULP, m))
                .getMagnitude() * barrier.getA();
        dc = chaser.getC().operate(Vector.OPERATOR.MULP, chaser.getPosition())
                .operate(Vector.OPERATOR.SUB, getPosition().operate(Vector.OPERATOR.MULP, m))
                .getMagnitude() * chaser.getA();
        dd = divider.getC().operate(Vector.OPERATOR.MULP, divider.getPosition())
                .operate(Vector.OPERATOR.SUB, getPosition().operate(Vector.OPERATOR.MULP, m))
                .getMagnitude() * divider.getA();
    }

    public Vector getPositionFromPrey(Vector vBest, double f){
        Vector cVector = Randoms.getRandomVector(numberOfDimensions,0, 1)
                .operate(Vector.OPERATOR.MULP, 2.0);
        double aValue = f*(2*Randoms.rand(0,1) -1);
        double dValue = cVector.operate(Vector.OPERATOR.MULP, vBest)
                .operate(Vector.OPERATOR.SUB, getPosition().operate(Vector.OPERATOR.MULP, m))
                .getMagnitude();
        return this.getPosition()
                .operate(Vector.OPERATOR.SUB, aValue*dValue);
    }

    public Vector getC() {
        return c;
    }

    public double getA() {
        return a;
    }

    public double getDa() {
        return da;
    }

    public double getDc() {
        return dc;
    }

    public double getDb() {
        return db;
    }

    public double getDd() {
        return dd;
    }
}
