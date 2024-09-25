package examples.si.benchmarks.singleObjective;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class SphereFunction extends ObjectiveFunction {

    int n = 2;

    public SphereFunction(){}
    public SphereFunction(int n){this.n=n;}
    @Override
    public Double call() {
        return Arrays.asList(super.getParameters()).stream().mapToDouble(d -> Math.pow((double)d, 2)).sum();
    }

    @Override
    public int getNumberOfDimensions() {
        return n;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-100, n);
    }

    @Override
    public double[] getMax() {
        return Commons.fill(100, n);
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
