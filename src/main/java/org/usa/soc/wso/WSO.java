package org.usa.soc.wso;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.intefaces.IAlgorithm;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class WSO implements IAlgorithm {

    private ObjectiveFunction fn;
    private Wasp[] wasps;
    private int numberOfIterations;
    private int numberOfWasps;
    private int numberOfDimensions;

    private double[] minBoundary, maxBoundary;

    private boolean isInitialized = false;

    private Vector gBest;

    private boolean isLocalMinima;

    private long nanoDuration;

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
        this.fn = fn;
        this.numberOfIterations = numberOfIterations;
        this.numberOfWasps = numberOfWasps;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isLocalMinima = isLocalMinima;
        this.c1 = c1;
        this.c2 = c2;

        this.wasps = new Wasp[numberOfWasps];
        this.gBest = new Vector(this.numberOfDimensions);
    }

    @Override
    public void runOptimizer() {
        if(!this.isInitialized){
            throw new RuntimeException("Wasps Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();

        for(int step = 0; step < this.numberOfIterations; step++){
            Wasp w0 = createNewRandomWasp();
            double p0 = w0.getForce() / totalForce;

            for(Wasp w: this.wasps){
                double p = w.getForce() / totalForce;

                if(p > p0){
                    w.setSolution(w0.getBestSolution());
                    w.updateDiversity(fn, isLocalMinima);
                    w.updateForce(this.c1, this.c2, this.fn);
                    this.updateBest(w);
                }
            }
        }

        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    @Override
    public long getNanoDuration() {
        return this.nanoDuration;
    }

    @Override
    public void initialize() {
        this.isInitialized = true;
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
        wasp.updateDiversity(fn, isLocalMinima);
        wasp.updateForce(this.c1, this.c2, this.fn);
        this.updateBest(wasp);
        return wasp;
    }

    private void updateBest(Wasp w) {
        Double fgbest = fn.setParameters(this.gBest.getPositionIndexes()).call();
        Double fpbest = fn.setParameters(w.getBestSolution().getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.gBest = w.getBestSolution().getClonedVector();
        }
    }

    @Override
    public void setBoundaries(double[] minBoundary, double[] maxBoundary) {
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
    }

    @Override
    public String getBestValue() {
        return String.valueOf(getBestDValue());
    }

    @Override
    public Double getBestDValue() {
        return fn.setParameters(this.gBest.getPositionIndexes()).call();
    }

    @Override
    public ObjectiveFunction getFunction() {
        return this.fn;
    }

    @Override
    public String getBestVariables() {
        return this.gBest.toString();
    }

    @Override
    public String getErrorPercentage() {
        return Mathamatics.getErrorPercentage( Math.abs(this.getBestDValue() - this.fn.getExpectedBestValue()));
    }

    public Vector getGBest(){
        return this.gBest;
    }
}
