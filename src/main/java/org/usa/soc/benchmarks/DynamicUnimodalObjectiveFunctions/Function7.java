package org.usa.soc.benchmarks.DynamicUnimodalObjectiveFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Randoms;

import java.util.Arrays;

public class Function7 extends ObjectiveFunction {
    private int numberOfDimensions = 100;
    private double[] min;
    private double[] max;

    private double[] expected;

    public Function7(int n){
        this.numberOfDimensions = n;
        min = Commons.fill(-1.28, n);
        max = Commons.fill(1.28, n);
        expected = Commons.fill(-0.25, n);
    }

    @Override
    public Double call() {
        Double sum = 0.0;
        for(int i=0;i< getNumberOfDimensions();i++){
            Double data = (Double)super.getParameters()[i];
            sum += i* Math.pow(data,4) + Randoms.randLBmax(0, 1);
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
