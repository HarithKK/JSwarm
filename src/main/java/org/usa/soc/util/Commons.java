package org.usa.soc.util;

import org.apache.commons.math3.special.Gamma;
import java.util.Arrays;

public class Commons {
    public static double[] fill(double value, int size){
        double d[] = new double[size];
        for (int i=0;i< size;i++) {
            d[i] = value;
        }
        return d;
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
}
