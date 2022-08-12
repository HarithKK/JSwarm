package org.usa.soc.gso;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;

public class GlowWorm {

    private Vector position;
    private double l;
    private double r;
    private int numberOfDimensions;
    private double[] minBoundary, maxBoundary;

    public GlowWorm(double l, double r, int numberOfDimensions, double[] minBoundary, double[] maxBoundary) {
        this.l = l;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.setR(r);
    }


    public void updateLuciferin(double ldc, double lac, ObjectiveFunction fn) {
        this.l = ((1-ldc)* getL()) + (lac * fn.setParameters(this.getPosition().getPositionIndexes()).call());
        if(Double.isNaN(this.l)){
            this.l = 0;
        }
    }

    public double getL() {
        return l;
    }

    public Vector getPosition() {
        return position;
    }

    public double getR() {
        return r;
    }

    public void setPosition(Vector v) {
        this.position.setVector(v, minBoundary, maxBoundary);
    }

    public void setR(double r) {
        this.r = r;
    }
}
