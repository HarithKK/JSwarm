package examples.si.benchmarks.nonGeneral.classical.unimodal.nonseparable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class Schwefel22Function extends ObjectiveFunction {
    @Override
    public Double call() {
        Double res =0.0;
        for(int i=0; i<getNumberOfDimensions(); i++){
            res += Math.abs((Double) getParameters()[i]);
        }

        Double res1 =0.0;
        for(int i=0; i<getNumberOfDimensions(); i++){
            res1 *= Math.abs((Double) getParameters()[i]);
        }
        return res + res1;
    }

    @Override
    public int getNumberOfDimensions() {
        return 10;
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
