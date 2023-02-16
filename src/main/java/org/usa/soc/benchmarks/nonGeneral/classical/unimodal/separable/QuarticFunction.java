package org.usa.soc.benchmarks.nonGeneral.classical.unimodal.separable;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Randoms;

/*
https://al-roomi.org/benchmarks/unconstrained/n-dimensions/161-quartic-or-modified-4th-de-jong-s-function
 */
public class QuarticFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        Double sum = 0.0;
        for(int i=0;i< getNumberOfDimensions();i++){
            sum += (((i+1) * Math.pow((Double)super.getParameters()[i], 4)));
        }
        return sum;
    }

    @Override
    public int getNumberOfDimensions() {
        return 10;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-1.28, this.getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(1.28, this.getNumberOfDimensions());
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
