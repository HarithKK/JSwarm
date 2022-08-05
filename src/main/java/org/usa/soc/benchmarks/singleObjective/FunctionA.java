package org.usa.soc.benchmarks.singleObjective;

import org.usa.soc.ObjectiveFunction;

public class FunctionA extends ObjectiveFunction {
    @Override
    public Double call() {
        Double x = (Double) super.getParameters()[0];
        if(x == Double.POSITIVE_INFINITY){
            return Double.POSITIVE_INFINITY;
        }
        if(x == Double.NEGATIVE_INFINITY){
            return Double.NEGATIVE_INFINITY;
        }
        return Math.pow(x, 4) - 2 * Math.pow(x, 3);
    }

    @Override
    public int getNumberOfDimensions() {
        return 1;
    }

    @Override
    public double[] getMin() {
        return new double[]{-100};
    }

    @Override
    public double[] getMax() {
        return new double[]{101};
    }

    @Override
    public double getExpectedBestValue() {
        return -1.69;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{1.5};
    }
}
