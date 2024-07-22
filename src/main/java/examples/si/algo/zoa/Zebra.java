package examples.si.algo.zoa;

import org.usa.soc.si.Agent;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class Zebra extends Agent {

    public Zebra(int numberOfDimensions,double[] minBoundary, double[] maxBoundary) {
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0,1);
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
