package org.usa.soc.util;

public class Validator {

    public static void checkBoundaries(double min, double max){
        if(min > max){
            throw new IllegalArgumentException("Validator: Boundaries Mismatched!");
        }
    }

    public static void checkBoundaries(double val, double min, double max){
        if(val > max || val < min){
            throw new IllegalArgumentException("Validator: Boundaries Mismatched!");
        }
    }

    public static boolean validateBestValue(Double comparatee, Double comparator, boolean isMin){
        return (isMin && comparatee.compareTo(comparator) < 0 ) || (!isMin && comparatee.compareTo(comparator) > 0);
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

    public static void checkPopulationSize(int actual, int expected) {
        if(actual < expected)
            throw new IllegalArgumentException("Validator: Population size is expected ["+expected+"]");
    }

    public static boolean validateRangeInOneAndZero(double seekersToTracersRatio) {
        if(seekersToTracersRatio > 1 || seekersToTracersRatio <0){
            return false;
        }
        return true;
    }
}
