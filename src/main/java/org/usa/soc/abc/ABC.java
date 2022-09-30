package org.usa.soc.abc;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class ABC extends Algorithm {

    private int numberOfFoodSources, maxTrials;

    private FoodSource[] foodSources;

    public ABC(ObjectiveFunction<Double> objectiveFunction,
              int stepsCount,
              int numberOfDimensions,
              int numberOfFoodSources,
              double[] minBoundary,
              double[] maxBoundary,
              int maxTrials,
              boolean isLocalMinima){

        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isLocalMinima = isLocalMinima;
        this.numberOfFoodSources = numberOfFoodSources;
        this.maxTrials = maxTrials;

        this.gBest = isLocalMinima ? new Vector(numberOfDimensions).setMaxVector() : new Vector(numberOfDimensions).setMinVector();

        this.foodSources = new FoodSource[numberOfFoodSources];
    }

    @Override
    public void runOptimizer(int time) {
        if(!this.isInitialized()){
            throw new RuntimeException("Food Sources Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();
        for(int step = 0; step< getStepsCount(); step++){

            double totalFm = runEmployeeBeePhase();
            runOnlookerBeePhase(totalFm);
            runScoutBeePhase();

            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(time, step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private void runScoutBeePhase() {
        for (FoodSource f: foodSources) {
            if(f.getTrials() >= maxTrials){
                f.reInitiate();
                f.calculateFitness(objectiveFunction);
            }
        }
    }

    private void runOnlookerBeePhase(double totalFm) {
        for (FoodSource f: foodSources) {
            f.calculateProbabilities(totalFm);
            f.updateBestPosition();
            updateGbest(f);
        }
    }

    private void updateGbest(FoodSource f) {
        Double fpbest = this.objectiveFunction.setParameters(f.getBestPosition().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.gBest.setVector(f.getBestPosition());
        }
    }

    private double runEmployeeBeePhase() {
        double totalFm = 0;
        for(int i=0; i<numberOfFoodSources ;i++){
            FoodSource neighbourBeeOccupied = foodSources[getRandomFoodSource(i)];
            FoodSource currentBee = foodSources[i];
            currentBee.calculateNextBestPosition(neighbourBeeOccupied);
            currentBee.calculateFitness(objectiveFunction);
            totalFm += currentBee.getFm();
        }
        return totalFm;
    }

    private int getRandomFoodSource(int current) {
        for(int i=0; i< numberOfFoodSources;i++){
            int n = (int)Randoms.rand(0, numberOfFoodSources-1);
            if(n != current){
                return n;
            }
        }
        return current;
    }

    @Override
    public void initialize() {
        setInitialized(true);

        for(int i = 0; i< numberOfFoodSources; i++){
            FoodSource f = new FoodSource(minBoundary, maxBoundary, numberOfDimensions);
            f.calculateFitness(objectiveFunction);
            this.foodSources[i] = f;
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.numberOfFoodSources];
        for(int i=0; i< this.numberOfFoodSources; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.foodSources[i].getBestPosition().getValue(j),2);
            }
        }
        return data;
    }
}
