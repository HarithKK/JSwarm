package org.usa.soc.benchmarks.singleObjective;

import org.usa.soc.ObjectiveFunction;

public class LevyFunction extends ObjectiveFunction {
    @Override
    public Double call() {

        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        double d1 = Math.pow(Math.sin(3* Math.PI * x), 2) ;
        double d2 = Math.pow((x-1), 2)*(1 + Math.pow(Math.sin(3*Math.PI *y),2));
        double d3 = Math.pow(y-1,2) * (1 + Math.pow(Math.sin(2*Math.PI *y),2));

        return d1 + d2 + d3;
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
        return new double[]{1,1};
    }
}
