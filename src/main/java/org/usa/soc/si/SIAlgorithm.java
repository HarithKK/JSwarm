package org.usa.soc.si;

import org.usa.soc.core.Flag;
import org.usa.soc.core.exceptions.KillOptimizerException;
import org.usa.soc.core.action.StepAction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.StringFormatter;

import java.util.*;

public abstract class SIAlgorithm extends Algorithm implements Cloneable {

    public Flag isGlobalMinima = new Flag();
    protected double[] minBoundary, maxBoundary;
    protected int stepsCount, numberOfDimensions;
    protected StepAction stepAction;
    protected ObjectiveFunction<Double> objectiveFunction;
    protected Vector gBest;
    private List<Double> history = new ArrayList<>();
    private double bestValue = 0,
            convergenceValue = Double.MAX_VALUE,
            gradiantDecent = Double.MAX_VALUE,
            meanBestValue = 0;

    public SIAlgorithm(
            ObjectiveFunction<Double> objectiveFunction,
            int stepsCount,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            long nanoDuration,
            boolean isGlobalMinima
    ) {
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.nanoDuration = nanoDuration;
        this.isPaused.unset();
        this.isKilled.unset();
        this.setInterval(0);
    }

    protected SIAlgorithm() {
    }

    @Override
    public SIAlgorithm clone() throws CloneNotSupportedException {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Test ID: " + new Date().getTime());
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
        sb.append(this.getNanoDuration() / 1000000);
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

    public List toList() {
        List<String> lst = new ArrayList<>();

        lst.add(String.valueOf(new Date().getTime()));
        lst.add(this.getClass().getSimpleName());
        lst.add(this.getFunction().getClass().getSimpleName());
        lst.add(String.valueOf(this.getBestDoubleValue()));
        lst.add(String.valueOf(this.getFunction().getExpectedBestValue()));
        lst.add(Arrays.toString(this.getFunction().getMin()));
        lst.add(Arrays.toString(this.getFunction().getMax()));
        lst.add(String.valueOf(this.getFunction().getNumberOfDimensions()));
        lst.add(String.valueOf(this.getNanoDuration() / 1000000));
        lst.add(String.valueOf(this.getBestVariables()));
        lst.add(StringFormatter.toString(this.getFunction().getExpectedParameters()));
        lst.add(String.valueOf(this.getConvergenceValue()));
        lst.add(String.valueOf(this.getGradiantDecent()));
        lst.add(String.valueOf(this.getMeanBestValue()));

        return lst;
    }

    public void addStepAction(StepAction a) {
        this.stepAction = a;
    }

    @Override
    public void stepCompleted(long step) throws InterruptedException, KillOptimizerException {

        this.currentStep = step;
        this.bestValue = objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        calculateMeanValue(step, this.bestValue);
        calculateConvergenceValue(step, this.bestValue);
        calculateGradiantDecent(step, this.bestValue);
        getHistory().add(this.bestValue);

        if (this.interval == 0) {
            return;
        }

        try {
            Thread.sleep(this.interval);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (this.isKilled.isSet()) {
            this.isKilled.unset();
            throw new KillOptimizerException("Optimizer has been forcefully stopped!");
        }

        this.checkPaused();
    }

    /**
     * Util Functions
     */
    private void calculateMeanValue(long step, double xValue) {

        if (getMeanBestValue() == Double.POSITIVE_INFINITY) {
            meanBestValue = xValue / (step + 1);
        } else {
            meanBestValue = ((getMeanBestValue() * (step)) + xValue) / (step + 1);
        }
    }

    private void calculateGradiantDecent(long step, double xValue) {
        double dg = (xValue) / (step + 1);
        if (dg == Double.POSITIVE_INFINITY) {
            this.gradiantDecent = 0;
        } else {
            this.gradiantDecent = getMeanBestValue() - Randoms.rand(0, 1) * dg;
        }

    }

    private void calculateConvergenceValue(long step, double xValue) {
        double dev = Math.pow(Math.abs(objectiveFunction.getExpectedBestValue() - getBestValue()), (1 / (step + 1)));
        this.convergenceValue = 1 - ((objectiveFunction.getExpectedBestValue() - xValue) / dev);
    }

    /**
     * Getters & Setters
     *
     * @return
     */


    public boolean isMinima() {
        return this.isGlobalMinima.isSet();
    }

    public Vector getGBest() {
        return this.gBest;
    }

    public Double getBestDoubleValue() {
        return this.objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();
    }

    public String getBestVariables() {
        return this.getGBest().toString();
    }

    public ObjectiveFunction getFunction() {
        return this.objectiveFunction;
    }

    public int getStepsCount() {
        return stepsCount;
    }

    public double getConvergenceValue() {
        return convergenceValue;
    }

    public double getBestValue() {
        return this.bestValue;
    }

    public double getGradiantDecent() {
        return gradiantDecent;
    }

    public double getMeanBestValue() {
        return meanBestValue;
    }

    public List<Double> getHistory() {
        return history;
    }
    protected void checkPaused() {
        if (this.isPaused()) {
            while (this.isPaused()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void sort(){
        for(AgentGroup a: agents.values()){a.sort(new AgentComparator());}
    }
}
