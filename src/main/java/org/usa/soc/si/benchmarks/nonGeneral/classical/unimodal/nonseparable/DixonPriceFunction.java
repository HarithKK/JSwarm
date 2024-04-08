package org.usa.soc.si.benchmarks.nonGeneral.classical.unimodal.nonseparable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class DixonPriceFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        Double res =0.0;
        for(int i=1; i<getNumberOfDimensions(); i++){
            Double x = (Double) getParameters()[i];
            res += (i+1) * Math.pow((2*Math.pow(x,2) - x -1), 2);
        }
        return res;
    }

    @Override
    public int getNumberOfDimensions() {
        return 3;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-10, getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(10, getNumberOfDimensions());
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(0, getNumberOfDimensions());
    }
}
