package utils;

import org.usa.soc.intefaces.IAlgorithm;
import org.usa.soc.util.StringFormatter;

import java.util.Arrays;

public class Logger {
    private static final String DL  = " \t ";
    public static void showFunction(IAlgorithm al){
        if(al == null){
            return;
        }
        double duration = al.getNanoDuration() / 1000000;
        System.out.println(
                al.getClass().getSimpleName() + DL+
                StringFormatter.format(al.getFunction().getClass().getSimpleName(), 23)+ DL+
                StringFormatter.format(al.getBestValue(), 20) + DL+
                StringFormatter.format(String.valueOf(al.getFunction().getExpectedBestValue()), 10) + DL+
                StringFormatter.format(al.getErrorPercentage(), 25) + DL +
                StringFormatter.format(Arrays.toString(al.getFunction().getMin()), 30)+ DL+
                StringFormatter.format(Arrays.toString(al.getFunction().getMax()), 30) + DL+
                StringFormatter.format(String.valueOf(al.getFunction().getNumberOfDimensions()),5) + DL+
                StringFormatter.format(String.valueOf(duration), 10) +DL+
                StringFormatter.format(al.getBestVariables(),80) + DL +
                StringFormatter.toString(al.getFunction().getExpectedParameters())
        );
    }
}
