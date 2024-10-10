package examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

/*
https://en.wikipedia.org/wiki/Ackley_function
https://www.sfu.ca/~ssurjano/ackley.html
 */
public class AckleysFunction extends ObjectiveFunction {

    @Override
    public Double call() {

        Double d1 = Arrays.asList(super.getParameters()).stream().mapToDouble(d -> Math.pow((Double)d,2)).sum();
        Double d2 = Arrays.asList(super.getParameters()).stream().mapToDouble(d -> Math.cos(2*Math.PI*(Double)d)).sum();
        double p1 = -20*Math.exp(-0.2*Math.sqrt(d1/numberOfDimensions));
        double p2 = Math.exp(d2/numberOfDimensions);
        return p1 - p2 + Math.E + 20;
    }

    @Override
    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-32.768, numberOfDimensions);
    }

    @Override
    public double[] getMax() {
        return Commons.fill(32.768, numberOfDimensions);
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
