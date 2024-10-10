package examples.si.benchmarks.nonGeneral.classical.unimodal.nonseparable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class BentCigarFunction extends ObjectiveFunction {
    @Override
    public Double call() {
        double sum = 0.0;
        for(int i=1;i<numberOfDimensions;i++){
            sum += Math.pow((Double)getParameters()[i],2);
        }
        return Math.pow((Double)getParameters()[0],2) + (sum*1000000);
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
        return Commons.fill(100, numberOfDimensions*2);
    }
}
