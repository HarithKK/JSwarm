package org.usa.soc.ssa;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.nonGeneral.classical.multimodal.nonseparable.AckleysFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SSA extends Algorithm {

    private int populationSize;

    private List<Squirrel> squirrels;

    private double Pdp, Gc, airDensity, speed, surfaceAreaBody, lossInHeight;

    public SSA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isLocalMinima,
            double Pdp,
            double Gc,
            double airDensity,
            double speed,
            double surfaceAreaBody,
            double lossInHeight
    ) {

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isLocalMinima = isLocalMinima;

        this.Pdp = Pdp;
        this.Gc = Gc;
        this.airDensity = airDensity;
        this.speed = speed;
        this.surfaceAreaBody = surfaceAreaBody;
        this.lossInHeight = lossInHeight;
        this.squirrels = new ArrayList<>(populationSize);
    }

    @Override
    public void runOptimizer(int time) {
        if(!this.isInitialized()){
            throw new RuntimeException("Squirrels Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        int hSquirrel = populationSize -1;
        int aSquirrelLower = populationSize - 4;
        int aSquirrelUpper = populationSize - 2;

        try{
            for(int step = 0; step< getStepsCount(); step++){

                Squirrel squirrelOnHickoryTree = squirrels.get(hSquirrel);
                // case for squirrels from acorn tree to hickory tree
                for(int i=aSquirrelLower; i <= aSquirrelUpper;i++){
                    Squirrel squirrelOnAcornTree = squirrels.get(i);
                    double dg = calculateRandomGlidingDistance();
                    if(Randoms.rand(0,1) > Pdp){
                        squirrelOnAcornTree.setPosition(
                                squirrelOnHickoryTree.getPosition()
                                        .operate(Vector.OPERATOR.SUB,squirrelOnAcornTree.getPosition().getClonedVector())
                                        .operate(Vector.OPERATOR.MULP, Gc)
                                        .operate(Vector.OPERATOR.MULP, dg)
                                        .operate(Vector.OPERATOR.ADD, squirrelOnAcornTree.getPosition().getClonedVector())
                        );
                    }else{
                        squirrelOnAcornTree.setPosition(Randoms.getRandomVector(numberOfDimensions,minBoundary, maxBoundary, 0,1));
                    }
                    squirrelOnAcornTree.setFitnessValue(objectiveFunction.setParameters(squirrelOnAcornTree.getPosition().getPositionIndexes()).call());
                }

                // Other squirrels
                for(int i=0; i<aSquirrelLower;i++){
                    Squirrel squirrel = squirrels.get(i);
                    Squirrel randomSquirrelOnAcornTree = squirrels.get(Randoms.rand(2)+1);
                    double dg = calculateRandomGlidingDistance();
                    // case for squirrels from normal tree to acorn tree
                    if(Randoms.rand(0,1) <= 0.5){
                        if(Randoms.rand(0,1) >= Pdp){
                            if(Randoms.rand(0,1) > Pdp){
                                squirrel.setPosition(
                                        squirrelOnHickoryTree.getPosition()
                                                .operate(Vector.OPERATOR.SUB,randomSquirrelOnAcornTree.getPosition().getClonedVector())
                                                .operate(Vector.OPERATOR.MULP, Gc)
                                                .operate(Vector.OPERATOR.MULP, dg)
                                                .operate(Vector.OPERATOR.ADD, randomSquirrelOnAcornTree.getPosition().getClonedVector())
                                );
                            }else{
                                squirrel.setPosition(Randoms.getRandomVector(numberOfDimensions,minBoundary, maxBoundary, 0,1));
                            }
                        }else{

                        }
                    }else{
                        // case for squirrels from normal tree to hickory tree for food supply
                        if(Randoms.rand(0,1) > Pdp){
                            squirrel.setPosition(
                                    squirrelOnHickoryTree.getPosition()
                                            .operate(Vector.OPERATOR.SUB,squirrelOnHickoryTree.getPosition().getClonedVector())
                                            .operate(Vector.OPERATOR.MULP, Gc)
                                            .operate(Vector.OPERATOR.MULP, dg)
                                            .operate(Vector.OPERATOR.ADD, squirrelOnHickoryTree.getPosition().getClonedVector())
                            );
                        }else{
                            squirrel.setPosition(Randoms.getRandomVector(numberOfDimensions,minBoundary, maxBoundary, 0,1));
                        }
                    }
                    squirrel.setFitnessValue(objectiveFunction.setParameters(squirrel.getPosition().getPositionIndexes()).call());
                }

                double sc = calculateSeasonalConstant();
                double smin = 0.00001 / Mathamatics.pow(365, ((step+1) * 2.5 / stepsCount));

                if(sc < smin){
                    for (Squirrel s: squirrels) {
                        s.setPosition(getLevyVector());
                        s.setFitnessValue(objectiveFunction.setParameters(s.getPosition().getPositionIndexes()).call());
                    }
                }

                Collections.sort(squirrels, new SquirrelComparator(isLocalMinima));

                double fgbest = objectiveFunction.setParameters(gBest.getClonedVector().getPositionIndexes()).call();
                if(Validator.validateBestValue(squirrels.get(hSquirrel).getFitnessValue(), fgbest, isLocalMinima)){
                    gBest.setVector(squirrels.get(hSquirrel).getPosition());
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

    private double calculateSeasonalConstant() {
        double sumSt =0;

        for(int i=1; i<4;i++){
            sumSt += Math.pow((squirrels.get(i).getFitnessValue() - squirrels.get(0).getFitnessValue()),2);
        }
        return Math.sqrt(sumSt);
    }

    private double calculateRandomGlidingDistance() {
        double Cl = Randoms.rand(0.675, 1.5);
        double L = 0.5 * Cl * speed * speed * surfaceAreaBody;
        double D = 0.5 * 0.6 * speed * speed * surfaceAreaBody;
        double tanTheta = D / L;
        return this.lossInHeight / tanTheta;
    }

    @Override
    public void initialize() {

        if(populationSize < 5){
            throw new RuntimeException("Need sufficient squirrels (>5)");
        }

        this.setInitialized(true);

        for(int i=0;i <populationSize; i++){
            Squirrel squirrel = new Squirrel(numberOfDimensions, minBoundary, maxBoundary);
            squirrel.setFitnessValue(objectiveFunction.setParameters(squirrel.getPosition().getPositionIndexes()).call());

            squirrels.add(squirrel);
        }

        Collections.sort(squirrels, new SquirrelComparator(isLocalMinima));
    }

    private Vector getLevyVector() {
        Vector v = new Vector(this.numberOfDimensions);
        double levy = Commons.levyflight(3);
        for(int i=0;i<numberOfDimensions;i++){
            v.setValue(minBoundary[i] + levy*(maxBoundary[i] - minBoundary[i]),i);
        }
        return v;
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.populationSize*2];
        for(int i=0; i< this.populationSize; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.squirrels.get(i).getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
