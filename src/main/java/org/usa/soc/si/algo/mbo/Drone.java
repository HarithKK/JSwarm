package org.usa.soc.si.algo.mbo;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

public class Drone {
    private Vector position;

    private double[] minBoundary, maxBoundary;
    public Drone(int numberOfDimensions, double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.maxBoundary = maxBoundary;
        this.minBoundary = minBoundary;
    }

    public Vector getPosition() {
        return position.getClonedVector();
    }
}
