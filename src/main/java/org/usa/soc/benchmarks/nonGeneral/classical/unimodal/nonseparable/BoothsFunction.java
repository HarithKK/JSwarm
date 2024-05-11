package org.usa.soc.benchmarks.nonGeneral.classical.unimodal.nonseparable;

import org.usa.soc.core.ObjectiveFunction;

/*
https://en.wikipedia.org/wiki/Booth_function
 */
public class BoothsFunction extends ObjectiveFunction {
    @Override
    public Double call() {

        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        double p1 = Math.pow(x + 2*y - 7, 2);
        double p2 = Math.pow(2*x + y - 5, 2);
        return p1 + p2;
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
        return new double[]{10, 10};
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{1,3};
    }
}
