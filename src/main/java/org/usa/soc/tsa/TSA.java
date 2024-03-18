package org.usa.soc.tsa;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.avoa.Vulture;
import org.usa.soc.core.Vector;
import org.usa.soc.tco.Termite;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class TSA extends Algorithm {
    private int populationSize;

    private Tunicate[] tunicates;

    private Vector A, G, F, M;

    private double pmin, pmax;

    public TSA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima
    ){

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isGlobalMinima = isGlobalMinima;

        this.tunicates = new Tunicate[populationSize];
        this.pmax = 4;
        this.pmin = 1;
        this.A = new Vector(numberOfDimensions);
        this.G = new Vector(numberOfDimensions);
        this.F = new Vector(numberOfDimensions);
        this.M = new Vector(numberOfDimensions);
    }

    @Override
    public void runOptimizer() throws Exception{
        if(!this.isInitialized()){
            throw new RuntimeException("Tunicates Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        for(int step = 0; step< getStepsCount(); step++){

            for (Tunicate tunicate: tunicates) {
                calculateConstVectors();
                Vector VPD = this.getGBest().getClonedVector()
                        .operate(Vector.OPERATOR.SUB, tunicate.getPosition().operate(Vector.OPERATOR.MULP, Randoms.rand(0,1)));
                Double PD = VPD.getMagnitude();
                Vector newP;
                if(Randoms.rand(0,1) <= 0.5){
                    newP = tunicate.getPosition().operate(Vector.OPERATOR.ADD, this.A.operate(Vector.OPERATOR.MULP, PD));
                }else{
                    newP = tunicate.getPosition().operate(Vector.OPERATOR.SUB, this.A.operate(Vector.OPERATOR.MULP, PD));
                }

                newP = tunicate.getPosition().operate(Vector.OPERATOR.ADD, newP.getClonedVector())
                        .operate(Vector.OPERATOR.DIV, ( 2 + Randoms.rand(0,1)));
                tunicate.setPosition(newP.fixVector(minBoundary, maxBoundary));
                tunicate.setFitnessValue(objectiveFunction.setParameters(tunicate.getPosition().getPositionIndexes()).call());
            }

            for (Tunicate tunicate: tunicates){
                updateGBest(tunicate);
            }

            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private void calculateConstVectors() {

        this.F.setVector(Randoms.getRandomVector(numberOfDimensions, 0, 1)
                .operate(Vector.OPERATOR.MULP, 2.0));
        this.G.setVector(Randoms.getRandomVector(numberOfDimensions, 0, 1)
                .operate(Vector.OPERATOR.ADD, Randoms.getRandomVector(numberOfDimensions, 0, 1))
                .operate(Vector.OPERATOR.SUB, this.F.getClonedVector()));
        this.M.setVector(Randoms.getRandomVector(numberOfDimensions, 0, 1)
                .operate(Vector.OPERATOR.MULP, (this.pmax - this.pmin))
                .operate(Vector.OPERATOR.ADD, pmin));
        this.A.setVector(this.G.getClonedVector()
                .operate(Vector.OPERATOR.DIV, this.M.getClonedVector()));

    }


    @Override
    public void initialize() {
        setInitialized(true);

        for(int i=0; i<populationSize; i++){
            Tunicate tunicate = new Tunicate(numberOfDimensions, minBoundary, maxBoundary);
            tunicate.setFitnessValue(objectiveFunction.setParameters(tunicate.getPosition().getPositionIndexes()).call());
            tunicates[i] = tunicate;
            updateGBest(tunicate);
        }

    }

    private void updateGBest(Tunicate tunicate) {
        Double fgbest = this.objectiveFunction.setParameters(gBest.getPositionIndexes()).call();
        Double fpbest = this.objectiveFunction.setParameters(tunicate.getPosition().getPositionIndexes()).call();
        if (Validator.validateBestValue(fpbest, fgbest, isGlobalMinima)) {
            this.gBest.setVector(tunicate.getPosition());
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.populationSize];
        for(int i=0; i< this.populationSize; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.tunicates[i].getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
