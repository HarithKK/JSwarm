package org.usa.soc.choa;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.jso.Jellyfish;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CHOA extends Algorithm {

    private int populationSize;

    private List<Chimp> chimps;

    Chimp attacker, chaser, barrier, divider;

    double fUpper, f;
    private Chaotics.type chaoticType;


    public CHOA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isLocalMinima,
            double fUpper,
            Chaotics.type type
    ) {

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isLocalMinima = isLocalMinima;
        this.fUpper = fUpper;
        this.chaoticType = type;

        this.chimps = new ArrayList<>(populationSize);
    }

    @Override
    public void runOptimizer(int time) throws Exception{
        if(!this.isInitialized()){
            throw new RuntimeException("Chimps Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        try{
            for(int step = 0; step< getStepsCount(); step++){

                Collections.sort(chimps, new ChimpComparator(isLocalMinima));
                f = fUpper*(1 - ((step+1)/ stepsCount));
                attacker = chimps.get(populationSize-1);
                attacker.updateFMAC(f, chaoticType);
                chaser = chimps.get(populationSize-2);
                chaser.updateFMAC(f, chaoticType);
                barrier = chimps.get(populationSize-3);
                barrier.updateFMAC(f, chaoticType);
                divider = chimps.get(populationSize-4);
                divider.updateFMAC(f, chaoticType);

                for(Chimp chimp: chimps){
                    chimp.updateFMAC(f, chaoticType);
                    chimp.updateDValues(attacker, chaser, barrier, divider);
                }

                for(Chimp chimp: chimps){
                    Vector newX;
                    double u = Randoms.rand(0,1);
                    if(u < 0.5){
                        if(Math.abs(chimp.getA()) < 1){
                            newX = chimp.getPositionFromPrey(this.gBest, f);
                        }else{
                            newX = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
                        }
                    }else if(u > 0.5){
                        Vector x1 = attacker.getPosition().operate(Vector.OPERATOR.SUB, chimp.getDa());
                        Vector x2 = attacker.getPosition().operate(Vector.OPERATOR.SUB, chimp.getDb());
                        Vector x3 = attacker.getPosition().operate(Vector.OPERATOR.SUB, chimp.getDc());
                        Vector x4 = attacker.getPosition().operate(Vector.OPERATOR.SUB, chimp.getDd());

                        newX = x1.operate(Vector.OPERATOR.ADD, x2)
                                .operate(Vector.OPERATOR.ADD, x3)
                                .operate(Vector.OPERATOR.ADD, x4)
                                .operate(Vector.OPERATOR.DIV, 4.0);
                    }else{
                        newX = chimp.getPosition();
                    }

                    chimp.setPosition(newX.fixVector(minBoundary, maxBoundary));
                    chimp.setFitnessValue(objectiveFunction.setParameters(chimp.getPosition().getPositionIndexes()).call());
                }

                for(Chimp chimp: chimps){
                    updateGBest(chimp);
                }

                if(this.stepAction != null)
                    this.stepAction.performAction(this.gBest.getClonedVector(), this.getBestDoubleValue(), step);
                stepCompleted(time, step);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    @Override
    public void initialize() {

        this.setInitialized(true);
        Validator.checkPopulationSize(populationSize, 4);

        for(int i=0;i <populationSize; i++){
            Chimp chimp = new Chimp(numberOfDimensions, minBoundary, maxBoundary);
            chimp.setFitnessValue(objectiveFunction.setParameters(chimp.getPosition().getPositionIndexes()).call());
            chimps.add(chimp);

            updateGBest(chimp);
        }

    }

    private void updateGBest(Chimp chimp) {
        double fgbest = objectiveFunction.setParameters(this.gBest.getClonedVector().getPositionIndexes()).call();
        if(Validator.validateBestValue(chimp.getFitnessValue(), fgbest, isLocalMinima)){
            this.gBest.setVector(chimp.getPosition());
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.populationSize*2];
        for(int i=0; i< this.populationSize; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.chimps.get(i).getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
