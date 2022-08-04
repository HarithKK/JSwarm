package org.usa.soc.benchmarks.singleObjective;

import org.usa.soc.ObjectiveFunction;

public class EggholderFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        double d1 =  (y+47) * Math.sin(Math.sqrt(Math.abs((x/2) + (y+47))));
        double d2 = x * Math.sin(Math.sqrt(Math.abs(x - (y+47))));
        return (-1) * (d1+d2);
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-512, -512};
    }

    @Override
    public double[] getMax() {
        return new double[]{512, 512};
    }

    @Override
    public double getExpectedBestValue() {
        return -959.6404;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{512, 404.2319};
    }
}
