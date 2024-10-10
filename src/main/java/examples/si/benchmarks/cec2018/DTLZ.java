package examples.si.benchmarks.cec2018;

import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;

public class DTLZ {
    public int D, M, K;

    public DTLZ(int K, int M, boolean is_k){
        if(!is_k){
            this.D = K;
            this.M = M;
            this.K = this.D - M +1;
        }else{
            this.M = M;
            this.K = K;
            this.D = K + M -1;
        }
    }

    public double g2(RealVector data){
        return Arrays.stream(data.toArray()).map(d-> Math.pow(d - 0.2, 2)).sum();
    }

    public double g1(RealVector data){
        return 100*(K + Arrays.stream(data.toArray()).map(d-> Math.pow(d,2) - Math.cos(20 * Math.PI * (d - 0.5))).sum());
    }
    public double g3(RealVector data){
        return Arrays.stream(data.toArray()).map(d-> Math.pow(d/2 - 0.25, 2)).sum();
    }

    public double calculateProduct(RealVector data){
        double product = 1;
        for(int i=0; i<data.getDimension(); i++){
            product *= (1 - data.getEntry(i));
        }
        return product;
    }

    public double calculateCosSinProduct(RealVector data){
        double product = 1;
        for(int i=0; i<data.getDimension()-1; i++){
            product *= Math.cos(data.getEntry(i));
        }
        return product * Math.sin(data.getEntry(data.getDimension()-1));
    }

    public double calculateCosSinProduct(RealVector data, double alpha){
        double product = 1;
        for(int i=0; i<data.getDimension()-1; i++){
            product *= Math.cos(Math.pow(data.getEntry(i), alpha)*1.570);
        }
        return product * Math.sin(Math.pow(data.getEntry(data.getDimension()-1), alpha)*1.570);
    }

}
