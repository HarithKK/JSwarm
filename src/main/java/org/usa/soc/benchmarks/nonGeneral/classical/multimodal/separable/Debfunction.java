package org.usa.soc.benchmarks.nonGeneral.classical.multimodal.separable;

import org.usa.soc.core.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class Debfunction extends ObjectiveFunction {
    @Override
    public Double call() {
        return Arrays.asList(super.getParameters()).stream().mapToDouble(d ->
                Math.sin(Math.pow((Double) d * 5 * Math.PI, 6))
        ).sum() * (-1 / getNumberOfDimensions());
    }

    @Override
    public int getNumberOfDimensions() {
        return 10;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-1, getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return  Commons.fill(1, getNumberOfDimensions());
    }

    @Override
    public double getExpectedBestValue() {
        return -1;
    }

    @Override
    public double[] getExpectedParameters() {
        return  Commons.fill(0, getNumberOfDimensions());
    }
}
