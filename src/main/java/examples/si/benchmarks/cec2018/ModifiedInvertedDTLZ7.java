package examples.si.benchmarks.cec2018;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class ModifiedInvertedDTLZ7 extends ObjectiveFunction {

    int M = 1;
    private int numberOfDimensions = 2;

    public ModifiedInvertedDTLZ7(int n, int M){
        this.numberOfDimensions = n;
        this.M = M;
    }

    private double getParam(int i){
        return (double)super.getParameters()[i];
    }

    private double calcG(){
        double sum = 0;
        for(int i=M; i < this.numberOfDimensions; i++){
            sum += getParam(i);
        }
        return (sum * 9 / (numberOfDimensions-M)) + 1;
    }

    @Override
    public Double call() {
        double GXm = calcG();
        double value = Double.MAX_VALUE;
        double hValue = 0.0;
        for(int i=0; i<M; i++){
            hValue = (Math.sin(3*Math.PI*getParam(i)) + 1) * getParam(i)/(1+GXm);
            value = Math.min(value, getParam(i));
        }
        value = Math.min(value, M - hValue);
        return value;
    }

    @Override
    public int getNumberOfDimensions() {
        return this.numberOfDimensions;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(0, numberOfDimensions);
    }

    @Override
    public double[] getMax() {
        return Commons.fill(1, numberOfDimensions);
    }

    @Override
    public double getExpectedBestValue() {
        return 0.0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(1, numberOfDimensions);
    }
}
