package examples.si.algo.goa;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.Agent;
import org.usa.soc.util.Randoms;

public class GrassHopper extends Agent {
    public GrassHopper(int numberOfDimensions, double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1);
        this.maxBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
    }
}
