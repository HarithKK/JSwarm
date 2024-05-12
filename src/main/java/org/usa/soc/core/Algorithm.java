package org.usa.soc.core;

import org.usa.soc.KillOptimizerException;
import org.usa.soc.core.action.StepAction;
import org.usa.soc.core.ds.SeriesData;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.StringFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class Algorithm implements Cloneable {

    private boolean isPaused, isKilled;
    protected boolean isGlobalMinima;

    protected double[] minBoundary;
    protected double[] maxBoundary;

    private List<Double> history = new ArrayList<>();

    protected StepAction stepAction;

    protected ObjectiveFunction<Double> objectiveFunction;

    protected int stepsCount;
    private long currentStep;
    private double bestValue = 0;
    private double convergenceValue = Double.MAX_VALUE;
    private double gradiantDecent = Double.MAX_VALUE;

    private double meanBestValue = 0;

    private int interval = 0;

    public Algorithm(
            ObjectiveFunction<Double> objectiveFunction,
            int stepsCount,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            long nanoDuration,
            boolean isGlobalMinima
    ) {
        this.isGlobalMinima = isGlobalMinima;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.nanoDuration = nanoDuration;
        this.isPaused = false;
        this.isKilled = false;
        this.setInterval(0);
    }

    protected Algorithm(){

    }

    protected int numberOfDimensions;

    protected Vector gBest;

    protected long nanoDuration;

    private boolean isInitialized = false;
    
    public abstract void runOptimizer() throws Exception;

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
        return this.isGlobalMinima;
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
    public void addStepAction(StepAction a){
        this.stepAction =a;
    }

    public void stepCompleted(long step) throws Exception {

        this.currentStep = step;

        double xValue = objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();
        // calculate convergence
        calculateMeanValue(step, xValue);
        calculateConvergenceValue(step, xValue);
        calculateGradiantDecent(step, xValue);

        getHistory().add(xValue);
        this.bestValue = xValue;

        if(this.interval == 0){
            return;
        }
        try {
            Thread.sleep(this.interval);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(this.isKilled){
            this.isKilled=false;
            throw new KillOptimizerException("Optimizer has been forcefully stopped!");
        }

        this.checkPaused();
    }

    private void calculateMeanValue(long step, double xValue) {

        if(getMeanBestValue() == Double.POSITIVE_INFINITY){
            meanBestValue = xValue / (step+1);
        }else{
            meanBestValue = ((getMeanBestValue() * (step)) + xValue) / (step + 1);
        }
    }

    private void calculateGradiantDecent(long step, double xValue) {
        double dg = (xValue) / (step+1);
        if(dg == Double.POSITIVE_INFINITY){
            this.gradiantDecent =0;
        }else{
            this.gradiantDecent = getMeanBestValue() - Randoms.rand(0,1)* dg;
        }

    }

    private void calculateConvergenceValue(long step, double xValue) {
        double dev = Math.pow(Math.abs(objectiveFunction.getExpectedBestValue() - getBestValue()), (1/(step+1)));
        this.convergenceValue = 1 - ((objectiveFunction.getExpectedBestValue() - xValue) / dev);
    }

    public abstract double[][] getDataPoints();

    public int getStepsCount() {
        return stepsCount;
    }

    public double getConvergenceValue() {
        return convergenceValue;
    }

    public double getBestValue() {
        return this.bestValue;
    }

    public String getName(){
        return this.getClass().getSimpleName();
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Test ID: "+ new Date().getTime());
        sb.append('\n');

        sb.append("Name: " + this.getClass().getSimpleName());
        sb.append('\n');

        sb.append("Function: " + this.getFunction().getClass().getSimpleName());
        sb.append('\n');

        sb.append("Best Value: ");
        sb.append(this.getBestDoubleValue());
        sb.append('\n');

        sb.append("Expected Best Value: ");
        sb.append(this.getFunction().getExpectedBestValue());
        sb.append('\n');

        sb.append("Minimum Obtained Value: ");
        sb.append(Arrays.toString(this.getFunction().getMin()));
        sb.append('\n');

        sb.append("Maximum Obtained Value: ");
        sb.append(Arrays.toString(this.getFunction().getMax()));
        sb.append('\n');

        sb.append("Number of Dimensions: ");
        sb.append(this.getFunction().getNumberOfDimensions());
        sb.append('\n');

        sb.append("Execution Time: ");
        sb.append(this.getNanoDuration()/ 1000000);
        sb.append('\n');

        sb.append("Best Position: ");
        sb.append(this.getBestVariables());
        sb.append('\n');

        sb.append("Expected Best Position: ");
        sb.append('\n');
        sb.append(StringFormatter.toString(this.getFunction().getExpectedParameters()));
        sb.append('\n');

        sb.append("Convergence: ");
        sb.append(this.getConvergenceValue());
        sb.append('\n');

        sb.append("Gradiant Decent: ");
        sb.append(this.getGradiantDecent());
        sb.append('\n');

        sb.append("Mean Best Value: ");
        sb.append(this.getMeanBestValue());
        sb.append('\n');

        return sb.toString();
    }

    public List toList(){
        List<String> lst = new ArrayList<>();

        lst.add(String.valueOf(new Date().getTime()));
        lst.add(this.getClass().getSimpleName());
        lst.add(this.getFunction().getClass().getSimpleName());
        lst.add(String.valueOf(this.getBestDoubleValue()));
        lst.add(String.valueOf(this.getFunction().getExpectedBestValue()));
        lst.add(Arrays.toString(this.getFunction().getMin()));
        lst.add(Arrays.toString(this.getFunction().getMax()));
        lst.add(String.valueOf(this.getFunction().getNumberOfDimensions()));
        lst.add(String.valueOf(this.getNanoDuration()/ 1000000));
        lst.add(String.valueOf(this.getBestVariables()));
        lst.add(StringFormatter.toString(this.getFunction().getExpectedParameters()));
        lst.add(String.valueOf(this.getConvergenceValue()));
        lst.add(String.valueOf(this.getGradiantDecent()));
        lst.add(String.valueOf(this.getMeanBestValue()));

        return lst;
    }

    public double getGradiantDecent() {
        return gradiantDecent;
    }

    public double getMeanBestValue() {
        return meanBestValue;
    }

    public void pauseOptimizer() {
        this.isPaused = true;
    }

    public void resumeOptimizer() {
        this.isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    protected void checkPaused() {
        if(this.isPaused()){
        while(this.isPaused()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    }

    public void stopOptimizer() {
        this.isKilled=true;
    }

    public long getCurrentStep() {
        return currentStep;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public List<Double> getHistory() {
        return history;
    }

    public List<SeriesData> getSeriesData() {
        return null;
    }
}
