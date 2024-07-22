package examples.si.benchmarks.nonGeneral.classical.multimodal.separable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class Alpine1Function extends ObjectiveFunction {
    @Override
    public Double call() {
        Double r = 0.0;
        for(int i=0; i< getNumberOfDimensions(); i++){
            Double x = (Double) getParameters()[i];
            r += Math.abs((x * Math.sin(x)) + (0.1* x));
        }
        return r;
    }

    @Override
    public int getNumberOfDimensions() {
        return 10;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-10,getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(10, getNumberOfDimensions());
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
