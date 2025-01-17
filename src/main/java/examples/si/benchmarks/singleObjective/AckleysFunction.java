package examples.si.benchmarks.singleObjective;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

/*
https://en.wikipedia.org/wiki/Ackley_function
https://www.sfu.ca/~ssurjano/ackley.html
 */
public class AckleysFunction extends ObjectiveFunction {
    private double[] min = new double[]{-5, -5};
    private double[] max = new double[]{5,5};

    @Override
    public Double call() {

        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        double p1 = -20*Math.exp(-0.2*Math.sqrt(0.5*((x*x)+(y*y))));
        double p2 = Math.exp(0.5*(Math.cos(2*Math.PI*x)+Math.cos(2*Math.PI*y)));
        return p1 - p2 + Math.E + 20;
    }

    @Override
    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-5,numberOfDimensions);
    }

    @Override
    public double[] getMax() {
        return Commons.fill(5,numberOfDimensions);
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(0,numberOfDimensions);
    }

}
