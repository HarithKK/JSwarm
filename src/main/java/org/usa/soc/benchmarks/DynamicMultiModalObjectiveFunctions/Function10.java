package org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class Function10 extends ObjectiveFunction {
    private int numberOfDimensions = 2;
    private double[] min;
    private double[] max;

    private double[] expected;

    public Function10(int n){
        this.numberOfDimensions = n;
        min = Commons.fill(-32, n);
        max = Commons.fill(32, n);
        expected = Commons.fill(0, n);
    }

    @Override
    public Double call() {
        Double y1 = Arrays.stream(super.getParameters()).mapToDouble(d->Math.pow((Double)d,2)).sum()/ numberOfDimensions;
        Double y2 = (-20)*Math.exp((-0.2)*Math.sqrt(y1));
        Double y3 = -Math.exp(Arrays.stream(super.getParameters()).mapToDouble(d->Math.cos(2*Math.PI*(double)d)).sum()/ numberOfDimensions);
        return y2+y3+20+Math.E;
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
