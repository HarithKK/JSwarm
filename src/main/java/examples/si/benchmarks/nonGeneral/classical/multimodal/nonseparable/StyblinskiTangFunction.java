package examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class StyblinskiTangFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        Double sum =0.0;
        for(int i=0; i<getNumberOfDimensions(); i++){
            double x = (Double) getParameters()[i];
            sum += Math.pow(x,4) - (16*x*x) + (5*x);
        }
        return sum * 0.5;
    }

    @Override
    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-5, getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(5, getNumberOfDimensions());
    }

    @Override
    public double getExpectedBestValue() {
        return -39.16599*numberOfDimensions;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(-2.903534, getNumberOfDimensions());
    }
}
