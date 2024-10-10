package examples.si.benchmarks.cec2017;

import examples.si.benchmarks.BaseFunctions;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.FileHandler;

import java.util.Arrays;

public class ShiftedandRotatedZakharovFunction extends ObjectiveFunction {

    private double fBias = 200.0;
    private RealMatrix fShift;
    private RealMatrix fMatrix;

    public ShiftedandRotatedZakharovFunction() {}

    @Override
    public ObjectiveFunction updateDimensions(int n){
        this.numberOfDimensions = 30;
        fShift = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2017/shift_data_2.txt").read().toRealMatrix().getSubMatrix(0,0, 0, 29);
        fMatrix = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2017/M_2_D20.txt").read().toRealMatrix();

        return this;
    }

    @Override
    public Double call() {
        RealMatrix rm = getParametersMatrix();
        RealMatrix rw = rm.subtract(fShift).multiply(fMatrix);

        return BaseFunctions.ZakharovFunction(rw, fBias, numberOfDimensions);
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
        return fBias;
    }

    @Override
    public double[] getExpectedParameters() {
        return new double[]{fShift.getEntry(0,0), fShift.getEntry(0,1)};
    }
}
