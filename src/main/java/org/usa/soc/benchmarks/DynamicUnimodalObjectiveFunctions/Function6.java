package org.usa.soc.benchmarks.DynamicUnimodalObjectiveFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Function6 extends ObjectiveFunction {
    private int numberOfDimensions = 100;
    private double[] min;
    private double[] max;

    private double[] expected;

    public Function6(int n){
        this.numberOfDimensions = n;
        min = Commons.fill(-100, n);
        max = Commons.fill(100, n);
        expected = Commons.fill(-750, n);
    }

    @Override
    public Double call() {
        return Arrays.stream(super.getParameters()).mapToDouble(d-> Math.pow(Math.abs((Double)d + 0.5),2)).sum();
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
