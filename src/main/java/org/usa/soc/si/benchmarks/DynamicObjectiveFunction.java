package org.usa.soc.si.benchmarks;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public abstract class DynamicObjectiveFunction extends ObjectiveFunction {
    private int numberOfDimensions = 100;
    private double[] min;
    private double[] max;

    private double[] expected;

    private double expectedValue;

    public  DynamicObjectiveFunction(int n, double minValue, double maxValue, double expectedParameter, double expectedBestValue){
        this.numberOfDimensions = n;
        min = Commons.fill(minValue, n);
        max = Commons.fill(maxValue, n);
        expected = Commons.fill(expectedParameter, n);
        expectedValue = expectedBestValue;
    }

    public  DynamicObjectiveFunction(int n, double minValue, double maxValue, double[] expectedParameters, double expectedBestValue){
        this.numberOfDimensions = n;
        min = Commons.fill(minValue, n);
        max = Commons.fill(maxValue, n);
        expected = expectedParameters;
        expectedValue = expectedBestValue;
    }

    public  DynamicObjectiveFunction(int n, double[] minValue, double[] maxValue, double[] expectedParameters, double expectedBestValue){
        this.numberOfDimensions = n;
        min = minValue;
        max = maxValue;
        expected = expectedParameters;
        expectedValue = expectedBestValue;
    }

    @Override
    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    @Override
    public double[] getMin() {
        return min;
    }

    @Override
    public double[] getMax() {
        return max;
    }

    @Override
    public double getExpectedBestValue() {
        return expectedValue;
    }

    @Override
    public double[] getExpectedParameters() {
        return expected;
    }
}
