package org.usa.soc.benchmarks.nonGeneral.classical.unimodal.separable;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class StepintFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        return Arrays.asList(super.getParameters()).stream().mapToDouble(d -> Math.abs((double)d)).sum() + 25;
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-5.12, this.getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(5.12, this.getNumberOfDimensions());
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(0, this.getNumberOfDimensions());
    }
}
