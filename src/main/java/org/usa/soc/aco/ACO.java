package org.usa.soc.aco;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.IAlgorithm;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

/*
Toksari, M. Duran. "Ant colony optimization for finding the global minimum." Applied Mathematics and computation 176.1 (2006): 308-316.
 */

public class ACO implements IAlgorithm {

    private ObjectiveFunction fn;
    private Ant[] ants;
    private int numberOfIterations;
    private int numberOfAnts;
    private int numberOfProcessIterations;
    private int numberOfDimensions;

    private double[] minBoundary, maxBoundary;

    private double alpha;

    private double evaporationRate;

    private boolean isInitialized = false;

    private Vector gBest;

    private boolean isLocalMinima;

    private long nanoDuration;

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
        this.fn = objectiveFunction;
        this.numberOfAnts = numberOfAnts;
        this.numberOfIterations = numberOfIterations;
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
        this.fn = objectiveFunction;
        this.numberOfAnts = numberOfAnts;
        this.numberOfIterations = numberOfIterations;
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
        if(!this.isInitialized){
            throw new RuntimeException("Ants Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();
        long I = Math.round(Math.sqrt(this.numberOfIterations));

        for(int i=1; i<= numberOfProcessIterations; i++){

            for(long step=i; step< (I*i); step++){
                // generate dx
                Vector dx = Randoms.getRandomVector(this.numberOfDimensions, -(this.alpha), this.alpha);

                for(Ant a: this.ants){
                    a.setPosition(getPositionVector(a, dx));
                    a.updatePBest(this.fn, this.isLocalMinima);

                    // update pheromones
                    this.pheromoneValue = this.pheromoneValue + (this.evaporationRate * this.fn.setParameters(this.gBest.getPositionIndexes()).call());
                }
            }
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private Vector getPositionVector(Ant a, Vector v) {

        Double fgbest = this.fn.setParameters(this.gBest.getPositionIndexes()).call();
        Double fpbest = this.fn.setParameters(a.getPbest().operate(Vector.OPERATOR.ADD, 0.01).getPositionIndexes()).call();

        boolean sign = (isLocalMinima && fgbest <= fpbest) || (!isLocalMinima && fgbest >= fpbest);

        return a.getPosition().operate(sign ? Vector.OPERATOR.ADD : Vector.OPERATOR.SUB, v);
    }

    @Override
    public long getNanoDuration() {
        return this.nanoDuration;
    }

    @Override
    public void initialize() {
        this.isInitialized = true;
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        for(int i=0;i<this.numberOfAnts; i++){
            Ant ant = new Ant(this.numberOfDimensions, this.minBoundary, this.maxBoundary);
            this.ants[i] = ant;
            this.updateBest(ant);
        }
    }

    private void updateBest(Ant a){
        Double fpbest = this.fn.setParameters(a.getPbest().getPositionIndexes()).call();
        Double fgbest = this.fn.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.gBest.setVector(a.getPosition());
        }
    }

    @Override
    public String getBestValue() {
        return String.valueOf(this.fn.setParameters(this.gBest.getPositionIndexes()).call());
    }

    @Override
    public Double getBestDValue() {
        return this.fn.setParameters(this.gBest.getPositionIndexes()).call();
    }

    @Override
    public ObjectiveFunction getFunction() {
        return this.fn;
    }

    @Override
    public String getBestVariables() {
        return this.gBest.toString();
    }

    @Override
    public IAlgorithm clone() throws CloneNotSupportedException {
        return new ACO(
                fn,
                numberOfAnts,
                numberOfIterations,
                numberOfProcessIterations,
                numberOfDimensions,
                minBoundary,
                maxBoundary,
                alpha,
                evaporationRate,
                pheromoneValue,
                isLocalMinima);
    }

    @Override
    public boolean isMinima() {
        return this.isLocalMinima;
    }

    @Override
    public Vector getBestVector() {
        return this.gBest;
    }

}
