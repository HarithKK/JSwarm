package examples.si.benchmarks.cec2022;

import examples.si.benchmarks.BaseFunctions;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.FileHandler;

public class HybridFunction1 extends ObjectiveFunction {

    private double fBias = 1800.0;
    private RealMatrix fShift;
    private RealMatrix fMatrix;
    private RealMatrix fShuffle;

    int n1 = 8, n2=8, n3=4;
    public HybridFunction1() {}
    @Override
    public ObjectiveFunction updateDimensions(int n){
        this.numberOfDimensions = 20;
        fShift = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2022/shift_data_6.txt").read().toRealMatrix().getSubMatrix(0,0, 0, 19);
        fMatrix = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2022/M_6_D20.txt").read().toRealMatrix();
        fShuffle = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2022/shuffle_data_6_D20.txt").read().toRealMatrix();

        return this;
    }

    @Override
    public Double call() {
        RealMatrix rm = getParametersMatrix();
        RealMatrix rw = rm.subtract(fShift).multiply(fMatrix);

        RealMatrix rn1 = MatrixUtils.createRealMatrix(1,n1);
        RealMatrix rn2 = MatrixUtils.createRealMatrix(1,n2);
        RealMatrix rn3 = MatrixUtils.createRealMatrix(1,n3);

        for(int i=0; i< n1;i++){
            rn1.addToEntry(0, i, rw.getEntry(0, (int)fShuffle.getEntry(0,i)-1));
        }

        for(int i=0; i< n2;i++){
            rn2.addToEntry(0, i, rw.getEntry(0, (int)fShuffle.getEntry(0,i+n1)-1));
        }

        for(int i=0; i< n3;i++){
            rn3.addToEntry(0, i, rw.getEntry(0, (int)fShuffle.getEntry(0,i+n1+n2)-1));
        }

        return BaseFunctions.BentCigar(rn1, fBias, n1) +
                BaseFunctions.HgBat(rn2, fBias, n2) +
                BaseFunctions.RastriginsFunction(rn3, fBias, n3);
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
