package examples.si.benchmarks.nonGeneral.classical.multimodal.separable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class Michalewicz10 extends ObjectiveFunction {

    public Michalewicz10(){
        setFixedDimentions(10);
    }

    @Override
    public Double call() {
        Double sum = 0.0;
        for(int i=0; i<numberOfDimensions;i++){
            Double x = (Double)getParameters()[i];
            sum += Math.sin(x)*Math.pow(Math.sin(x * x * i / Math.PI), 20);
        }
        return -sum;
    }

    @Override
    public int getNumberOfDimensions() {
        return 10;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(0, numberOfDimensions);
    }

    @Override
    public double[] getMax() {
        return Commons.fill(Math.PI, numberOfDimensions);
    }

    @Override
    public double getExpectedBestValue() {
        return -9.6602;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(2.27,numberOfDimensions);
    }
}
