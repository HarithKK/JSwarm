package examples.si.benchmarks.nonGeneral.classical.unimodal.nonseparable.notincluded;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class RotatedEllipsisFunction extends ObjectiveFunction {

    @Override
    public Double call() {

        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        return (7 * Math.pow(x, 2)) - (6 * Math.sqrt(3) * x * y) + (13 * Math.pow(y,2));
    }

    @Override
    public int getNumberOfDimensions() {
        return 2;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-500, getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(500, getNumberOfDimensions());
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
