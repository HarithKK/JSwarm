package examples.si.benchmarks.cec2018;

import org.apache.commons.math3.linear.RealVector;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.ArrayList;
import java.util.List;

public class MIDTLZ1 extends ObjectiveFunction {

    DTLZ dtlz;
    List<List<Double>> paretoHistory = new ArrayList<>();

    public MIDTLZ1(int K, int M){
        dtlz = new DTLZ(K, M, true);
    }

    public MIDTLZ1(){
        dtlz = new DTLZ(10, 10, true);
    }

    @Override
    public Double call() {
        RealVector rw = getParametersVector();
        RealVector X = rw.getSubVector(0, dtlz.M);
        RealVector XM = rw.getSubVector(dtlz.M, numberOfDimensions - dtlz.M);

        double g = dtlz.g2(XM);

        List<Double> fs = new ArrayList<>();

        for(int i=0; i<dtlz.M; i++){
            if(i > 0){
                fs.add(dtlz.calculateProduct(X.getSubVector(0, dtlz.M+1-i)) * 0.5 * (1 + g));
            }else{
                fs.add(X.getEntry(0)* 0.5 * (1 + g));
            }
        }

        paretoHistory.add(fs);

        return fs.stream().mapToDouble(d->d).min().getAsDouble();
    }

    public DTLZ getDtlz(){
        return dtlz;
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
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(1, numberOfDimensions);
    }

    public List<Double> getParetoFront(int i){
        return paretoHistory.get(i);
    }
}
