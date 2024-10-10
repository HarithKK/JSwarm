package examples.si.benchmarks.cec2005;

import examples.si.benchmarks.BaseFunctions;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.pdfbox.util.Matrix;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.FileHandler;

import java.util.List;

public class ShiftedRotatedGriewanksFunctionwithoutBounds extends ObjectiveFunction {

    private double fBias = 390.0;
    private RealMatrix fShift;
    private RealMatrix fMatrix;

    public ShiftedRotatedGriewanksFunctionwithoutBounds() {}

    @Override
    public ObjectiveFunction updateDimensions(int n){
        this.numberOfDimensions = 30;
        fShift = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2005/data_griewank.txt").read().toRealMatrix().getSubMatrix(0,0, 0, 29);
        fMatrix = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2005/griewank_M_D30.txt").read().toRealMatrix();

        return this;
    }

    @Override
    public Double call() {

        RealMatrix rm = getParametersMatrix();
        RealMatrix rw = rm.subtract(fShift).multiply(fMatrix);

        return BaseFunctions.GriewanksFunction(rw, fBias, numberOfDimensions);
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
