package org.usa.soc.util;

import org.apache.commons.math3.analysis.function.Abs;
import org.apache.commons.math3.util.Precision;
import org.usa.soc.ObjectiveFunction;

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
}
