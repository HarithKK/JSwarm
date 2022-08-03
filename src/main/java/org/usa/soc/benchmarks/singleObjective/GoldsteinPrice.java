package org.usa.soc.benchmarks.singleObjective;

import org.usa.soc.ObjectiveFunction;

public class GoldsteinPrice extends ObjectiveFunction {
    @Override
    public Double call() {
        double x = (double)super.getParameters()[0];
        double y = (double)super.getParameters()[1];

        double z1 = Math.pow((x + y+ 2),2)*(19-(14*x)+(3*(Math.pow(x,2))) - (14*y)+ (6*x*y) + (3 * Math.pow(y,2)));

        return Math.pow((1.5- x + (x*y)), 2) + Math.pow((2.25 - x + (x*Math.pow(y,2))),2) + Math.pow((2.625 - x + (x*Math.pow(y,3))),2);
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-2, -2};
    }

    @Override
    public double[] getMax() {
        return new double[]{2, 2};
    }
    @Override
    public double getExpectedBestValue() {
        return 3;
    }
}
