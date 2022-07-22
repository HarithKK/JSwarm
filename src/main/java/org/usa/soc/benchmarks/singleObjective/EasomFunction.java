package org.usa.soc.benchmarks.singleObjective;

import org.usa.soc.ObjectiveFunction;

public class EasomFunction extends ObjectiveFunction {
    @Override
    public Double call() {

        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        return -Math.cos(x)*Math.cos(y)*Math.exp(-(Math.pow(x-Math.PI, 2) + Math.pow(y - Math.PI, 2)));
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-100, -100};
    }

    @Override
    public double[] getMax() {
        return new double[]{100,100};
    }
}
