package examples.si.algo.gwo;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.Agent;
import org.usa.soc.util.Randoms;

public class Wolf extends Agent {

    public Wolf(int numberOfDimensions, double[] minBoundary, double[] maxBoundary) {
        this.maxBoundary =maxBoundary;
        this.minBoundary = minBoundary;
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0 , 1);
    }
    public Wolf getClonedWolf(){
        Wolf w = new Wolf(this.position.getNumberOfDimensions(), minBoundary, maxBoundary);
        w.setPosition(this.getPosition());
        w.setFitnessValue(this.fitnessValue);
        return w;
    }
}
