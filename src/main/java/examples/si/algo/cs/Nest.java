package examples.si.algo.cs;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.Agent;
import org.usa.soc.util.Randoms;

public class Nest extends Agent {
    private Vector best;

    public Nest(double[] minBoundary, double[] maxBoundary, int numberOfDimensions) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1);
        this.best = this.position;
    }
}
