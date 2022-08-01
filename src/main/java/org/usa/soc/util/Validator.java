package org.usa.soc.util;

public class Validator {

    public static void checkBoundaries(double min, double max){
        if(min > max){
            throw new IllegalArgumentException("Validator: Boundaries Mismatched!");
        }
    }

    public static boolean validateBestValue(Double fpbest, Double fgbest, boolean isMin){
        return (isMin && fpbest.compareTo(fgbest) < 0 ) || (!isMin && fpbest.compareTo(fgbest) > 0);
    }

    public static void checkBoundaries(double[] minBoundary, double[] maxBoundary, int numberOfDimensions) {

        if(minBoundary.length != numberOfDimensions || maxBoundary.length != numberOfDimensions){
            throw new IllegalArgumentException("Minimum/Maximum Boundaries should have " + numberOfDimensions + " Elements");
        }

        for(int i=0; i< minBoundary.length;i++){
            if(minBoundary[i] > maxBoundary[i]){
                throw new IllegalArgumentException("Validator: Boundaries Mismatched!");
            }
        }

    }

    public static double validatePosition(double min, double max, double val){
        if(val>max){
            return max;
        } else if (val<min) {
            return min;
        }else{
            return val;
        }
    }

    public static void checkMinMax(Double wMax, Double wMin) {
        if(wMax < wMin)
            throw new IllegalArgumentException("Validator: Min Max Mismatched!");
    }
}
