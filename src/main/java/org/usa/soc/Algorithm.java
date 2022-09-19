package org.usa.soc;

import org.usa.soc.core.Action;
import org.usa.soc.core.Vector;

public abstract class Algorithm implements Cloneable {

    protected boolean isLocalMinima;

    protected double[] minBoundary;
    protected double[] maxBoundary;

    protected Action stepAction;

    protected ObjectiveFunction<Double> objectiveFunction;

    protected int stepsCount;

    private Double bestValue = 1.0;

    private double convergenceValue = Double.MAX_VALUE;

    public Algorithm(
            ObjectiveFunction<Double> objectiveFunction,
            int stepsCount,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            long nanoDuration,
            boolean isLocalMinima
    ) {
        this.isLocalMinima = isLocalMinima;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.nanoDuration = nanoDuration;
    }

    protected Algorithm(){

    }

    protected int numberOfDimensions;

    protected Vector gBest;

    protected long nanoDuration;

    private boolean isInitialized = false;

    public void runOptimizer(){
        this.runOptimizer(0);
    };

    public abstract void runOptimizer(int time);

    public abstract void initialize();

    protected boolean isInitialized(){
        return this.isInitialized;
    }

    protected void setInitialized(boolean v){
        this.isInitialized = v;
    }

    public Vector getGBest() { return this.gBest; }

    public Double getBestDoubleValue() {
        return this.objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();
    }
    public long getNanoDuration() {
        return nanoDuration;
    }

    public boolean isMinima() {
        return this.isLocalMinima;
    }

    public String getBestVariables() {
        return this.getGBest().toString();
    }

    public ObjectiveFunction getFunction() {
        return this.objectiveFunction;
    }

    @Override
    public Algorithm clone() throws CloneNotSupportedException{
        return null;
    }
    public void addStepAction(Action a){
        this.stepAction =a;
    }

    public void stepCompleted(int time){

        // calculate convergence
        calculateConvergenceValue();

        if(time == 0){
            return;
        }
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void calculateConvergenceValue() {
        double xValue = objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        this.convergenceValue = Math.abs(xValue - objectiveFunction.getExpectedBestValue()) /
                Math.pow(Math.abs(getBestValue() - objectiveFunction.getExpectedBestValue()), objectiveFunction.getOrderOfConvergence());
        this.bestValue = xValue;
    }

    public abstract double[][] getDataPoints();

    public int getStepsCount() {
        return stepsCount;
    }

    public double getConvergenceValue() {
        return convergenceValue;
    }

    public double getBestValue() {
        return bestValue;
    }
}
