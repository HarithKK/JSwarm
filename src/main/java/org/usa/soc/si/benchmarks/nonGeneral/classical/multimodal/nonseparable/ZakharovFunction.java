package org.usa.soc.si.benchmarks.nonGeneral.classical.multimodal.nonseparable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class ZakharovFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        Double res1 =0.0;
        for(int i=0; i<getNumberOfDimensions(); i++){
            res1 += 0.5 * (i+1) * (Double) getParameters()[i];
        }
        return Arrays.asList(super.getParameters()).stream().mapToDouble(d -> Math.pow((double)d, 2)).sum()
                + Math.pow(res1, 2)
                + Math.pow(res1, 4);
    }

    @Override
    public int getNumberOfDimensions() {
        return 10;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-5, getNumberOfDimensions());
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
