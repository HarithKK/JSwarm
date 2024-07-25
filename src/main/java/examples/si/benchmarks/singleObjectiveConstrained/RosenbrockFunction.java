package examples.si.benchmarks.singleObjectiveConstrained;

import org.usa.soc.si.ObjectiveFunction;

public class RosenbrockFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        double x = (double)super.getParameters()[0];
        double y = (double)super.getParameters()[1];
        return Math.pow((1-x),2) + 100*Math.pow((y-Math.pow(x,2)),2);
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-1.5,-0.5};
    }

    @Override
    public double[] getMax() {
        return new double[]{1.5,2.5};
    }

    @Override
    public boolean validateRange(){
        double x = (double)super.getParameters()[0];
        double y = (double)super.getParameters()[1];
        return Math.pow(x-1, 3) - y + 1<=0 && x + y -2 <=0;
    };

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[0];
    }
}
