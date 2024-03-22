package org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Randoms;

import java.util.Arrays;

public class Function8 extends ObjectiveFunction {
    private int numberOfDimensions = 100;
    private double[] min;
    private double[] max;

    private double[] expected;

    public Function8(int n){
        this.numberOfDimensions = n;
        min = Commons.fill(-500, n);
        max = Commons.fill(500, n);
        expected = Commons.fill(-300, n);
    }

    @Override
    public Double call() {
        return Arrays.stream(super.getParameters()).mapToDouble(d-> (-(double)d)*Math.sin(Math.sqrt(Math.abs((double)d)))).sum();
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
        return -418.9829 * numberOfDimensions;
    }

    @Override
    public double[] getExpectedParameters() {
        return expected;
    }
}
