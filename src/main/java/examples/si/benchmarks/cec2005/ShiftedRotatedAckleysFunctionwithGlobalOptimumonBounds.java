package examples.si.benchmarks.cec2005;

import examples.si.benchmarks.BaseFunctions;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.FileHandler;
import org.usa.soc.util.Randoms;

import java.util.Arrays;

public class ShiftedRotatedAckleysFunctionwithGlobalOptimumonBounds extends ObjectiveFunction {

    private double fBias = -140.0;
    private RealMatrix fShift;
    private RealMatrix fMatrix;

    public ShiftedRotatedAckleysFunctionwithGlobalOptimumonBounds() {}

    @Override
    public ObjectiveFunction updateDimensions(int n){
        this.numberOfDimensions = 30;
        fShift = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2005/data_ackley.txt").read().toRealMatrix().getSubMatrix(0,0, 0, 29);
        fMatrix = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2005/ackley_M_D30.txt").read().toRealMatrix();

        UniformRealDistribution distribution = new UniformRealDistribution(-32, 32);
        for(int i=0; i<numberOfDimensions;i++){
            if(i % 2 == 0){
                fShift.setEntry(0, i, -32);
            }else{
                fShift.setEntry(0, i, distribution.sample());
            }
        }
        return this;
    }

    @Override
    public Double call() {

        RealMatrix rm = getParametersMatrix();
        RealMatrix rw = rm.subtract(fShift).multiply(fMatrix);

        return BaseFunctions.AckleyFunction(rw, fBias, numberOfDimensions);
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
