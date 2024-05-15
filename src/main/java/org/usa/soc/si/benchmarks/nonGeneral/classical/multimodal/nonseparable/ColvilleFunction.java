package org.usa.soc.si.benchmarks.nonGeneral.classical.multimodal.nonseparable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class ColvilleFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        Double x1 = (Double) super.getParameters()[0];
        Double x2 = (Double) super.getParameters()[1];
        Double x3 = (Double) super.getParameters()[2];
        Double x4 = (Double) super.getParameters()[3];

        Double d = 100 * Math.pow((Math.pow(x1,2) - x2), 2);
        d += Math.pow(x1-1, 2);
        d += Math.pow(x3-1, 2);
        d += 90*Math.pow(Math.pow(x3,2)-x4, 2);
        d += 10.1*(Math.pow(x2-1,2) + Math.pow(x4-1,2));
        d += 19.8*(x2-1)*(x4-1);
        return d;
    }

    @Override
    public int getNumberOfDimensions() {
        return 4;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-10, this.getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(10, this.getNumberOfDimensions());
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(1, this.getNumberOfDimensions());
    }
}
