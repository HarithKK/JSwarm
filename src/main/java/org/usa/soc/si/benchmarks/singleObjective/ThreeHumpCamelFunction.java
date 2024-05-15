package org.usa.soc.si.benchmarks.singleObjective;

import org.usa.soc.si.ObjectiveFunction;

public class ThreeHumpCamelFunction extends ObjectiveFunction {
    @Override
    public Double call() {

        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        return (2*Math.pow(x,2)) - (1.05*Math.pow(x,4)) + (Math.pow(x,6)/6) + (x*y) + (Math.pow(y,2));
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-5, -5};
    }

    @Override
    public double[] getMax() {
        return new double[]{5, 5};
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{0,0};
    }
}