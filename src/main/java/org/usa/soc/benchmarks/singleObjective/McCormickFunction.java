package org.usa.soc.benchmarks.singleObjective;

import org.usa.soc.ObjectiveFunction;

/*
f(x,y)=\sin \left(x+y\right)+\left(x-y\right)^{2}-1.5x+2.5y+1
 */
public class McCormickFunction extends ObjectiveFunction {
    @Override
    public Double call() {

        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        return Math.sin(x + y) + Math.pow(x-y, 2) - (1.5*x) + (2.5*y) +1;
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-1.5, -3};
    }

    @Override
    public double[] getMax() {
        return new double[]{4,4};
    }
}
