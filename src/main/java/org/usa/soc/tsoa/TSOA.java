package org.usa.soc.tsoa;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TSOA extends Algorithm {

    private int populationSize;

    private List<Tree> trees;
    private double distanceFactor =0;

    private double c1, c2;

    private int seedsCount = 2;

    public TSOA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
            double distanceFactor
    ){

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isGlobalMinima = isGlobalMinima;
        this.distanceFactor = distanceFactor;

        this.c1 = 10;
        this.c2 = 10;

        trees = new ArrayList<>();
    }

    @Override
    public void runOptimizer() throws Exception {
        System.out.println(this.objectiveFunction.getClass().getSimpleName());
        if(!this.isInitialized()){
            throw new RuntimeException("Trees Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        double distanceDecrement = distanceFactor/stepsCount;

        int deligator = (int)(trees.size()/2);
        int totalSeedsCount = deligator*seedsCount;

        for(int step = 0; step< getStepsCount(); step++){
            System.out.println(step);
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

            for(int i = 0; i < deligator; i++){
                t = trees.get(i);
                for(int j =0; j< seedsCount; j++){
                    Tree newTree = new Tree(numberOfDimensions, minBoundary, maxBoundary);
                    if(Randoms.rand(0,1) < 0.5){
                        Vector v1 = predicted.operate(Vector.OPERATOR.SUB, t.getlBest())
                                .operate(Vector.OPERATOR.MULP, c1)
                                .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                        Vector v2 = this.gBest.getClonedVector()
                                .operate(Vector.OPERATOR.SUB, t.getlBest())
                                .operate(Vector.OPERATOR.MULP, c2)
                                .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                        newTree.setPosition(
                                t.getPosition()
                                        .operate(Vector.OPERATOR.ADD, v1)
                                        .operate(Vector.OPERATOR.ADD, v2)
                                        .operate(Vector.OPERATOR.MULP, distanceFactor)
                                        .fixVector(minBoundary, maxBoundary)
                        );
                    }else{
                        newTree.setPosition(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary));
                    }

                    newTree.setFitnessValue(objectiveFunction.setParameters(newTree.getPosition().getPositionIndexes()).call());
                    if(Validator.validateBestValue(newTree.getFitnessValue(), t.getlBestValue(), isGlobalMinima)){
                        t.setlBest(newTree.getPosition());
                        t.setlBestValue(newTree.getFitnessValue());
                    }
                    newTree.setlBest(newTree.getPosition());
                    newTree.setlBestValue(newTree.getFitnessValue());
                    this.trees.add(newTree);
                }
                t.updateLambda(totalLabmda, totalDistance);
                distanceFactor *= (1-distanceDecrement);
            }

            Collections.sort(trees, new TreeComparator());
            updateGBest(trees.get(0));

            for(int i = 0; i < totalSeedsCount; i++){
                trees.remove(trees.size()-1);
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
            tree.setlBest(tree.getPosition());
            tree.setlBestValue(tree.getFitnessValue());
            this.trees.add(tree);
        }

        Collections.sort(trees, new TreeComparator());
        updateGBest(trees.get(0));
    }

    private void updateGBest(Tree tree) {
        Double fgbest = this.objectiveFunction.setParameters(gBest.getPositionIndexes()).call();
        Double fpbest = this.objectiveFunction.setParameters(tree.getPosition().getPositionIndexes()).call();
        if (Validator.validateBestValue(fpbest, fgbest, isGlobalMinima)) {
            this.gBest.setVector(tree.getPosition());
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.populationSize];
        for(int i=0; i< this.populationSize; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.trees.get(i).getPosition().getValue(j),2);
            }
        }
        return data;
    }
}