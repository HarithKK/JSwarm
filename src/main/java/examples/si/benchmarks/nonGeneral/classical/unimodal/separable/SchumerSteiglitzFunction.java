package examples.si.benchmarks.nonGeneral.classical.unimodal.separable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class SchumerSteiglitzFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        return Arrays.asList(super.getParameters()).stream().mapToDouble(d -> Math.pow((double)d, 4)).sum();
    }

    @Override
    public int getNumberOfDimensions() {
        return 10;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-100, this.getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(100, this.getNumberOfDimensions());
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(0, this.getNumberOfDimensions());
    }
}
