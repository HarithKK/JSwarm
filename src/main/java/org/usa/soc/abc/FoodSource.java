package org.usa.soc.abc;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class FoodSource {
    private Vector position;
    private Vector bestPosition;
    double[] minBoundary;
    double[] maxBoundary;
    int numberOfDimensions;

    private double fm;

    private double probability;

    private int trials;

    public FoodSource(double[] minBoundary, double[] maxBoundary, int numberOfDimensions) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1);
        this.bestPosition = position.getClonedVector();
        this.trials =0;
    }


    public double getFm() {
        return fm;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position.setVector(position);
    }

    public void calculateNextBestPosition(FoodSource neighbourBeeOccupied) {
        Vector v1 = this.getPosition()
                .operate(Vector.OPERATOR.ADD, neighbourBeeOccupied.getPosition())
                .operate(Vector.OPERATOR.MULP, Randoms.rand(-1,1));

        this.position.setVector( this.position.operate(Vector.OPERATOR.SUB, v1).fixVector(minBoundary, maxBoundary));
    }

    public void calculateFitness(ObjectiveFunction fn) {
        double fm = fn.setParameters(position.getPositionIndexes()).call();
        if(fm >= 0){
            this.fm = 1 / (1+fm);
        }else{
            this.fm = Math.abs(1+ fm);
        }
    }

    public void calculateProbabilities(double totalFm) {
        this.probability = this.fm / totalFm;
    }

    public Vector getBestPosition() {
        return bestPosition;
    }

    public void updateBestPosition() {
        if(Randoms.rand(0,1) > probability){
            this.bestPosition.setVector(this.position);
        }else{
            trials += 1;
        }
    }

    public int getTrials() {
        return trials;
    }

    public void reInitiate() {
        this.position.setVector(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1));
        this.bestPosition = position.getClonedVector();
        this.trials =0;
    }
}
