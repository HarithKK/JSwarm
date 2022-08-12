package org.usa.soc.wso;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class WSO extends Algorithm {
    private Wasp[] wasps;
    private int numberOfWasps;

    private double c1, c2;

    private double totalForce;

    public WSO(ObjectiveFunction fn,
               int numberOfIterations,
               int numberOfWasps,
               int numberOfDimensions,
               double[] minBoundary,
               double[] maxBoundary,
               double c1,
               double c2,
               boolean isLocalMinima) {
        this.objectiveFunction = fn;
        this.stepsCount = numberOfIterations;
        this.numberOfWasps = numberOfWasps;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isLocalMinima = isLocalMinima;
        this.c1 = c1;
        this.c2 = c2;

        this.wasps = new Wasp[numberOfWasps];
        this.gBest = isLocalMinima? new Vector(this.numberOfDimensions).setMaxVector() : new Vector(this.numberOfDimensions).setMinVector();
    }

    @Override
    public void runOptimizer(int time) {
        if(!this.isInitialized()){
            throw new RuntimeException("Wasps Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();

        for(int step = 0; step < this.stepsCount; step++){
            Wasp w0 = createNewRandomWasp();
            double p0 = w0.getForce() / totalForce;

            for(Wasp w: this.wasps){
                double p = w.getForce() / totalForce;

                if(p < p0){
                    w.setSolution(w0.getBestSolution());
                    w.updateDiversity(objectiveFunction, isLocalMinima);
                    w.updateForce(this.c1, this.c2, this.objectiveFunction);
                    this.updateBest(w);
                }
            }
            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue());
            sleep(time);
        }

        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    @Override
    public long getNanoDuration() {
        return this.nanoDuration;
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        for(int i=0; i< this.numberOfWasps;i++){
            Wasp wasp = createNewRandomWasp();
            this.totalForce += wasp.getForce();

            this.wasps[i] = wasp;
        }
    }

    private Wasp createNewRandomWasp(){
        Wasp wasp = new Wasp(
                this.minBoundary,
                this.maxBoundary,
                this.numberOfDimensions);
        wasp.setSolution(Randoms.getRandomVector(this.numberOfDimensions, this.minBoundary, this.maxBoundary));
        wasp.updateDiversity(objectiveFunction, isLocalMinima);
        wasp.updateForce(this.c1, this.c2, this.objectiveFunction);
        this.updateBest(wasp);
        return wasp;
    }

    private void updateBest(Wasp w) {
        Double fgbest = objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();
        Double fpbest = objectiveFunction.setParameters(w.getBestSolution().getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.gBest = w.getBestSolution().getClonedVector();
        }
    }

    @Override
    public Algorithm clone() throws CloneNotSupportedException {
        return new WSO(objectiveFunction,
                stepsCount,
                numberOfWasps,
                numberOfDimensions,
                minBoundary,
                maxBoundary,
                c1,
                c2,
                isLocalMinima);
    }

    @Override
    public double[][] getDataPoints(){
        double[][] data = new double[this.numberOfDimensions][this.numberOfWasps];
        for(int i=0; i< this.numberOfWasps; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.wasps[i].getBestSolution().getValue(j),2);
            }
        }
        return data;
    };
}
