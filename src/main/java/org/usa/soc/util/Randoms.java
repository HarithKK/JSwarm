package org.usa.soc.util;

import org.usa.soc.core.Vector;

import java.util.Random;

public class Randoms {
    static double precision = 1000000D;
    public static double rand (double min, double max) {
        Random randomValue = new java.util.Random();

        double number = randomValue.nextInt((int) ((max - min) * precision + 1)) + min * precision;
        return number / precision;
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
