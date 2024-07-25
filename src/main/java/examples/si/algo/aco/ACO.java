package examples.si.algo.aco;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

/*
Toksari, M. Duran. "Ant colony optimization for finding the global minimum." Applied Mathematics and computation 176.1 (2006): 308-316.
 */

public class ACO extends SIAlgorithm {
    private int numberOfAnts;
    private int numberOfProcessIterations;

    private double alpha;

    private double evaporationRate;

    private double pheromoneValue;

    private Long I;

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
        this.isGlobalMinima.setValue(isGlobalMinima);
        setFirstAgents("ants", new ArrayList<>(numberOfAnts));
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
        this.isGlobalMinima.setValue(isGlobalMinima);
        setFirstAgents("Ants", new ArrayList<>(numberOfAnts));
    }

    private double calculateAlpha() {
        return 1 / Randoms.rand(0,10);
    }

    @Override
    public void step() throws Exception{

            for(long step=currentStep; step< (I*currentStep); step++){
                // generate dx
                Vector dx = Randoms.getRandomVector(this.numberOfDimensions, -(this.alpha), this.alpha);

                for(AbsAgent agent: getFirstAgents()){
                    Ant a = (Ant)agent;
                    a.setPosition(getPositionVector(a, dx));
                    a.updatePBest(this.objectiveFunction, this.isGlobalMinima.isSet());

                    // update pheromones
                    this.pheromoneValue = this.pheromoneValue + (this.evaporationRate * this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call());
                    updateBest(a);
                }
            }
    }

    private Vector getPositionVector(Ant a, Vector v) {

        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();
        Double fpbest = this.objectiveFunction.setParameters(a.getPbest().getPositionIndexes()).call();

        boolean sign = !Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet());

        return a.getPosition().operate(sign ? Vector.OPERATOR.ADD : Vector.OPERATOR.SUB, v);
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        for(int i=0;i<this.numberOfAnts; i++){
            Ant ant = new Ant(this.numberOfDimensions, this.minBoundary, this.maxBoundary);
            getFirstAgents().add(ant);
            this.updateBest(ant);
        }

        I = Math.round(Math.sqrt(this.getStepsCount()));
    }

    private void updateBest(Ant a){

        Double fpbest = this.objectiveFunction.setParameters(a.getPbest().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())){
            this.gBest.setVector(a.getPbest());
        }
    }

    @Override
    public SIAlgorithm clone() throws CloneNotSupportedException {
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
                isGlobalMinima.isSet());
    }
}
