package org.usa.soc.benchmarks.nonGeneral.classical.unimodal.nonseparable;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class BealeFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        double x = (double)super.getParameters()[0];
        double y = (double)super.getParameters()[1];
        return Math.pow((1.5- x + (x*y)), 2) + Math.pow((2.25 - x + (x*Math.pow(y,2))),2) + Math.pow((2.625 - x + (x*Math.pow(y,3))),2);
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-4.5, this.getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(4.5, this.getNumberOfDimensions());
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{3, 0.5};
    }
}
