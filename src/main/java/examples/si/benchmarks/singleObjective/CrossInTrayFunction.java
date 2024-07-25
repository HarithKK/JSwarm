package examples.si.benchmarks.singleObjective;

import org.usa.soc.si.ObjectiveFunction;

public class CrossInTrayFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        double d1 = Math.abs(100 - ( Math.sqrt( Math.pow(x,2) + Math.pow(y,2)) / Math.PI));
        d1 = Math.sin(x)* Math.sin(y)*Math.exp(d1);
        return Math.pow(Math.abs(d1) + 1, 0.1) * (-0.0001);
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-10, -10};
    }

    @Override
    public double[] getMax() {
        return new double[]{10, 10};
    }

    @Override
    public double getExpectedBestValue() {
        return -2.06261;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{1.34941, -1.34941, -1.34941, 1.34941, -1.34941, -1.34941, 1.34941, 1.34941};
    }
}
