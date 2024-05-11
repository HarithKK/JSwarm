package org.usa.soc.si.zoa;

import org.usa.soc.core.Algorithm;
import org.usa.soc.core.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class ZOA extends Algorithm {

    private int populationSize;

    private Zebra[] zebras;
    private Zebra pioneerZebra, attackedZebra;

    public ZOA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima
    ) {

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.isGlobalMinima = isGlobalMinima;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.zebras = new Zebra[populationSize];
        this.pioneerZebra = new Zebra(numberOfDimensions, minBoundary, maxBoundary);
    }

    @Override
    public void runOptimizer() throws Exception{
        if(!this.isInitialized()){
            throw new RuntimeException("Squirrels Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        try{
            for(int step = 0; step< getStepsCount(); step++){

                for(Zebra zebra: zebras){
                    // phase 1
                    long I = Math.round(1 + Randoms.rand(0,1));
                    Vector newX = pioneerZebra.getPosition()
                            .operate(Vector.OPERATOR.SUB, zebra.getPosition().operate(Vector.OPERATOR.MULP, (double)I))
                            .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1))
                            .operate(Vector.OPERATOR.ADD, zebra.getPosition());
                    zebra.updatePosition(objectiveFunction, newX, isGlobalMinima);

                    // phase 2
                    if(Randoms.rand(0,1) <= 0.5){
                        double c = 0.01*(2*Randoms.rand(0,1) -1)*(1 - (step+1)/stepsCount);
                        Vector S1 = zebra.getPosition().operate(Vector.OPERATOR.MULP, (1+c));
                        zebra.updatePosition(objectiveFunction, S1, isGlobalMinima);
                        attackedZebra = zebra;
                    }else{
                        Vector S2 = attackedZebra.getPosition()
                                .operate(Vector.OPERATOR.SUB, zebra.getPosition().operate(Vector.OPERATOR.MULP, (double)I))
                                .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1))
                                .operate(Vector.OPERATOR.ADD, zebra.getPosition());
                        zebra.updatePosition(objectiveFunction, S2, isGlobalMinima);
                    }

                }

                for(Zebra zebra: zebras){
                    updateGbest(zebra);
                }
                if(this.stepAction != null)
                    this.stepAction.performAction(this.gBest.getClonedVector(), this.getBestDoubleValue(), step);
                stepCompleted(step);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    @Override
    public void initialize() {
        this.setInitialized(true);

        for(int i=0; i<populationSize; i++){
            Zebra zebra = new Zebra(numberOfDimensions, minBoundary, maxBoundary);
            zebra.setFitnessValue(objectiveFunction.setParameters(zebra.getPosition().getPositionIndexes()).call());
            zebras[i] = zebra;
        }

        for(Zebra zebra: zebras){
            updateGbest(zebra);
        }

        attackedZebra = zebras[Randoms.rand(populationSize-1)];
    }

    private void updateGbest(Zebra zebra) {
        double fgbest = objectiveFunction.setParameters(gBest.getClonedVector().getPositionIndexes()).call();
        if(Validator.validateBestValue(zebra.getFitnessValue(), fgbest, isGlobalMinima)){
            gBest.setVector(zebra.getPosition());
            pioneerZebra = zebra;
        }
    }


    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.populationSize*2];
        for(int i=0; i< this.populationSize; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.zebras[i].getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
