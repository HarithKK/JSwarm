package org.usa.soc.util;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.stat.inference.TTest;
import org.usa.soc.core.ds.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Commons {
    public static double[] fill(double value, int size){
        double d[] = new double[size];
        Arrays.fill(d, value);
        return d;
    }

    public static double computeWeight(double lambda, double sigma){
        return Math.exp(-sigma) * (Math.pow(1 + Math.exp(-lambda), -1));
    }

    public static int rouletteWheelSelection(double[] probabilityArray){
        double totalSum = Arrays.stream(probabilityArray).sum();
        double rand = Randoms.rand(0, totalSum);
        double cumilativeSum =0;
        int index =0;
        for (double d: probabilityArray) {
            cumilativeSum += d;
            if(cumilativeSum >= rand){
                return index;
            }else{
                index++;
            }
        }
        return -1;
    }

    public static double levyflight(int numberOfDimentions){
        double beta=3/2;
        double sigma = Math.pow((Gamma.gamma(1+beta) * Math.sin(Math.PI * (beta/2))) /
                (Math.pow(Gamma.gamma((1+beta)/2) * beta * 2, ((beta-1)/2))), (1/beta));
        return Math.pow( (Randoms.rand(1, numberOfDimentions) * sigma) / Math.abs(Randoms.rand(1, numberOfDimentions)), (1/beta));
    }

    public static Vector levyFlightVector(int numberOfDimentions, double beta){
        double sigma = Math.pow((Gamma.gamma(1+beta) * Math.sin(Math.PI * (beta/2))) /
                (Math.pow(Gamma.gamma((1+beta)/2) * beta * 2, ((beta-1)/2))), (1/beta));

        Vector v = new Vector(numberOfDimentions);
        for(int i=0; i< numberOfDimentions;i++){
            v.setValue(Math.pow( (Randoms.rand(1, numberOfDimentions) * sigma) / Math.abs(Randoms.rand(1, numberOfDimentions)), (1/beta)), i);
        }
        return v;
    }

    public static Vector levyFlightVector(int D, double[] min, double[] max) {
        Vector v = new Vector(D);
        Vector levy = Commons.levyFlightVector(D, 1.5);

        for(int i=0;i<D;i++){
            v.setValue(min[i] + levy.getValue(i)*(max[i] - min[i]),i);
        }
        return v.fixVector(min, max);
    }

    public static int factorial(int n){
        int f = 1;
        while(n > 0){
            f *= n;
            n = n-1;
        }
        return f;
    }

    public static double[] getRandomPoint(int dimension, double radius) {
        Random random = new Random();

        double[] coordinates = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            coordinates[i] = random.nextGaussian();
        }

        // Normalize the vector to get a point on the unit sphere
        double norm = 0;
        for (double coordinate : coordinates) {
            norm += coordinate * coordinate;
        }
        norm = Math.sqrt(norm);

        for (int i = 0; i < dimension; i++) {
            coordinates[i] /= norm;
        }

        // Scale the point to the desired radius
        for (int i = 0; i < dimension; i++) {
            coordinates[i] *= radius;
        }

        return coordinates;
    }

    public static double calculatePValue(double[] s1, double[] s2){
        TTest tTest = new TTest();
        double d = tTest.tTest(s1, s2);
        return Double.isNaN(d) ? -1 : d;
    }

    public static RealMatrix expm(RealMatrix matrix) {
        int n = matrix.getRowDimension();
        RealMatrix result = MatrixUtils.createRealIdentityMatrix(n);
        RealMatrix power = matrix;

        for (int i = 1; i < 10; i++) { // Adjust the number of iterations for accuracy
            result = result.add(power.scalarMultiply(1.0 / factorial(i)));
            power = power.multiply(matrix);
        }

        return result;
    }

    public static RealMatrix expm(RealMatrix matrix, double t, int e) {
        int n = matrix.getRowDimension();
        RealMatrix result = MatrixUtils.createRealIdentityMatrix(n);
        RealMatrix power = matrix;

        for (int i = 1; i < e; i++) {
            result = result.add(power.scalarMultiply(Math.pow(t,i) / factorial(i)));
            power = Commons.hadamardProduct(power, matrix);
        }

        return result;
    }

    private static RealMatrix hadamardProduct(RealMatrix p1, RealMatrix p2) {
        RealMatrix hm = MatrixUtils.createRealMatrix(p1.getRowDimension(), p1.getColumnDimension());
        for(int i=0; i< p1.getRowDimension();i++){
            for(int j=0; j< p1.getRowDimension();j++){
                hm.setEntry(i,j, p1.getEntry(i,j)* p2.getEntry(i,j));
            }
        }
        return hm;
    }
}
