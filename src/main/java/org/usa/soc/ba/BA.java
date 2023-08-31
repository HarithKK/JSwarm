package org.usa.soc.ba;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class BA extends Algorithm {

    private int numberOfBats;

    private Bat[] bats;

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
              boolean isLocalMinima){
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isLocalMinima = isLocalMinima;
        this.numberOfBats = numberOfBats;
        this.fMin = fMin;
        this.fMax = fMax;
        this.alpha = alpha;
        this.gamma = gamma;
        this.A0 = A0;
        this.r0 = r0;

        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.bats = new Bat[numberOfBats];
    }
    @Override
    public void runOptimizer(int time) throws Exception {
        if(!this.isInitialized()){
            throw new RuntimeException("Bat Agents Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();
        for(int step = 0; step< getStepsCount(); step++){

            double at =0;
            for(Bat b : bats){
                b.updatePosition(this.gBest);
                Vector newSolution = b.generateNewSolution(Aavg);

                if(Randoms.rand(0, (alpha * A0)) < b.getA()){
                    b.updatePBest(objectiveFunction, isLocalMinima, newSolution);
                }
                b.updatePulseRates();
                b.updateLoudness();
                at += b.getA();
            }
            Aavg = at /(double)numberOfBats;

            for(Bat b: bats){
                updateGBest(b);
            }

            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(time, step);
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
            this.bats[i] = b;

            updateGBest(b);
        }
        this.Aavg = at / numberOfBats;
    }

    private void updateGBest(Bat b) {
        Double fpbest = this.objectiveFunction.setParameters(b.getBest().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.gBest.setVector(b.getBest());
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.numberOfBats];
        for(int i=0; i< this.numberOfBats; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.bats[i].getBest().getValue(j),2);
            }
        }
        return data;
    }
}
