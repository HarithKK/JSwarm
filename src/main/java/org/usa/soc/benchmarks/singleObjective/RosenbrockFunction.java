package org.usa.soc.benchmarks.singleObjective;

import org.usa.soc.ObjectiveFunction;

import java.util.Arrays;

public class RosenbrockFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        double k =0;
        for(int i=0;i<2;i++){
            double x= (double)super.getParameters()[i];
            double x1= (double)super.getParameters()[i+1];

            k += (Math.pow((x1 - Math.pow(x, 2)), 2) * 100) + Math.pow((1-x),2);
        }
        return k;
    }

    @Override
    public int getNumberOfDimensions() {
        return 3;
    }

    @Override
    public double[] getMin() {
        return new double[]{-1000,-1000,-1000};
    }

    @Override
    public double[] getMax() {
        return new double[]{1000,1000,1000};
    }

    @Override
    public double getExpectedBestValue() {
        return 3;
    }
}
