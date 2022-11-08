package org.usa.soc.util;

import org.usa.soc.core.Vector;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Randoms {
    static double precision = 1000D;
    public static double rand (double min, double max) {
        if(max == min)
            return max;
        return ThreadLocalRandom.current().nextDouble(min, max+1);
    }

    public static double randAny (double f1, double f2) {

        if(Double.isNaN(f1) || Double.isNaN(f2)){
            return rand(0,1);
        }else if(f1 < f2){
            return rand(f1, f2);
        }else if (f1 > f2){
            return rand(f2, f1);
        }else{
            return rand(f1, f1+1);
        }
    }

    public static double getDoubleRand () {
        return new java.util.Random().nextDouble();
    }

    public static Vector getRandomVector(int D, double[] min, double[] max) {
        Vector v = new Vector(D);
        for(int i=0;i<D;i++){
            v.setValue(rand(min[i], max[i]),i);
        }
        return v.fixVector(min,max);;
    }

    public static Vector getRandomVector(int D, double[] min, double[] max, double rMin, double rMax) {
        Vector v = new Vector(D);
        for(int i=0;i<D;i++){
            v.setValue((min[i] + rand(rMin, rMax)*(max[i]-min[i])),i);
        }
        return v.fixVector(min,max);
    }

    public static Vector getRandomVector(int D, double min, double max) {
        Vector v = new Vector(D);
        for(int i=0;i<D;i++){
            v.setValue(rand(min, max),i);
        }
        return v;
    }

    public static Vector getRandomUniVector(double min, double max) {
        Vector v = new Vector(1);
        v.setValue(rand(min, max),0);
        return v;
    }

    public static Vector getRandomVector(Vector s, double r) {
        Vector v = new Vector(s.getNumberOfDimensions());
        for(int i=0;i<s.getNumberOfDimensions();i++){
            double val = v.getValue(i);
            v.setValue(rand(val-r, val+r),i);
        }
        return v;
    }

    public static Vector getRandomVector(Vector s, Vector r) {
        Vector v = new Vector(s.getNumberOfDimensions());
        for(int i=0;i<s.getNumberOfDimensions();i++){
            double val = v.getValue(i);
            v.setValue(rand(val-r.getValue(i), val+r.getValue(i)),i);
        }
        return v;
    }

    public static int rand (int bound) {
        Random randomValue = new java.util.Random();
        return randomValue.nextInt(bound);
    }
}
