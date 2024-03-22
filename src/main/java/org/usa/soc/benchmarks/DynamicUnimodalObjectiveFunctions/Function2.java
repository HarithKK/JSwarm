package org.usa.soc.benchmarks.DynamicUnimodalObjectiveFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class Function2 extends ObjectiveFunction {
    private int numberOfDimensions = 100;
    private double[] min;
    private double[] max;

    private double[] expected;

    public  Function2(int n){
        this.numberOfDimensions = n;
        min = Commons.fill(-10, n);
        max = Commons.fill(10, n);
        expected = Commons.fill(-3, n);
    }

    @Override
    public Double call() {
        Double sum = 0.0;
        Double dot = 0.0;
        for(int i=0;i< getNumberOfDimensions();i++){
            Double data = Math.abs((Double)super.getParameters()[i]);
            sum += data;
            dot *= data;
        }
        return sum + dot;
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
