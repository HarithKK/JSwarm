package org.usa.soc.benchmarks.singleObjective;

import org.usa.soc.core.ObjectiveFunction;

public class BealeFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        double x = (double)super.getParameters()[0];
        double y = (double)super.getParameters()[1];
        return Math.pow((1.5- x + (x*y)), 2) + Math.pow((2.25 - x + (x*Math.pow(y,2))),2) + Math.pow((2.625 - x + (x*Math.pow(y,3))),2);
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-4.5, -4.5};
    }

    @Override
    public double[] getMax() {
        return new double[]{4.5, 4.5};
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{3, 0.5};
    }
}
