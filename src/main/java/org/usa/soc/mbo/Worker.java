package org.usa.soc.mbo;

import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;

public class Worker {

    private Vector position;

    private double[] minBoundary, maxBoundary;
    public Worker(int numberOfDimensions, double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.maxBoundary = maxBoundary;
        this.minBoundary = minBoundary;
    }

    public Vector getPosition() {
        return position;
    }
}
