package org.usa.soc.benchmarks.singleObjective;

import org.usa.soc.ObjectiveFunction;

import java.util.Arrays;

public class SphereFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        return Arrays.asList(super.getParameters()).stream().mapToDouble(d -> Math.pow((double)d, 2)).sum();
    }

    @Override
    public int getNumberOfDimensions() {
        return 3;
    }

    @Override
    public double[] getMin() {
        return new double[]{-1000,-1000,-1000};
    }

    @Override
    public double[] getMax() {
        return new double[]{1000,1000,1000};
    }
}
