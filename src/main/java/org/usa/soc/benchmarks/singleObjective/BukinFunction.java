package org.usa.soc.benchmarks.singleObjective;

import org.usa.soc.ObjectiveFunction;

/*
f(x,y)=100{\sqrt {\left|y-0.01x^{2}\right|}}+0.01\left|x+10\right|.\quad
 */

public class BukinFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        return (100 * Math.sqrt(Math.abs(y - (0.01*Math.pow(x,2))))) + (0.01*Math.abs(x+ 10));
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-15, -3};
    }

    @Override
    public double[] getMax() {
        return new double[]{-5,3};
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{-10,1};
    }
}
