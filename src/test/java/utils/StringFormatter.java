package utils;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

public class StringFormatter {

    public static String format(String i, int routes){
        String s = i + generate(() -> " ").limit(routes).collect(joining());
        return s.substring(0,routes);
    }
}
