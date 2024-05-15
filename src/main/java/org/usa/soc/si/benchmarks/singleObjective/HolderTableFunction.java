package org.usa.soc.si.benchmarks.singleObjective;

import org.usa.soc.si.ObjectiveFunction;

public class HolderTableFunction extends ObjectiveFunction {
    @Override
    public Double call() {

        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        double d = Math.abs(1 - (Math.sqrt(Math.pow(x,2) + Math.pow(y,2)) / Math.PI));
        return -Math.abs(Math.sin(x) * Math.cos(y) * Math.exp(d));
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
        return -19.2085;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{8.05502, -9.66459, -8.05502, 9.66459, -8.05502, -9.66459, 8.05502, 9.66459};
    }
}