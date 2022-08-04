package org.usa.soc.benchmarks.singleObjective;

import org.usa.soc.ObjectiveFunction;

import java.util.Arrays;

public class StyblinskiTangFunction extends ObjectiveFunction {
    @Override
    public Double call() {

        Double dx = Arrays.stream(super.getParameters()).mapToDouble(v -> {
            Double d = (Double)v;
            return Math.pow(d, 4) - (16*Math.pow(d,2)) + 5*d;
        }).sum();
        return dx /2;
    }

    @Override
    public int getNumberOfDimensions() {
        return 3;
    }

    @Override
    public double[] getMin() {
        return new double[]{-5, -5, -5 };
    }

    @Override
    public double[] getMax() {
        return new double[]{1, 1, 1};
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{-2.903534, -2.903534, -2.903534};
    }
}
