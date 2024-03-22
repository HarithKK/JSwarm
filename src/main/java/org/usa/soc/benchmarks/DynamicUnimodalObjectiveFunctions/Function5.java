package org.usa.soc.benchmarks.DynamicUnimodalObjectiveFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class Function5 extends ObjectiveFunction {
    private int numberOfDimensions = 100;
    private double[] min;
    private double[] max;

    private double[] expected;

    public Function5(int n){
        this.numberOfDimensions = n;
        min = Commons.fill(-30, n);
        max = Commons.fill(30, n);
        expected = Commons.fill(-15, n);
    }

    @Override
    public Double call() {
        Double sum = 0.0;
        for(int i=0;i< getNumberOfDimensions()-1;i++){
            double i1 = (Double)super.getParameters()[i+1];
            double i0 = (Double)super.getParameters()[i];
            sum += Math.abs( 100* Math.pow(i1 - Math.pow(i0,2),2) + Math.pow((i0 - 1), 2));
        }
        return sum;
    }

    @Override
    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    @Override
    public double[] getMin() {
        return min;
    }

    @Override
    public double[] getMax() {
        return max;
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return expected;
    }
}
