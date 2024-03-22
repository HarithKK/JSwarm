package org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class Function9 extends ObjectiveFunction {
    private int numberOfDimensions = 2;
    private double[] min;
    private double[] max;

    private double[] expected;

    public Function9(int n){
        this.numberOfDimensions = n;
        min = Commons.fill(-5.12, n);
        max = Commons.fill(5.12, n);
        expected = Commons.fill(-2, n);
    }

    @Override
    public Double call() {
        return Arrays.stream(super.getParameters()).mapToDouble(d->
                (Math.pow((double)d,2) - (10* Math.cos(2*Math.PI*(double)d)) + 10)).sum();
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
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return expected;
    }
}
