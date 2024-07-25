package examples.si.algo.tsa;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.Agent;
import org.usa.soc.util.Randoms;

public class Tunicate extends Agent {

    public Tunicate(int numberOfDimensions,double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1);
    }

}
