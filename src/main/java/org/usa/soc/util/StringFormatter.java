package org.usa.soc.util;

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
}
