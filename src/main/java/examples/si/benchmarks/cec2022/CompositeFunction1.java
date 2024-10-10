package examples.si.benchmarks.cec2022;

import examples.si.benchmarks.BaseFunctions;
import examples.si.benchmarks.WeightCalculator;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.FileHandler;

public class CompositeFunction1 extends ObjectiveFunction {

    private double fBias = 2300;
    private RealMatrix fShift;
    private RealMatrix fMatrix;

    double[] lambdas = new double[]{1, 1e-6, 1e-6, 1e-6, 1e-6};
    double[] bias = new double[]{0, 200, 300, 100, 400};
    double[] rhos = new double[]{10, 20, 30, 40, 50};

    public CompositeFunction1() {}
    @Override
    public ObjectiveFunction updateDimensions(int n){
        this.numberOfDimensions = 20;
        fShift = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2022/shift_data_9.txt").read().toRealMatrix().getSubMatrix(0,0, 0, 19);
        fMatrix = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2022/M_9_D20.txt").read().toRealMatrix();

        return this;
    }

    @Override
    public Double call() {
        RealMatrix rm = getParametersMatrix();

        RealMatrix rw0 = rm.subtract(fShift)
                        .scalarMultiply(0.02048).multiply(fMatrix.getSubMatrix(0, numberOfDimensions-1, 0, numberOfDimensions-1));
        double g0 = lambdas[0] * BaseFunctions.RosenbrocksFunction(rw0, bias[0], numberOfDimensions);
        double w0 = WeightCalculator.calculate(rm.subtract(fShift), rhos[0], numberOfDimensions);

        RealMatrix rw1 = rm.subtract(fShift).multiply(fMatrix.getSubMatrix(numberOfDimensions, 2*numberOfDimensions-1, 0, numberOfDimensions-1));
        double g1 = lambdas[1] * BaseFunctions.EllipticFunction(rw1, bias[1], numberOfDimensions);
        double w1 = WeightCalculator.calculate(rm.subtract(fShift), rhos[1], numberOfDimensions);

        RealMatrix rw2 = rm.subtract(fShift).multiply(fMatrix.getSubMatrix(2*numberOfDimensions, 3*numberOfDimensions-1, 0, numberOfDimensions-1));
        double g2 = lambdas[2] * BaseFunctions.BentCigar(rw2, bias[2], numberOfDimensions);
        double w2 = WeightCalculator.calculate(rm.subtract(fShift), rhos[2], numberOfDimensions);

        RealMatrix rw3 = rm.subtract(fShift).multiply(fMatrix.getSubMatrix(3*numberOfDimensions, 4*numberOfDimensions-1, 0, numberOfDimensions-1));
        double g3 = lambdas[3] * BaseFunctions.DiscusFunction(rw3, bias[3]);
        double w3 = WeightCalculator.calculate(rm.subtract(fShift), rhos[3], numberOfDimensions);

        RealMatrix rw4 = rm.subtract(fShift).multiply(fMatrix.getSubMatrix(4*numberOfDimensions, 5*numberOfDimensions-1, 0, numberOfDimensions-1));
        double g4 = lambdas[4] * BaseFunctions.EllipticFunction(rw3, bias[4], numberOfDimensions);
        double w4 = WeightCalculator.calculate(rm.subtract(fShift), rhos[4], numberOfDimensions);

        double totalW = w0 + w1 + w2 + w3 + w4;

        RealVector ws = new ArrayRealVector(new double[]{w0/totalW, w1/totalW, w2/totalW, w3/totalW, w4/totalW});
        RealVector gs = new ArrayRealVector(new double[]{g0, g1, g2, g3, g4});

        return ws.dotProduct(gs) + fBias;
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
