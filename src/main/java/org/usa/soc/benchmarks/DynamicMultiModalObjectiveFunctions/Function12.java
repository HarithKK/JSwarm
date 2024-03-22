package org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class Function12 extends ObjectiveFunction {
    private int numberOfDimensions = 2;
    private double[] min;
    private double[] max;

    private double[] expected;

    public Function12(int n){
        this.numberOfDimensions = n;
        min = Commons.fill(-50, n);
        max = Commons.fill(50, n);
        expected = Commons.fill(-30, n);
    }

    private double Y(int i){
        return 1 + (((double)super.getParameters()[i]+1)/4);
    }

    private double U(double x, int a, int k, int m){
        if(x < -a){
            return k*Math.pow(-x-a, m);
        }else if(x >= -a && x <= a){
            return 0;
        }else{
            return k*Math.pow(x-a, m);
        }
    }

    @Override
    public Double call() {

        double y1 = 10*Math.sin(Math.PI * Y(0));
        double y2 = 0;

        for(int i=0; i<numberOfDimensions-1; i++){
            y2 += Math.pow(Y(i)-1,2) * (1 + 10*Math.pow(Math.sin(Math.PI*Y(i+1)),2));
        }

        double y3 = Math.pow(Y(numberOfDimensions-1)-1,2);
        double y4 = Arrays.stream(super.getParameters()).mapToDouble(d-> U((double)d, 10, 100, 4)).sum();

        return (Math.PI * (y1 + y2+ y3) / numberOfDimensions) + y4;
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
