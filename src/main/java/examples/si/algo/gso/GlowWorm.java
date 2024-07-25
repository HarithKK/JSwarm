package examples.si.algo.gso;

import org.usa.soc.si.Agent;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

public class GlowWorm extends Agent {

    private double l;
    private double r;
    private int numberOfDimensions;

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

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }
}
