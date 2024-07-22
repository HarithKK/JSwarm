package org.usa.soc.si;

import org.usa.soc.core.ds.Vector;

public class Agent {
    private Vector position;
    public double[] minBoundary, maxBoundary;
    public int numberOfDimensions;

    public Agent(double[] minBoundary, double[] maxBoundary, int numberOfDimensions, Vector position) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.position.setVector(position);
    }

    public Vector getPosition() {
        return position.getClonedVector();
    }

    public void setPosition(Vector position) {
        this.position.setVector(position);
    }
}
