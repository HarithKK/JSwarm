package examples.si.algo.gwo;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;

public class Wolf {

    private double[] minBoundary;
    private double[] maxBoundary;

    private Vector position;

    private double fitnessValue;
    public Wolf(int numberOfDimensions, double[] minBoundary, double[] maxBoundary) {
        this.maxBoundary =maxBoundary;
        this.minBoundary = minBoundary;
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0 , 1);
    }

    public Vector getPosition() {
        return position.getClonedVector();
    }

    public void setPosition(Vector position) {
        this.position.setVector(position, minBoundary, maxBoundary);
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public Wolf getClonedWolf(){
        Wolf w = new Wolf(this.position.getNumberOfDimensions(), minBoundary, maxBoundary);
        w.setPosition(this.getPosition());
        w.setFitnessValue(this.fitnessValue);
        return w;
    }
}
