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
            double totalFM = 0;

            for(int i=0; i< numberOfFoodSources; i++){
               totalFM += runEmployeeBeePhase(i);
            }

            runOnlookerBeePhase(totalFM);
            runScoutBeePhase();

            for(FoodSource f: foodSources){
                updateGbest(f);
            }

            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest.getClonedVector(), this.getBestDoubleValue(), step);
            stepCompleted(time, step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private void runScoutBeePhase() {
        for (FoodSource f: foodSources) {
            if(f.getTrials() >= maxTrials){
                f.reInitiate();
                f.setFm(f.calculateFitness(objectiveFunction, f.getPosition()));
                f.setCounter(0);
            }
        }
    }

    private void runOnlookerBeePhase(double totalFM) {
        double b =0;
        for (int i=0; i<numberOfFoodSources ;i++) {
            FoodSource cb = foodSources[i];
            b += (Randoms.rand(0,1) * cb.calculateProbabilities(totalFM));

            for(int j=0; j<numberOfFoodSources ;j++){
                if(j==i)
                    continue;
                if(b < foodSources[j].getProbability()){
                    runEmployeeBeePhase(j);
                    break;
                }
            }
        }
    }

    private void updateGbest(FoodSource f) {
        Double fpbest = this.objectiveFunction.setParameters(f.getPosition().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(gBest.getPositionIndexes()).call();
        System.out.println(fpbest +"  |  "+ fgbest);
        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            gBest.setVector(f.getPosition());
        }
    }

    private double runEmployeeBeePhase(int i) {
        FoodSource currentBee = foodSources[i];
        FoodSource neighbourBeeOccupied = foodSources[getRandomFoodSource(i)];
        Vector v = currentBee.calculateNextBestPosition(neighbourBeeOccupied);
        Double fm = currentBee.calculateFitness(objectiveFunction, v);
        double pfm = currentBee.calculateFitness(objectiveFunction, currentBee.getPosition());
        if(!Validator.validateBestValue(fm,pfm,this.isLocalMinima)){
            currentBee.setPosition(v);
            currentBee.setFm(fm);
            currentBee.setCounter(currentBee.getFm() + 1);
            return fm;
        }else{
            currentBee.setCounter(0);
            return pfm;
        }
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
            f.setFm(f.calculateFitness(objectiveFunction, f.getPosition()));
            f.setCounter(0);
            updateGbest(f);
            this.foodSources[i] = f;
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.numberOfFoodSources];
        for(int i=0; i< this.numberOfFoodSources; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.foodSources[i].getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
