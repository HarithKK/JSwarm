package examples.si.benchmarks.cec2018;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Mathamatics;

import java.util.Arrays;

public class ModifiedInvertedDTLZ1 extends ObjectiveFunction {

    int M = 1;
    private int numberOfDimensions = 2;

    public ModifiedInvertedDTLZ1(int n, int M){
        this.numberOfDimensions = n;
        this.M = M;
    }

    private double getParam(int i){
        return (double)super.getParameters()[i];
    }

    private double calcG(int m){
        double sum = 0;
        for(int i=m; i < this.numberOfDimensions; i++){
            sum += Math.pow(getParam(i)-0.5, 2);
        }
        return sum + 1;
    }

    private double f1(double gm){
        return getParam(0) * gm;
    }

    private double f2(int p, double gm){
        double value = 1.0;
        for(int i=this.M-p; i>=0; i--){
            value = 1 - getParam(i)*value;
        }
        return value * gm;
    }

    @Override
    public Double call() {
        double GXm = calcG(this.M);
        double value = Double.MAX_VALUE;
        for(int i=1; i<M; i++){
            value = Math.min(value, f2(i, GXm));
        }
        value = Math.min(value, f1(GXm));
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
