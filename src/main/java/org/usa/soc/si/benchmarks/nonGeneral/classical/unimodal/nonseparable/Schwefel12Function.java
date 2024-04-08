package org.usa.soc.si.benchmarks.nonGeneral.classical.unimodal.nonseparable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class Schwefel12Function extends ObjectiveFunction {
    @Override
    public Double call() {
        Double res =0.0;
        for(int i=0; i<getNumberOfDimensions(); i++){
            Double r = 0.0;
            for(int j=0; j<=i;j++){
                r += (Double)getParameters()[j];
            }
            res += Math.pow(r, 2);
        }
        return res;
    }

    @Override
    public int getNumberOfDimensions() {
        return 10;
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
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(0, getNumberOfDimensions());
    }
}
