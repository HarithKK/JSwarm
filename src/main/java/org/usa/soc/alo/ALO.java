package org.usa.soc.alo;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.Arrays;

public class ALO extends Algorithm {

    private int numberOfAnts;

    private Ant ants[], antLions[];
    private double minFs, maxFs;

    public ALO(
            ObjectiveFunction objectiveFunction,
            int numberOfAnts,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima
    ) {
        this.objectiveFunction = objectiveFunction;
        this.numberOfAnts = numberOfAnts;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.isGlobalMinima = isGlobalMinima;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.ants = new Ant[numberOfAnts];
        this.antLions = new Ant[numberOfAnts];

        this.minFs = Double.MIN_VALUE;
        this.maxFs = Double.MAX_VALUE;
    }

    @Override
    public void runOptimizer() throws Exception{
        if (!this.isInitialized()) {
            throw new RuntimeException("Ants Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();

        for (int step = 0; step < stepsCount; step++) {
            double I = calculateI(step, stepsCount);
            for (Ant a: ants) {
                int selectedAntLionIndex = getAntLionIndexFromRouletteWheel();

                Vector minValuesVector = new Vector(numberOfDimensions).setMaxVector();
                Vector maxValuesVector = new Vector(numberOfDimensions).setMinVector();

                for (Ant a1: ants){
                    for(int i=0; i< a1.getPosition().getNumberOfDimensions();i++){
                        double v = a1.getPosition().getValue(i);
                        minValuesVector.setValue(Math.min(v, minValuesVector.getValue(i)), i);
                        maxValuesVector.setValue(Math.max(v, maxValuesVector.getValue(i)), i);
                    }
                }

                Vector RA = getNewPosition(minValuesVector, maxValuesVector, antLions[selectedAntLionIndex].getPosition(), I, step);
                Vector RE = getNewPosition(minValuesVector, maxValuesVector, gBest, I, step);
                Vector R = RA.operate(Vector.OPERATOR.ADD, RE).operate(Vector.OPERATOR.DIV, 2.0).fixVector(minBoundary, maxBoundary);

                a.setPosition(R);
                a.setFitnessValue(objectiveFunction.setParameters(a.getPosition().getPositionIndexes()).call());

            }

            for(int i=0; i<numberOfAnts; i++){
                if(Validator.validateBestValue(ants[i].getFitnessValue(), antLions[i].getFitnessValue(), !isGlobalMinima)){
                    antLions[i] = ants[i].cloneAnt();
                }
            }

            Ant elite = antLions[0];
            for(int i=0; i<numberOfAnts; i++) {
                Ant a = antLions[i];
                if(Validator.validateBestValue(a.getFitnessValue(), elite.getFitnessValue(), isGlobalMinima)){
                    elite = a.cloneAnt();
                }else{
                    antLions[i] = elite.cloneAnt();
                }
            }

            if(Validator.validateBestValue(
                    objectiveFunction.setParameters(elite.getPosition().getPositionIndexes()).call(),
                    objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call(),
                    isGlobalMinima
            )){
                this.gBest.setVector(elite.getPosition());
            }

            if (this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private Vector getNewPosition(Vector minValuesVector, Vector maxValuesVector, Vector currentVector, Double I, int step){
        double C = Randoms.rand(0,1) < 0.5 ? 1.0 : -1.0;
        Vector CI = minValuesVector.operate(Vector.OPERATOR.DIV, I)
                .operate(Vector.OPERATOR.MULP, C)
                .operate(Vector.OPERATOR.ADD, currentVector);

        double D = Randoms.rand(0,1) >= 0.5 ? 1.0 : -1.0;
        Vector DI = maxValuesVector.operate(Vector.OPERATOR.DIV, I)
                .operate(Vector.OPERATOR.MULP, D)
                .operate(Vector.OPERATOR.ADD, currentVector);


        double []randomWalk = getRandomWalk(step);
        double rw = randomWalk[randomWalk.length -1];
        Arrays.sort(randomWalk);

        Vector newPos = new Vector(numberOfDimensions);

        for(int i=0; i< numberOfDimensions;i++){
            double val =  (rw - randomWalk[0]) * (DI.getValue(i) - CI.getValue(i));
            val /= ((randomWalk[randomWalk.length -1] - randomWalk[0])+1);
            val += CI.getValue(i);
            newPos.setValue(val, i);
        }

        return newPos;
    }

    private double[] getRandomWalk(int step) {
        double randomWalk[] = new double[step + 1];
        randomWalk[0] = 0;

        for(int i=1;i<=step;i++){
            randomWalk[i] = randomWalk[i-1] + 2*((Randoms.rand(0,1) < 0.5 ? 1 : 0) -1);
        }
        return randomWalk;
    }

    private double calculateI(int step, int stepsCount) {
        double w;
        if((double)step > 0.1*stepsCount){
            w = 2;
        }else if((double)step > 0.5*stepsCount){
            w = 3;
        }else if((double)step > 0.75*stepsCount){
            w = 4;
        }else if((double)step > 0.9*stepsCount){
            w = 5;
        }else if((double)step > 0.95*stepsCount){
            w =6;
        }else{
            w = 1;
        }
        return Math.pow(10, w) * (((double)step + 1) / (double)stepsCount);
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        for (int i = 0; i < this.numberOfAnts; i++) {
            Ant ant = new Ant(numberOfDimensions, minBoundary, maxBoundary);
            ant.setFitnessValue(objectiveFunction.setParameters(ant.getPosition().getPositionIndexes()).call());
            ants[i] = ant;
        }

        Ant elite = new Ant(numberOfDimensions, minBoundary, maxBoundary);
        for (int i = 0; i < this.numberOfAnts; i++) {
            Ant antLion = new Ant(numberOfDimensions, minBoundary, maxBoundary);
            antLion.setFitnessValue(objectiveFunction.setParameters(antLion.getPosition().getPositionIndexes()).call());
            antLions[i] = antLion;
            this.maxFs = Math.max(this.maxFs, antLion.getFitnessValue());
            this.minFs = Math.min(this.minFs, antLion.getFitnessValue());

            if(Validator.validateBestValue(antLion.getFitnessValue(), elite.getFitnessValue(), isGlobalMinima)){
                elite = antLion;
            }
        }
        this.gBest.setVector(elite.getPosition());
    }

    private int getAntLionIndexFromRouletteWheel(){
        double deltaFs = maxFs - minFs;
        double fsb = isGlobalMinima ? maxFs : minFs;
        double p0 = Randoms.rand(0,1);

        for(int i=0; i<numberOfAnts; i++){
            double p = Math.abs(antLions[i].getFitnessValue() - fsb) / deltaFs;
            if(p > p0){
                return i;
            }
        }
        return 0;
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.numberOfAnts*2];
        for(int i=0; i< this.numberOfAnts; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.ants[i].getPosition().getValue(j),2);
            }
            for(int j =0; j< numberOfDimensions; j++){
                data[j][i+numberOfAnts] = Mathamatics.round(this.antLions[i].getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
