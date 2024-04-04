package org.usa.soc.zoa;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class Zebra {
    private Vector position;
    private double fitnessValue;

    public Zebra(int numberOfDimensions,double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1);
    }

    public Vector getPosition() {
        return position.getClonedVector();
    }

    public void setPosition(Vector position) {
        this.position.setVector(position);
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public void updatePosition(ObjectiveFunction objectiveFunction, Vector newX, boolean isGlobalMinima) {
        double fitnessValueNewX = objectiveFunction.setParameters(newX.getPositionIndexes()).call();
        if(Validator.validateBestValue(
                fitnessValueNewX,
                this.getFitnessValue(),
                isGlobalMinima)
        ){
            this.setPosition(newX);
            this.setFitnessValue(fitnessValueNewX);
        }
    }
}
