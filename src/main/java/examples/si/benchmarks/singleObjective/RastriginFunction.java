package examples.si.benchmarks.singleObjective;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class RastriginFunction extends ObjectiveFunction {

    double A = 10;

    @Override
    public Double call() {
        return A*getNumberOfDimensions() +
                Arrays.asList(super.getParameters()).stream().mapToDouble(p -> {
                    double i = (double)p;
                    return Math.pow(i, 2) - A* Math.cos(2 * Math.PI *i);
                }).sum();
    }

    @Override
    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-5.12, numberOfDimensions);
    }

    @Override
    public double[] getMax() {
        return Commons.fill(5.12, numberOfDimensions);
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(0, numberOfDimensions);
    }
}
