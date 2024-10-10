package examples.si.benchmarks.cec2022;

import examples.si.benchmarks.BaseFunctions;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;
import org.usa.soc.util.FileHandler;

public class HybridFunction2 extends ObjectiveFunction {

    private double fBias = 2000.0;
    private RealMatrix fShift;
    private RealMatrix fMatrix;
    private RealMatrix fShuffle;

    int[] n = new int[]{2, 4, 4, 4, 2, 4};
    public HybridFunction2() {}
    @Override
    public ObjectiveFunction updateDimensions(int n){
        this.numberOfDimensions = 20;
        fShift = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2022/shift_data_7.txt").read().toRealMatrix().getSubMatrix(0,0, 0, 19);
        fMatrix = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2022/M_7_D20.txt").read().toRealMatrix();
        fShuffle = new FileHandler("src/main/java/examples/si/benchmarks/cecdata/cec2022/shuffle_data_7_D20.txt").read().toRealMatrix();

        return this;
    }

    @Override
    public Double call() {
        RealMatrix rm = getParametersMatrix();
        RealMatrix rw = rm.subtract(fShift).multiply(fMatrix);

        RealMatrix rn1 = MatrixUtils.createRealMatrix(1,n[0]);
        RealMatrix rn2 = MatrixUtils.createRealMatrix(1,n[1]);
        RealMatrix rn3 = MatrixUtils.createRealMatrix(1,n[2]);
        RealMatrix rn4 = MatrixUtils.createRealMatrix(1,n[3]);
        RealMatrix rn5 = MatrixUtils.createRealMatrix(1,n[4]);

        for(int i=0; i< n[0];i++){
            rn1.addToEntry(0, i, rw.getEntry(0, (int)fShuffle.getEntry(0,i)-1));
        }

        for(int i=0; i< n[1];i++){
            rn2.addToEntry(0, i, rw.getEntry(0, (int)fShuffle.getEntry(0,i+n[0])-1));
        }

        for(int i=0; i< n[2];i++){
            rn3.addToEntry(0, i, rw.getEntry(0, (int)fShuffle.getEntry(0,i+n[0]+n[1])-1));
        }

        for(int i=0; i< n[3];i++){
            rn4.addToEntry(0, i, rw.getEntry(0, (int)fShuffle.getEntry(0,i+n[0]+n[1]+n[2])-1));
        }

        for(int i=0; i< n[4];i++){
            rn5.addToEntry(0, i, rw.getEntry(0, (int)fShuffle.getEntry(0,i+n[0]+n[1]+n[2]+n[3])-1));
        }

        return BaseFunctions.HgBat(rn1, fBias, n[0]) +
                BaseFunctions.Katsuura(rn2, 0, n[1]) +
                BaseFunctions.AckleyFunction(rn3, 0, n[2])+
                BaseFunctions.ModifiedSchwefelFunction(rn3, 0, n[3])+
                BaseFunctions.SchafferFunction(rn3, 0, n[4]);
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
