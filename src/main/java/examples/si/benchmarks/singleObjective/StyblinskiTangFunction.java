package examples.si.benchmarks.singleObjective;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class StyblinskiTangFunction extends ObjectiveFunction {
    @Override
    public Double call() {

        Double dx = Arrays.stream(super.getParameters()).mapToDouble(v -> {
            Double d = (Double)v;
            return Math.pow(d, 4) - (16*Math.pow(d,2)) + 5*d;
        }).sum();
        return dx /2;
    }

    @Override
    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-5, numberOfDimensions);
    }

    @Override
    public double[] getMax() {
        return Commons.fill(1, numberOfDimensions);
    }

    @Override
    public double getExpectedBestValue() {
        return -117.5;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(-2.903534, numberOfDimensions);
    }
}
