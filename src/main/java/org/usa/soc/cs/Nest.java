package org.usa.soc.cs;

import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;

public class Nest {
    private Vector position, best;
    double[] minBoundary;
    double[] maxBoundary;
    int numberOfDimensions;

    public Nest(double[] minBoundary, double[] maxBoundary, int numberOfDimensions) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1);
        this.best = this.position;
    }

    public Vector getPosition() {
        return position.getClonedVector();
    }

    public void setPosition(Vector position) {
        this.position = position.getClonedVector();
    }
}
