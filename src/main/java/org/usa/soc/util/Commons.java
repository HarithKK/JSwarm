package org.usa.soc.util;

public class Commons {
    public static double[] fill(double value, int size){
        double d[] = new double[size];
        for (int i=0;i< size;i++) {
            d[i] = value;
        }
        return d;
    }
}
