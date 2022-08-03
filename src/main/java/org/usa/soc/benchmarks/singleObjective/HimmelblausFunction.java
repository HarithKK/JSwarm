package org.usa.soc.benchmarks.singleObjective;


import org.usa.soc.ObjectiveFunction;

public class HimmelblausFunction extends ObjectiveFunction {
    @Override
    public Double call() {

        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        return Math.pow((Math.pow(x, 2) + y - 11),2) + Math.pow((x + Math.pow(y,2) -7), 2);
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
}
