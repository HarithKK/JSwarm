package org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class Function11 extends ObjectiveFunction {
    private int numberOfDimensions = 2;
    private double[] min;
    private double[] max;

    private double[] expected;

    public Function11(int n){
        this.numberOfDimensions = n;
        min = Commons.fill(-600, n);
        max = Commons.fill(600, n);
        expected = Commons.fill(-400, n);
    }

    @Override
    public Double call() {

        double y1 =0;
        double y2=0;

        for(int i=0; i< numberOfDimensions; i++){
            double x = (double) super.getParameters()[i];
            y1 += Math.pow(x,2);
            y2 *= Math.cos(x/ Math.sqrt(i));
        }
        return (y1/4000) - y2 +1;
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
