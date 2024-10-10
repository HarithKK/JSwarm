package examples.si.benchmarks.nonGeneral.classical.multimodal.separable;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class Michalewicz5 extends ObjectiveFunction {

    public Michalewicz5(){
        setFixedDimentions(5);
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
        return numberOfDimensions;
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
        return -4.687658;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{2.27,1.57};
    }
}
