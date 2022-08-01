package org.usa.soc.benchmarks.singleObjectiveConstrained;

import org.usa.soc.ObjectiveFunction;

public class MishraBirdFunction extends ObjectiveFunction {

    @Override
    public Double call() {
        double x = (double)super.getParameters()[0];
        double y = (double)super.getParameters()[1];

        return (Math.sin(y)*Math.exp(Math.pow((1 - Math.cos(2)),2))) +
                (Math.cos(y)*Math.exp(Math.pow((1 - Math.sin(2)),2))) +
                    Math.pow((x - y),2);
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-10,-6.5};
    }

    @Override
    public double[] getMax() {
        return new double[]{0,0};
    }

    @Override
    public boolean validateRange(){
        double x = (double)super.getParameters()[0];
        double y = (double)super.getParameters()[1];
        return Math.pow(x+5, 2) + Math.pow(y+5, 2) <= 25;
    };
}
