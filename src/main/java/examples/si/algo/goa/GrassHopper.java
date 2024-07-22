package examples.si.algo.goa;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.Agent;
import org.usa.soc.util.Randoms;

public class GrassHopper extends Agent {
    private Vector totalDistanceVector;

    public GrassHopper(int numberOfDimensions, double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1);
        this.totalDistanceVector = new Vector(numberOfDimensions);
    }


    public Vector getTotalDistanceVector() {
        return totalDistanceVector;
    }

    public void setTotalDistanceVector(Vector totalDistanceVector) {
        this.totalDistanceVector.setVector(totalDistanceVector);
    }

    public void addTotalDistance(Vector totalDistance) {
        this.totalDistanceVector.setVector(this.totalDistanceVector.operate(Vector.OPERATOR.ADD,totalDistance));
    }
}
