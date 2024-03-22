package org.usa.soc.benchmarks.DynamicUnimodalObjectiveFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class Function3 extends ObjectiveFunction {
    private int numberOfDimensions = 2;
    private double[] min;
    private double[] max;

    private double[] expected;

    public Function3(int n){
        this.numberOfDimensions = n;
        min = Commons.fill(-100, n);
        max = Commons.fill(100, n);
        expected = Commons.fill(-30, n);
    }

    @Override
    public Double call() {

        Double []cols = new Double[getNumberOfDimensions()];
        Double sum = 0.0;
        for(int i=0;i< getNumberOfDimensions();i++){
            cols[i] = sum;
            sum += (Double)super.getParameters()[i];
        }
        sum = 0.0;
        for(int i=1;i< getNumberOfDimensions();i++){
            sum += Math.pow(cols[i],2);
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
