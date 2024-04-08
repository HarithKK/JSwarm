package org.usa.soc.si.algo.choa;

public class Chaotics {
    public enum type { QUADRATIC, GAUSS_MOUSE, LOGISTIC, SINGER, BERNOULLI, TENT };

    public static double getChaoticValue(type type,double x){
        switch (type){
            case QUADRATIC: return Math.pow(x,2) - 1;
            case GAUSS_MOUSE: return x == 0 ? 1: x % 1;
            case LOGISTIC: return 4*x*(1-x);
            case SINGER: return 1.07*(7.86*x - 23.31*Math.pow(x,2) +28.75*Math.pow(x,3) -13.302875*Math.pow(x,4));
            case BERNOULLI: return (2*x) % 1;
            case TENT: return x < 0.7 ? x/0.7 : (10/3)*(1-x);
            default: return 1;
        }
    }
}
