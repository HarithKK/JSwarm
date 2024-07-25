package examples.si.benchmarks.singleObjective;

import org.usa.soc.si.ObjectiveFunction;

import java.util.Arrays;

public class SphereFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        return Arrays.asList(super.getParameters()).stream().mapToDouble(d -> Math.pow((double)d, 2)).sum();
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return new double[]{-100,-100};
    }

    @Override
    public double[] getMax() {
        return new double[]{100,100};
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{0,0};
    }
}
