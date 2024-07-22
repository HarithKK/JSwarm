package examples.si.algo.ba;

import org.usa.soc.si.Algorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class BA extends Algorithm {

    private int numberOfBats;

    private double fMin, fMax, alpha, A0, r0, gamma, Aavg;

    public BA(ObjectiveFunction<Double> objectiveFunction,
              int stepsCount,
              int numberOfDimensions,
              double[] minBoundary,
              double[] maxBoundary,
              int numberOfBats,
              double fMin,
              double fMax,
              double alpha,
              double gamma,
              double A0,
              double r0,
              boolean isGlobalMinima){
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.numberOfBats = numberOfBats;
        this.fMin = fMin;
        this.fMax = fMax;
        this.alpha = alpha;
        this.gamma = gamma;
        this.A0 = A0;
        this.r0 = r0;

        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.agents = new ArrayList<>(numberOfBats);
    }
    @Override
    public void runOptimizer() throws Exception {
        if(!this.isInitialized()){
            throw new RuntimeException("Bat Agents Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();
        for(int step = 0; step< getStepsCount(); step++){

            double at =0;
            for(Bat b : (Bat[]) agents.toArray()){
                b.updatePosition(this.gBest);
                Vector newSolution = b.generateNewSolution(Aavg);

                if(Randoms.rand(0, (alpha * A0)) < b.getA()){
                    b.updatePBest(objectiveFunction, isGlobalMinima.isSet(), newSolution);
                }
                b.updatePulseRates();
                b.updateLoudness();
                at += b.getA();
            }
            Aavg = at /(double)numberOfBats;

            for(Bat b: (Bat[]) agents.toArray()){
                updateGBest(b);
            }

            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        double at =0;
        for(int i=0; i< numberOfBats; i++){
            Bat b = new Bat(
                    minBoundary,
                    maxBoundary,
                    numberOfDimensions,
                    alpha,
                    A0,
                    gamma,
                    r0
            );
            b.initiatePulseFrequency(fMax, fMin, Randoms.getRandomVector(numberOfDimensions, 0, 1));
            b.updatePulseRates();
            b.updateLoudness();
            at+=b.getA();
            this.agents.set(i, b);

            updateGBest(b);
        }
        this.Aavg = at / numberOfBats;
    }

    private void updateGBest(Bat b) {
        Double fpbest = this.objectiveFunction.setParameters(b.getBest().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())){
            this.gBest.setVector(b.getBest());
        }
    }
}
