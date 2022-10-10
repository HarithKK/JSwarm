package org.usa.soc.benchmarks.nonGeneral.classical.multimodal.separable;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class EasomFunction extends ObjectiveFunction {
    @Override
    public Double call() {

        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        return -Math.cos(x)*Math.cos(y)*Math.exp(-Math.pow(x-Math.PI, 2) - Math.pow(y - Math.PI, 2));
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-100, this.getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(100, this.getNumberOfDimensions());
    }

    @Override
    public double getExpectedBestValue() {
        return -1;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{3.14, 3.14};
    }
}
