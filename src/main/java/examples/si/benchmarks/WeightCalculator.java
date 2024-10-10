package examples.si.benchmarks;

import org.apache.commons.math3.linear.RealMatrix;

public class WeightCalculator {

    public static double calculate(RealMatrix rm, double rho, int n){

        double sum = 0;

        for(int i=0; i<n ;i++){
            sum += Math.pow(rm.getEntry(0, i),2);
        }

        return Math.exp(-(sum / (2 * n * rho * rho)))/sum;
    }
}
