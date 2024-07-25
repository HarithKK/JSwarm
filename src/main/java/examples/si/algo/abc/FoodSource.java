package examples.si.algo.abc;

import org.usa.soc.si.Agent;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

public class FoodSource extends Agent {

    private double fm;
    private double counter;

    private double probability;

    private int trials;

    public FoodSource(double[] minBoundary, double[] maxBoundary, int numberOfDimensions) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1);
        this.trials =0;
    }


    public double getFm() {
        return fm;
    }

    public Vector getPosition() {
        return position.getClonedVector();
    }

    public void setPosition(Vector position) {
        this.position.setVector(position);
    }

    public Vector calculateNextBestPosition(FoodSource neighbourBeeOccupied) {
        return this.getPosition().operate(Vector.OPERATOR.SUB, neighbourBeeOccupied.getPosition())
                .operate(Vector.OPERATOR.MULP, Randoms.rand(-1,1))
                .operate(Vector.OPERATOR.ADD, this.getPosition());
    }

    public double calculateFitness(ObjectiveFunction fn, Vector v) {
        double fm = fn.setParameters(v.getPositionIndexes()).call();
        if(fm >= 0){
            fm = 1 / (1+fm);
        }else{
            fm = Math.abs(1+ fm);
        }
        return fm;
    }

    public double calculateProbabilities(double totalFm) {
        this.probability = this.getFm() / totalFm;
        return this.getProbability();
    }

    public int getTrials() {
        return trials;
    }

    public void reInitiate() {
        this.position.setVector(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1));
        this.trials =0;
    }

    public void setFm(double fm) {
        this.fm = fm;
    }

    public double getCounter() {
        return counter;
    }

    public void setCounter(double counter) {
        this.counter = counter;
    }

    public double getProbability() {
        return probability;
    }
}
