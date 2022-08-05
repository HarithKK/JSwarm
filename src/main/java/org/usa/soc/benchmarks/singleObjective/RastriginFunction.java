package org.usa.soc.benchmarks.singleObjective;

import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.usa.soc.ObjectiveFunction;

import java.util.Arrays;

public class RastriginFunction extends ObjectiveFunction {

    double A = 10;

    @Override
    public Double call() {
        return A*getNumberOfDimensions() +
                Arrays.asList(super.getParameters()).stream().mapToDouble(p -> {
                    double i = (double)p;
                    return Math.pow(i, 2) - A* Math.cos(2 * Math.PI *i);
                }).sum();
    }

    @Override
    public int getNumberOfDimensions() {
        return 3;
    }

    @Override
    public double[] getMin() {
        return new double[]{-5.12,-5.12, -5.12};
    }

    @Override
    public double[] getMax() {
        return new double[]{5.12,5.12, 5.12};
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{0,0,0};
    }
}
