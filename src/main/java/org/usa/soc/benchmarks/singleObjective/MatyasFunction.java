package org.usa.soc.benchmarks.singleObjective;

import org.usa.soc.ObjectiveFunction;

/*
https://en.wikipedia.org/wiki/Matyas_function
 */
public class MatyasFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        return ((0.26)*(Math.pow(x,2) + Math.pow(y,2))) - 0.48*x*y;
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-10, -10};
    }

    @Override
    public double[] getMax() {
        return new double[]{10,10};
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }
}
