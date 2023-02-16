package org.usa.soc.tno;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.tco.Termite;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TNO extends Algorithm {

    private List<Tree> trees;
    private Vector bestAngle;

    private int numberOfTrees;
    private double seeAngle, c1, c2;

    public TNO(ObjectiveFunction<Double> objectiveFunction,
               int stepsCount,
               int numberOfDimensions,
               int numberOfTrees,
               double[] minBoundary,
               double[] maxBoundary,
               double seeAngle,
               double c1,
               double c2,
               boolean isLocalMinima){

        super.numberOfDimensions = numberOfDimensions;
        super.stepsCount = stepsCount;
        super.objectiveFunction = objectiveFunction;
        super.minBoundary = minBoundary;
        super.maxBoundary = maxBoundary;
        super.isLocalMinima = isLocalMinima;
        this.numberOfTrees = numberOfTrees;
        this.seeAngle = seeAngle;
        this.c1 = c1;
        this.c2 = c2;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);

        this.trees = new ArrayList<>(numberOfTrees);

    }

    @Override
    public void runOptimizer(int time) {
        if(!this.isInitialized()){
            throw new RuntimeException("Trees Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        for(int step = 0; step< getStepsCount(); step++){
            Vector oldGbest = this.gBest.getClonedVector();
            for (Tree tree: trees) {
                if(Randoms.rand(0,1)<c2){
                    Vector v1 = this.bestAngle.getClonedVector().
                            operate(Vector.OPERATOR.MULP,Randoms.rand(0, 1)).
                            operate(Vector.OPERATOR.MULP,c1);
                    Vector v2 = getSupportedRandomVector(v1);
                    // set as new Tree
                    Vector v3 = oldGbest.getClonedVector().operate(Vector.OPERATOR.ADD, v2);
                    tree.setVector(tree.
                            getPosition().
                            operate(Vector.OPERATOR.MULP, Randoms.rand(0,1)).
                            operate(Vector.OPERATOR.ADD, v3));
                }else{
                    tree.setVector(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary));
                }
                updateGbestValue(tree);
            }

            bestAngle.setVector(this.getGBest().operate(Vector.OPERATOR.SUB, oldGbest));

            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(time, step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private Vector getSupportedRandomVector(Vector v) {
        Vector returnVector = new Vector(numberOfDimensions);
        for(int i =0; i< v.getNumberOfDimensions(); i++){
            double value = Randoms.rand(-Math.abs(v.getValue(i)), Math.abs(v.getValue(i)));
            returnVector.setValue(value, i);
        }

        return returnVector;

    }

    @Override
    public void initialize() {

        this.setInitialized(true);
        Vector oldGbest = this.gBest.getClonedVector();

        for(int i=0; i< numberOfTrees; i++){
            this.trees.add(new Tree(
                    this.minBoundary,
                    this.maxBoundary,
                    this.numberOfDimensions
            ));
        }

        for (Tree tree: this.trees) {
            updateGbestValue(tree);
        }

        bestAngle = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        bestAngle.setVector(this.getGBest().operate(Vector.OPERATOR.SUB, oldGbest));
    }

    private void updateGbestValue(Tree tree) {

        Double fgbest = this.objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();
        Double fpbest =this.objectiveFunction.setParameters(tree.getPosition().getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.gBest.setVector(tree.getPosition());
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.numberOfTrees];
        for(int i=0; i< this.numberOfTrees; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.trees.get(i).getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
