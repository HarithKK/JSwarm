package examples.si.algo.geo;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.Agent;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class Eagle extends Agent {

    private Vector localBestPositon;
    private double localBestFitnessValue;

    public Eagle(int numberOfDimensions, double[] minBoundary, double[] maxBoundary){
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary,0,1);
        this.localBestPositon = position.getClonedVector();
    }
    
    public Vector getLocalBestPositon() {
        return localBestPositon.getClonedVector();
    }

    public void setLocalBestPositon(Vector localBestPositon) {
        this.localBestPositon.setVector(localBestPositon);
    }

    public double getLocalBestFitnessValue() {
        return localBestFitnessValue;
    }

    public void setLocalBestFitnessValue(double localBestFitnessValue) {
        this.localBestFitnessValue = localBestFitnessValue;
    }

    public void updateLocalBest(boolean isGlobalMinima) {
        if(Validator.validateBestValue(fitnessValue, localBestFitnessValue, isGlobalMinima)){
            this.setLocalBestPositon(this.getPosition());
            this.setLocalBestFitnessValue(this.fitnessValue);
        }
    }
}
