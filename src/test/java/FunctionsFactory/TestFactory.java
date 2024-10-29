package FunctionsFactory;

import examples.si.benchmarks.singleObjective.SphereFunction;
import org.junit.jupiter.api.Test;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.si.runners.FunctionsFactory;
import org.usa.soc.si.runners.Main;
import org.usa.soc.util.Commons;

import java.util.Arrays;

class OF extends ObjectiveFunction{

    @Override
    public Double call() {
        return Arrays.stream(this.getParameters()).mapToDouble(d -> Math.pow((double)d, 2)).sum();
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-100, numberOfDimensions);
    }

    @Override
    public double[] getMax() {
        return Commons.fill(100, numberOfDimensions);
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

public class TestFactory {

    public static void main(String[] args) {

        Main.executeMain(new FunctionsFactory().register(new OF()).build());
    }
}
