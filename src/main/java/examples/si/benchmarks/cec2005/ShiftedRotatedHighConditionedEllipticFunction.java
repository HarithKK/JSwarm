package examples.si.benchmarks.cec2005;

import examples.si.benchmarks.BaseFunctions;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.FileHandler;

import java.math.BigDecimal;
import java.util.List;

public class ShiftedRotatedHighConditionedEllipticFunction extends ObjectiveFunction {

    private double fBias = -450.0;
    private RealMatrix fShift;

    public ShiftedRotatedHighConditionedEllipticFunction() {

    }

    @Override
    public ObjectiveFunction updateDimensions(int n){
        if(n <= 100 && n >= 2){
            numberOfDimensions = n;
        }
        fShift = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2005/data_high_cond_elliptic_rot.txt").read().toRealMatrix();
        return this;
    }

    @Override
    public Double call() {
        RealMatrix rm = getParametersMatrix();
        RealMatrix rw = rm.subtract(fShift);

        return BaseFunctions.EllipticFunction(rw, fBias, numberOfDimensions);
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
