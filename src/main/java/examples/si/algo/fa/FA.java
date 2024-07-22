package examples.si.algo.fa;

import org.usa.soc.si.Algorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class FA extends Algorithm {

    private int numberOfFlies;

    private double gama, alpha, beta0, characteristicLength;

    private static final double ALPHA_DECENT = 0.97;

    public FA(ObjectiveFunction<Double> objectiveFunction,
              int stepsCount,
              int numberOfDimensions,
              int numberOfFlies,
              double[] minBoundary,
              double[] maxBoundary,
              double characteristicLength,
              double alpha,
              double beta0,
              boolean isGlobalMinima){

        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.characteristicLength = characteristicLength;
        this.gama = 1 / Math.pow(characteristicLength, 2);
        this.alpha = alpha;
        this.beta0 = beta0;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.numberOfFlies = numberOfFlies;
        agents = new ArrayList<>(numberOfFlies);
    }

    @Override
    public void runOptimizer() throws Exception{

        if(!this.isInitialized()){
            throw new RuntimeException("Nests Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();
        for(int step = 0; step< getStepsCount(); step++){
            for(int i=0; i< numberOfFlies; i++){

                Fly fi = (Fly) agents.get(i);

                for(int j=0; j< numberOfFlies; j++){
                    if(i==j){
                        continue;
                    }

                    Fly fj =(Fly) agents.get(j);

                    if(fj.getIntensity() < fi.getIntensity()){

                        double r = fi.getPosition().getDistance(fj.getPosition());
                        double beta = calculateAttractiveness(r);

                        Vector v3 = Randoms.getRandomVector(this.numberOfDimensions, 0, 1)
                                .operate(Vector.OPERATOR.SUB, 0.5)
                                .operate(Vector.OPERATOR.MULP, alpha);
                        Vector v2 = fj.getPosition()
                                .operate(Vector.OPERATOR.SUB, fi.getPosition())
                                .operate(Vector.OPERATOR.MULP, beta);

                        fi.setPosition(
                                fi.getPosition()
                                        .operate(Vector.OPERATOR.ADD, v2)
                                        .operate(Vector.OPERATOR.ADD, v3)
                                        .fixVector(minBoundary, maxBoundary)
                        );

                        fi.updateIntensity(objectiveFunction, gama, r);
                    }
                    updateGBest(fi);
                    this.alpha *= ALPHA_DECENT;
                }
            }
            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private void updateGBest(Fly fi) {
        Double fpbest = this.objectiveFunction.setParameters(fi.getPosition().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())){
            this.gBest.setVector(fi.getPosition());
        }
    }

    private double calculateAttractiveness(double r) {
        return beta0 * Math.exp(-(gama * Math.pow(r, 2)));
    }

    @Override
    public void initialize() {
        setInitialized(true);

        if(!Validator.validateRangeInOneAndZero(alpha)){
            throw new RuntimeException("Alpha should be between 0 and 1");
        }

        for(int i = 0; i< numberOfFlies; i++){
            Fly d = new Fly(minBoundary, maxBoundary, numberOfDimensions);
            d.setIntensity(objectiveFunction.setParameters(d.getPosition().getPositionIndexes()).call());
            updateGBest(d);
            this.agents.set(i, d);
        }
    }
}
