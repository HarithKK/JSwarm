package org.usa.soc.util;

import org.apache.commons.math3.analysis.function.Abs;
import org.apache.commons.math3.util.Precision;
import org.usa.soc.core.ds.Vector;

public class Mathamatics {
    public static Double absRound(Double value, int r){
        return new Abs().value(Precision.round(value, r));
    }

    public static Double round(Double value, int r){
        return Precision.round(value, r);
    }

    public static Double[] getCenterPoint(int D, double[] min, double[] max){
        Double []indexes = new Double[D];
        for(int i=0;i<D;i++){
            indexes[i] = Math.abs(max[i] - min[i])/2;
        }
        return indexes;
    }

    public static String getErrorPercentage(double r, double range) {
        range = Mathamatics.round(range,1);
        if(range != 0){
            r =  ( 4 * r ) / range;
            r = ((2 * range) / (1 + Math.exp(-r)));
            r = r - range;
        }
        return Mathamatics.round(r,2) + " %";
    }

    public static int calculateHammingDistance(double d1, double d2){
        double d = Double.longBitsToDouble(Double.doubleToRawLongBits(d1) ^ Double.doubleToRawLongBits(d2));
        String s = Long.toBinaryString(Double.doubleToRawLongBits(d));
        int count =0;
        for(char c : s.toCharArray()){
            if(c=='1')
                count++;
        }
        return count;
    }

    public static double getMinimumDimensionDistance(double []min, double []max, int D){
        double minD = Double.MAX_VALUE;
        for(int i=0; i< D;i++){
            minD = Math.min(minD, max[i] - min[i]);
        }
        return minD;
    }

    public static double getMaximumDimensionDistance(double []min, double []max, int D){
        double maxD = Double.MIN_VALUE;
        for(int i=0; i< D;i++){
            maxD = Math.max(maxD, max[i] - min[i]);
        }
        return maxD;
    }

    public static double getEuclideanNorm(Vector v){
        double sum =0;
        for (double d: v.getPositionIndexes()) {
            sum += Math.pow(d,2);
        }
        return Math.sqrt(sum);
    }

    public static Vector getLevyVector(int t, int D, double min, double max){
        Vector v = new Vector(D);

        for(int i=0; i< D; i++){
            v.setValue(min < 1 ? t : Math.pow(t, Randoms.rand(min, max)), i);
        }
        return v;
    }

    public static double pow(double a, double b){
        return new org.apache.commons.math3.analysis.function.Power(a).value(b);
    }
}
