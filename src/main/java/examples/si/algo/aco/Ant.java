package examples.si.algo.aco;

import org.usa.soc.si.Agent;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class Ant extends Agent {
    private Vector pbest;

    public Ant(int numberOfDimensions, double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.pbest = this.position.getClonedVector();
        this.maxBoundary = maxBoundary;
        this.minBoundary = minBoundary;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position.setVector(position, minBoundary, maxBoundary);
    }

    public Vector getPbest() {
        return pbest;
    }

    public void updatePBest(ObjectiveFunction<Double> fn, boolean isGlobalMinima) {
        double pBestVal =fn.setParameters(this.pbest.getPositionIndexes()).call();
        double stepBestVal = fn.setParameters(this.position.getPositionIndexes()).call();
        if(Validator.validateBestValue(stepBestVal, pBestVal, isGlobalMinima)){
            this.pbest.setVector(this.position, this.minBoundary, this.maxBoundary);
        }
    }

    public void setBoundaries(double[] minBoundary, double[] maxBoundary) {
        this.maxBoundary = maxBoundary;
        this.minBoundary = minBoundary;
    }
}
