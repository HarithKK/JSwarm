package examples.si.benchmarks.nonGeneral.classical.multimodal.separable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class CsendesFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        return Arrays.asList(super.getParameters()).stream().mapToDouble(d ->
            Math.pow((Double) d, 6) * (2+ Math.sin(1/ (Double)d))
        ).sum();
    }

    @Override
    public int getNumberOfDimensions() {
        return 10;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-1, getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(1, getNumberOfDimensions());
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(0, getNumberOfDimensions());
    }
}
