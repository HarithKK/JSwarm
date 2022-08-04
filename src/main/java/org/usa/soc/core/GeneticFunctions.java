package org.usa.soc.core;

import org.usa.soc.util.Randoms;

import java.math.BigInteger;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

public class GeneticFunctions {

    private double chromasone1, chromasone2, chromasone3;

    public GeneticFunctions(double chromasone1, double chromasone2) {
        this.chromasone1 = chromasone1;
        this.chromasone2 = chromasone2;
    }

    public GeneticFunctions crossover(){

        String c1 = Long.toBinaryString(Double.doubleToRawLongBits(this.chromasone1));
        String c2 = Long.toBinaryString(Double.doubleToRawLongBits(this.chromasone2));

        int maxLength = Math.max(c1.length(), c2.length());
        int crossOverPoint = Randoms.rand(maxLength);

        StringBuilder sc1 = new StringBuilder(fixLength(c1, maxLength));
        StringBuilder sc2 = new StringBuilder(fixLength(c2, maxLength));

        for(int i=0; i< crossOverPoint; i++){
            char c = sc1.charAt(i);
            sc1.setCharAt(i, sc2.charAt(i));
            sc2.setCharAt(i,c);
        }

        this.chromasone1 = convertBinaryArrayToDouble(sc1.toString());
        this.chromasone2 = convertBinaryArrayToDouble(sc2.toString());

        return this;
    }

    public GeneticFunctions mutate(double probability, int numOfMutations){

        if(Randoms.rand(0, 1) < probability){
            return this;
        }

        String c1 = Long.toBinaryString(Double.doubleToRawLongBits(this.chromasone1));
        String c2 = Long.toBinaryString(Double.doubleToRawLongBits(this.chromasone2));

        int maxLength = Math.max(c1.length(), c2.length());

        StringBuilder sc1 = new StringBuilder(fixLength(c1, maxLength));
        StringBuilder sc2 = new StringBuilder(fixLength(c2, maxLength));

        for(int i=0; i< numOfMutations; i++){
            int mutationPoint = Randoms.rand(maxLength);

            mutate(sc1, mutationPoint);
            mutate(sc2, mutationPoint);

        }

        this.chromasone1 = convertBinaryArrayToDouble(sc1.toString());
        this.chromasone2 = convertBinaryArrayToDouble(sc2.toString());

        return this;
    }

    public double getFittestValue(boolean isMinimum){
        if(isMinimum){
            return Math.min(this.chromasone1, this.chromasone2);
        }else{
            return Math.max(this.chromasone1, this.chromasone2);
        }
    }

    private void mutate(StringBuilder sc1, int mutationPoint) {
        if(sc1.charAt(mutationPoint) == '1'){
            sc1.setCharAt(mutationPoint, '0');
        }else{
            sc1.setCharAt(mutationPoint, '1');
        }
    }

    private String fixLength(String s, int count){
        if(count < s.length())
            return s;
        return generate(() -> "0").limit(count - s.length()).collect(joining()) + s;
    }

    private double convertBinaryArrayToDouble(String s){
        return Double.longBitsToDouble(new BigInteger(s, 2).longValue());
    }

    public static void main(String[] args) {
        GeneticFunctions g = new GeneticFunctions(4,3);
        g.crossover().mutate(0.5,1);
    }

}
