package examples.si.algo.tsoa;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.Agent;
import org.usa.soc.util.Randoms;

public class Tree extends Agent {

    private double lambda = 0;
    private double distance = 0;
    private double w = 0;

    public Tree(int numberOfDimensions,double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1);
        this.setLambda(1);
    }

    public double getLambda() {
        return lambda;
    }

    public double getCalculatedDistance(Vector predicted) {
        this.distance = this.getPosition().operate(Vector.OPERATOR.SUB, predicted).getMagnitude();
        return this.distance;
    }

    public void updateLambda(double p, double totalLabmda, double totalDistance) {
        this.setLambda((Math.pow(this.distance, -p) * this.w ) / ((totalDistance)* totalLabmda));
    }

    public void updateWeight(double totalFitnessValue) {
        this.setW(this.fitnessValue / totalFitnessValue);
    }

    public void setLambda(double lambda) {
        if(!Double.isNaN(lambda)){
            this.lambda = lambda;
        }
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }
}
