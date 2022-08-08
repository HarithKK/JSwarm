package org.usa.soc.aco;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
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
            boolean isLocalMinima
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
        this.isLocalMinima = isLocalMinima;
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
            boolean isLocalMinima
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
        this.pheromoneValue = 0.1;
        this.isLocalMinima = isLocalMinima;
        this.ants = new Ant[this.numberOfAnts];
    }

    private double calculateAlpha() {
        return 1 / Randoms.rand(0,10);
    }

    @Override
    public void runOptimizer() {
        if(!this.isInitialized()){
            throw new RuntimeException("Ants Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();
        long I = Math.round(Math.sqrt(this.stepsCount));

        for(int i=1; i<= numberOfProcessIterations; i++){

            for(long step=i; step< (I*i); step++){
                // generate dx
                Vector dx = Randoms.getRandomVector(this.numberOfDimensions, -(this.alpha), this.alpha);

                for(Ant a: this.ants){
                    a.setPosition(getPositionVector(a, dx));
                    a.updatePBest(this.objectiveFunction, this.isLocalMinima);

                    // update pheromones
                    this.pheromoneValue = this.pheromoneValue + (this.evaporationRate * this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call());
                }
            }
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private Vector getPositionVector(Ant a, Vector v) {

        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();
        Double fpbest = this.objectiveFunction.setParameters(a.getPbest().operate(Vector.OPERATOR.ADD, 0.01).getPositionIndexes()).call();

        boolean sign = (isLocalMinima && fgbest <= fpbest) || (!isLocalMinima && fgbest >= fpbest);

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

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.gBest.setVector(a.getPosition());
        }
    }

    @Override
    public Algorithm clone() throws CloneNotSupportedException {
        return new ACO(
                objectiveFunction,
                numberOfAnts,
                stepsCount,
                numberOfProcessIterations,
                numberOfDimensions,
                minBoundary,
                maxBoundary,
                alpha,
                evaporationRate,
                pheromoneValue,
                isLocalMinima);
    }

}
