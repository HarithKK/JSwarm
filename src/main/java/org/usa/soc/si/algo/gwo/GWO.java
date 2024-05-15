package org.usa.soc.si.algo.gwo;

import org.usa.soc.si.Algorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GWO extends Algorithm {

    private static final Double MAX_A = 2.0;
    private int numberOfWolfs;
    private Wolf alpha, beta, delta;
    private List<Wolf> wolfs;

    private Vector a;

    public GWO (ObjectiveFunction<Double> objectiveFunction,
                int stepsCount,
                int numberOfDimensions,
                int numberOfWolfs,
                double[] minBoundary,
                double[] maxBoundary,
                boolean isGlobalMinima){

        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima = isGlobalMinima;
        this.numberOfWolfs = numberOfWolfs;

        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.wolfs = new ArrayList<>(numberOfWolfs);

        this.a = new Vector(numberOfDimensions);
        this.a.resetAllValues(MAX_A);
    }
    @Override
    public void runOptimizer() throws Exception{
        if(!this.isInitialized()){
            throw new RuntimeException("Wolfs Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();
        for(int step = 0; step< stepsCount; step++){
            double aDecrement = 2*(1.0 - (step/ stepsCount));
            for(Wolf w: wolfs){
                Vector X1 = getUpdatedPositionVector(w, alpha, calcA(), calcC());
                Vector X2 = getUpdatedPositionVector(w, beta,  calcA(), calcC());
                Vector X3 = getUpdatedPositionVector(w, delta,  calcA(), calcC());

                Vector newX = X1
                        .operate(Vector.OPERATOR.ADD, X2)
                        .operate(Vector.OPERATOR.ADD, X3)
                        .operate(Vector.OPERATOR.DIV, 3.0)
                        .fixVector(minBoundary, maxBoundary);
                Double fitnessValue = objectiveFunction.setParameters(newX.getPositionIndexes()).call();
                if(Validator.validateBestValue(fitnessValue, w.getFitnessValue(), isGlobalMinima)){
                    w.setPosition(newX);
                    w.setFitnessValue(fitnessValue);
                }
            }

            updateWolfHirarchy();

            this.a.resetAllValues(aDecrement);

            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private Vector getUpdatedPositionVector(Wolf w, Wolf prey, Vector C, Vector A){
        Vector D = prey.getPosition().operate(Vector.OPERATOR.MULP, C).operate(Vector.OPERATOR.SUB, w.getPosition());
        Vector AD = A.operate(Vector.OPERATOR.MULP, D);
        return w.getPosition().operate(Vector.OPERATOR.SUB, AD).fixVector(minBoundary, maxBoundary);
    }

    private Vector calcA(){
        return Randoms.getRandomVector(numberOfDimensions,0,1)
                .operate(Vector.OPERATOR.MULP, 2.0)
                .operate(Vector.OPERATOR.SUB, 1.0)
                .operate(Vector.OPERATOR.MULP, a);
    }

    private Vector calcC(){
        return Randoms.getRandomVector(numberOfDimensions,0,1)
                .operate(Vector.OPERATOR.MULP, 2.0);
    }

    @Override
    public void initialize() {

        if(numberOfWolfs < 3){
            throw new RuntimeException("Wolfs count should be greater than 3");
        }

        setInitialized(true);

        for(int i =0;i< numberOfWolfs; i++){
            Wolf w = new Wolf(numberOfDimensions, minBoundary, maxBoundary);
            w.setFitnessValue(this.objectiveFunction.setParameters(w.getPosition().getPositionIndexes()).call());
            wolfs.add(w);
        }

        Collections.sort(wolfs, new WolfComparator());

        this.alpha = wolfs.get(0).getClonedWolf();
        this.beta = wolfs.get(1).getClonedWolf();
        this.delta = wolfs.get(2).getClonedWolf();

        updateWolfHirarchy();
    }

    private void updateWolfHirarchy(){

//        findAlpha();
//        findBeta();
//        findDelta();

        Collections.sort(wolfs, new WolfComparator());

        this.alpha = wolfs.get(0).getClonedWolf();
        this.beta = wolfs.get(1).getClonedWolf();
        this.delta = wolfs.get(2).getClonedWolf();

        this.gBest.setVector(this.alpha.getPosition());
    }

    private void findAlpha(){
        for(int j= 0;j < this.numberOfWolfs; j++){
            if(Validator.validateBestValue(wolfs.get(j).getFitnessValue(), alpha.getFitnessValue(), isGlobalMinima)){
                this.alpha = wolfs.get(j).getClonedWolf();
            }
        }
    }

    private void findBeta(){
        for(int j= 0;j < this.numberOfWolfs; j++){
            double falpha = alpha.getFitnessValue();
            double fbeta = beta.getFitnessValue();
            double f = wolfs.get(j).getFitnessValue();
            if(Validator.validateBestValue(f, fbeta, isGlobalMinima) &&
                    Validator.validateBestValue(falpha, fbeta, isGlobalMinima)
            ){
                this.beta = wolfs.get(j).getClonedWolf();
            }
        }
    }

    private void findDelta(){
        for(int j= 0;j < this.numberOfWolfs; j++){
            double fbeta = beta.getFitnessValue();
            double fdelta = delta.getFitnessValue();
            double f = wolfs.get(j).getFitnessValue();
            if(Validator.validateBestValue(f, fdelta, isGlobalMinima) &&
                    Validator.validateBestValue(fbeta, fdelta, isGlobalMinima)
            ){
                this.delta = wolfs.get(j).getClonedWolf();
            }
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.numberOfWolfs];
        for(int i=0; i< this.numberOfWolfs; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.wolfs.get(i).getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
