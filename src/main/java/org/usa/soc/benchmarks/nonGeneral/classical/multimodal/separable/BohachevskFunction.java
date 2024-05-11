package org.usa.soc.benchmarks.nonGeneral.classical.multimodal.separable;

import org.usa.soc.core.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class BohachevskFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        return Math.pow(x,2) + (Math.pow(y,2) * 2) - (0.3*Math.cos(3 * Math.PI * x)) - (0.4 * Math.cos(4*Math.PI*y)) + 0.7;
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-100, getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(100, getNumberOfDimensions());
    }

    @Override
    public double getExpectedBestValue() {
        return 0.0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(0, getNumberOfDimensions());
    }
}
