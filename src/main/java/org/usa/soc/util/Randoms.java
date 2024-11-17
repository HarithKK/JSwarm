package org.usa.soc.util;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.usa.soc.core.ds.Vector;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Randoms {
    static double precision = 1000D;
    public static double rand (double min, double max) {
        if(max == min)
            return max;
        return ThreadLocalRandom.current().nextDouble(min, max+1);
    }
    public static int rand (int min, int max) {
        if(max == min)
            return max;
        return ThreadLocalRandom.current().nextInt(min, max+1);
    }

    public static double rand (){
        return new UniformRealDistribution(0,1).sample();
    }

    public static double randLBmax (double min, double max) {
        if(max == min)
            return max;
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static double randAny (double f1, double f2) {

        if(Double.isNaN(f1) || Double.isNaN(f2)){
            return rand(0,1);
        }
        else if(Double.isInfinite(f1) && Double.isInfinite(f2)){
            return rand(Double.MIN_VALUE,Double.MAX_VALUE);
        }
        else if(Double.isInfinite(f1)){
            return rand(Double.MIN_VALUE,f2);
        }
        else if(Double.isInfinite(f2)){
            return rand(f1,Double.MAX_VALUE);
        }
        else if(f1 < f2){
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
            UniformRealDistribution ur = new UniformRealDistribution(min[i], max[i]);
            v.setValue(ur.sample()+min[i],i);
        }
        return v.fixVector(min,max);
    }

    public static Vector getRandomVector(int D, double radius) {
        Vector v = new Vector(D);
        double[] d = Commons.getRandomPoint(D, radius);
        for(int i=0;i<D;i++){
            v.setValue(d[i],i);
        }
        return v;
    }

    public static Vector getRandomVector(int D, double[] min, double[] max, double rMin, double rMax) {
        Vector v = new Vector(D);
        UniformRealDistribution ur = new UniformRealDistribution(rMin, rMax);
        for(int i=0;i<D;i++){
            v.setValue((min[i] + ur.sample()*(max[i]-min[i])),i);
        }
        return v.fixVector(min,max);
    }

    public static Vector getRandomGaussianVector(int D, double[] min, double[] max, double mean, double std) {
        Vector v = new Vector(D);
        NormalDistribution nd = new NormalDistribution(mean, std);
        for(int i=0;i<D;i++){
            v.setValue((min[i] + nd.sample()*(max[i]-min[i])),i);
        }
        return v.fixVector(min,max);
    }

    public static Vector getRandomGaussianVector(int D, double min, double max, double mean, double std) {
        Vector v = new Vector(D);
        NormalDistribution nd = new NormalDistribution(mean, std);
        for(int i=0;i<D;i++){
            v.setValue((min + nd.sample()*(max-min)),i);
        }
        return v;
    }

    public static Vector getNormalRandomVector(int D, double[] min, double[] max, double m, double n) {
        Vector v = new Vector(D);
        for(int i=0;i<D;i++){
            v.setValue((min[i] + Randoms.rand(m,n)*(max[i]-min[i])),i);
        }
        return v.fixVector(min,max);
    }

    public static Vector getRandomVector(int D, double min, double max) {
        Vector v = new Vector(D);
        UniformRealDistribution ur = new UniformRealDistribution(min, max);
        for(int i=0;i<D;i++){
            v.setValue(ur.sample(),i);
        }
        return v;
    }

    public static Vector getRandomVector(Vector s, double r) {
        Vector v = new Vector(s.getNumberOfDimensions());
        for(int i=0;i<s.getNumberOfDimensions();i++){
            double val = v.getValue(i);
            UniformRealDistribution ur = new UniformRealDistribution(val-r, val+r);
            v.setValue(ur.sample(),i);
        }
        return v;
    }

    public static Vector getRandomVector(Vector s, Vector r) {
        Vector v = new Vector(s.getNumberOfDimensions());
        for(int i=0;i<s.getNumberOfDimensions();i++){
            double val = v.getValue(i);
            UniformRealDistribution ur = new UniformRealDistribution(val-r.getValue(i), val+r.getValue(i));
            v.setValue(ur.sample(),i);
        }
        return v;
    }

    public static int rand (int bound) {
        Random randomValue = new java.util.Random();
        return randomValue.nextInt(bound);
    }
}
