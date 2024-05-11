package org.usa.soc.benchmarks.nonGeneral.classical.multimodal.nonseparable;

import org.usa.soc.core.ObjectiveFunction;

public class GoldsteinPrice extends ObjectiveFunction {
    @Override
    public Double call() {
        double x = (double)super.getParameters()[0];
        double y = (double)super.getParameters()[1];

        double z1 = 1+Math.pow((x + y+ 2),2)*(19-(14*x)+(3*(Math.pow(x,2))) - (14*y)+ (6*x*y) + (3 * Math.pow(y,2)));
        double z2 = 30+Math.pow((2*x - 3*y),2)*(18-(32*x)+(12*(Math.pow(x,2))) + (48*y) - (36*x*y) + (27 * Math.pow(y,2)));

        return z1 * z2;
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

    @Override
    public double[] getExpectedParameters() {
        return new double[]{0,-1};
    }
}
