package org.usa.soc.si.benchmarks.singleObjective;

import org.usa.soc.si.ObjectiveFunction;

public class RosenbrockFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        double k =0;
        for(int i=1;i<getNumberOfDimensions()-1;i++){
            double x= (double)super.getParameters()[i];
            double x1= (double)super.getParameters()[i+1];

            k += (Math.pow((x1 - Math.pow(x, 2)), 2) * 100) + Math.pow((x-1),2);
        }
        return k;
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-1000,-1000};
    }

    @Override
    public double[] getMax() {
        return new double[]{1000,1000};
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{1,1};
    }
}
