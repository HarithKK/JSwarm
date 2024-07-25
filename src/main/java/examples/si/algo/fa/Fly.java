package examples.si.algo.fa;

import org.usa.soc.si.Agent;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

public class Fly extends Agent {

    private double intensity;

    public Fly(double[] minBoundary, double[] maxBoundary, int numberOfDimensions) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;

        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1);
    }

    public double updateIntensity(ObjectiveFunction f, double gama, double r) {
        return f.setParameters(this.getPosition().getPositionIndexes()).call() * Math.exp(-(gama * Math.pow(r, 2)));
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public double getIntensity() {
        return intensity;
    }
}
