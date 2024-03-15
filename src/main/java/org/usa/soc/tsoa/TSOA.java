package org.usa.soc.tsoa;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TSOA extends Algorithm {

    private int populationSize;

    private List<Tree> trees;
    private double distanceFactor =0;

    public TSOA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isLocalMinima
    ){

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isLocalMinima = isLocalMinima;
        this.distanceFactor = 5;

        trees = new ArrayList<>(this.populationSize);
    }

    @Override
    public void runOptimizer() throws Exception {
        if(!this.isInitialized()){
            throw new RuntimeException("Trees Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        for(int step = 0; step< getStepsCount(); step++){

            Collections.sort(trees, new TreeComparator());
            updateGBest(trees.get(0));

            Vector predicted = new Vector(this.numberOfDimensions);
            Tree t = null;

            for(int i =0; i <this.trees.size(); i++){
                t = trees.get(i);
                predicted = predicted.operate(Vector.OPERATOR.ADD, t.getPosition().operate(Vector.OPERATOR.MULP, t.getLambda()));
            }

            double totalLabmda = 0;
            double totalDistance = 0;
            for(int i =0; i <this.trees.size(); i++){
                t = trees.get(i);
                totalLabmda += t.getLambda();
                totalDistance += t.getCalculatedDistance(predicted);
            }

            for(int i = this.trees.size()/2; i <this.trees.size(); i++){
                t = trees.get(i);
                t.setPosition(predicted.operate(Vector.OPERATOR.ADD, Randoms.getRandomVector(numberOfDimensions, 1, this.distanceFactor)));
                t.updateLambda(totalLabmda, totalDistance);
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

        for(int i=0; i<this.populationSize; i++){
            Tree tree = new Tree(numberOfDimensions, minBoundary, maxBoundary);
            tree.setFitnessValue(objectiveFunction.setParameters(tree.getPosition().getPositionIndexes()).call());
            this.trees.add(tree);
        }

    }

    private void updateGBest(Tree tree) {
        Double fgbest = this.objectiveFunction.setParameters(gBest.getPositionIndexes()).call();
        Double fpbest = this.objectiveFunction.setParameters(tree.getPosition().getPositionIndexes()).call();
        if (Validator.validateBestValue(fpbest, fgbest, isLocalMinima)) {
            this.gBest.setVector(tree.getPosition());
        }
    }

    @Override
    public double[][] getDataPoints() {
        return new double[0][];
    }
}
