package org.usa.soc.benchmarks.nonGeneral.classical.unimodal.separable;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class ChungReynoldsSquares extends ObjectiveFunction {
    @Override
    public Double call() {
        Double sum = 0.0;
        for(int i=0;i< getNumberOfDimensions();i++){
            sum += ((i+1) * Math.pow((Double)super.getParameters()[i], 2));
        }
        return Math.pow(Arrays.asList(super.getParameters()).stream().mapToDouble(d -> Math.pow((double)d,2) ).sum(), 2);
    }

    @Override
    public int getNumberOfDimensions() {
        return 10;
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
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(0, this.getNumberOfDimensions());
    }
}
