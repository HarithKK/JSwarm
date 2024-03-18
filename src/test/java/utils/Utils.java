package utils;

import java.util.Arrays;

public class Utils {

    public static double[] fill (int n, double val){
        double[] d = new double[n];
        Arrays.fill(d, val);
        return d;
    }
}
