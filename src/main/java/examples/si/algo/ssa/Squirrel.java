package examples.si.algo.ssa;

import org.usa.soc.si.Agent;
import org.usa.soc.util.Randoms;

public class Squirrel extends Agent {

    public Squirrel(int numberOfDimensions,double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1);
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
    }

}
