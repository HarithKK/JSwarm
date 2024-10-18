package org.usa.soc.util;

import org.apache.commons.math3.linear.RealMatrix;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

public class StringFormatter {

    public static String toString(Double p[]){
        StringBuilder sb = new StringBuilder();
        int i=1;
        for (double d: p) {
            sb.append("[").append(i++).append("] : ").append(format(String.valueOf(d), 12)).append("\t");
        }
        return sb.toString();
    }

    public static String toString(double p[]){
        StringBuilder sb = new StringBuilder();
        int i=1;
        for (double d: p) {
            sb.append("[").append(i++).append("] : ").append(d).append("\n");
        }
        return sb.toString();
    }

    public static String format(String i, int routes){
        String s = i + generate(() -> " ").limit(routes).collect(joining());
        return s.substring(0,routes);
    }

    public static String toString(RealMatrix matrix){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<matrix.getRowDimension(); i++){
            sb.append("|");
            for(int j=0; j<matrix.getColumnDimension(); j++){
                sb.append(matrix.getEntry(i,j));
                sb.append(",\t");
            }
            sb.replace(sb.length()-1, sb.length(), "|\n");
        }
        return sb.toString();
    }
}
