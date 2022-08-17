package org.usa.soc.util;

import org.usa.soc.core.Vector;

import java.util.Random;

public class Randoms {
    static double precision = 1000D;
    public static double rand (double min, double max) {
        Random randomValue = new java.util.Random();
        double number = randomValue.nextInt((int) ((max - min) * precision + 1)) + min * precision;
        return number / precision;
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
        return v;
    }

    public static Vector getRandomVector(int D, double[] min, double[] max, double rMin, double rMax) {
        Vector v = new Vector(D);
        for(int i=0;i<D;i++){
            v.setValue((min[i] + rand(rMin, rMax)*(max[i]-min[i])),i);
        }
        return v;
    }

    public static Vector getRandomVector(int D, double min, double max) {
        Vector v = new Vector(D);
        for(int i=0;i<D;i++){
            v.setValue(rand(min, max),i);
        }
        return v;
    }

    public static int rand (int bound) {
        Random randomValue = new java.util.Random();
        return randomValue.nextInt(bound);
    }
}
