package org.usa.soc.si.aco;

import org.usa.soc.core.Algorithm;
import org.usa.soc.core.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

/*
Toksari, M. Duran. "Ant colony optimization for finding the global minimum." Applied Mathematics and computation 176.1 (2006): 308-316.
 */

public class ACO extends Algorithm {

    private Ant[] ants;
    private int numberOfAnts;
    private int numberOfProcessIterations;

    private double alpha;

    private double evaporationRate;

    private double pheromoneValue;

    public ACO(
            ObjectiveFunction objectiveFunction,
            int numberOfAnts,
            int numberOfIterations,
            int numberOfProcessIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            double alpha,
            double evaporationRate,
            double pheromoneValue,
            boolean isGlobalMinima
    ){
        this.objectiveFunction = objectiveFunction;
        this.numberOfAnts = numberOfAnts;
        this.stepsCount = numberOfIterations;
        this.numberOfProcessIterations = numberOfProcessIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.alpha = alpha;
        this.evaporationRate = evaporationRate;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = new Vector(this.numberOfDimensions);
        this.pheromoneValue = pheromoneValue;
        this.isGlobalMinima = isGlobalMinima;
        this.ants = new Ant[this.numberOfAnts];
    }

    public ACO(
            ObjectiveFunction objectiveFunction,
            int numberOfAnts,
            int numberOfIterations,
            int numberOfProcessIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima
    ){
        this.objectiveFunction = objectiveFunction;
        this.numberOfAnts = numberOfAnts;
        this.stepsCount = numberOfIterations;
        this.numberOfProcessIterations = numberOfProcessIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.alpha = calculateAlpha();
        this.evaporationRate = 0.01;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = new Vector(this.numberOfDimensions);
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.pheromoneValue = 0.1;
        this.isGlobalMinima = isGlobalMinima;
        this.ants = new Ant[this.numberOfAnts];
    }

    private double calculateAlpha() {
        return 1 / Randoms.rand(0,10);
    }

    @Override
    public void runOptimizer() throws Exception{
        if(!this.isInitialized()){
            throw new RuntimeException("Ants Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();
        long I = Math.round(Math.sqrt(this.getStepsCount()));

        for(int i=1; i<= numberOfProcessIterations; i++){

            for(long step=i; step< (I*i); step++){
                // generate dx
                Vector dx = Randoms.getRandomVector(this.numberOfDimensions, -(this.alpha), this.alpha);

                for(Ant a: this.ants){
                    a.setPosition(getPositionVector(a, dx));
                    a.updatePBest(this.objectiveFunction, this.isGlobalMinima);

                    // update pheromones
                    this.pheromoneValue = this.pheromoneValue + (this.evaporationRate * this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call());
                    updateBest(a);
                }
                if(this.stepAction != null)
                    this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), i);
                stepCompleted(step);
            }
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private Vector getPositionVector(Ant a, Vector v) {

        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();
        Double fpbest = this.objectiveFunction.setParameters(a.getPbest().getPositionIndexes()).call();

        boolean sign = !Validator.validateBestValue(fpbest, fgbest, isGlobalMinima);

        return a.getPosition().operate(sign ? Vector.OPERATOR.ADD : Vector.OPERATOR.SUB, v);
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        for(int i=0;i<this.numberOfAnts; i++){
            Ant ant = new Ant(this.numberOfDimensions, this.minBoundary, this.maxBoundary);
            this.ants[i] = ant;
            this.updateBest(ant);
        }
    }

    private void updateBest(Ant a){

        Double fpbest = this.objectiveFunction.setParameters(a.getPbest().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima)){
            this.gBest.setVector(a.getPbest());
        }
    }

    @Override
    public Algorithm clone() throws CloneNotSupportedException {
        return new ACO(
                objectiveFunction,
                numberOfAnts,
                getStepsCount(),
                numberOfProcessIterations,
                numberOfDimensions,
                minBoundary,
                maxBoundary,
                alpha,
                evaporationRate,
                pheromoneValue,
                isGlobalMinima);
    }

    @Override
    public double[][] getDataPoints(){
        double[][] data = new double[this.numberOfDimensions][this.numberOfAnts];
        for(int i=0; i< this.numberOfAnts; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.ants[i].getPosition().getValue(j),2);
            }
        }
        return data;
    };

}
