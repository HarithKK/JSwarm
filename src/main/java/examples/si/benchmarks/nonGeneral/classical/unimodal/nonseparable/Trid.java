package examples.si.benchmarks.nonGeneral.classical.unimodal.nonseparable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class Trid extends ObjectiveFunction {
    @Override
    public Double call() {
        double d1 = 0;
        double d2 = 0;

        for(int i=0; i<numberOfDimensions; i++){
            d1 += Math.pow((Double)super.getParameters()[i], 2);
        }

        for(int i=1; i<numberOfDimensions; i++){
            d2 += (Double)super.getParameters()[i] * (Double)super.getParameters()[i-1];
        }
        return d1 - d2;
    }

    @Override
    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-Math.pow(numberOfDimensions,2), numberOfDimensions);
    }

    @Override
    public double[] getMax() {
        return Commons.fill(Math.pow(numberOfDimensions,2), numberOfDimensions);
    }

    @Override
    public double getExpectedBestValue() {
        return (-numberOfDimensions*(numberOfDimensions+4)*(numberOfDimensions-1))/6;
    }

    @Override
    public double[] getExpectedParameters() {
        double d[] = new double[numberOfDimensions];

        for(int i=1; i<numberOfDimensions; i++){
            d[i] = i*(numberOfDimensions + 1 - i);
        }
        return d;
    }
}
