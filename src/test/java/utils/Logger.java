package utils;

import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.StringFormatter;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Logger {
    private static final String DL  = " \t ";
    static DecimalFormat df = new DecimalFormat("#");

    public static void showFunction(SIAlgorithm al){
        if(al == null){
            return;
        }
        double duration = al.getNanoDuration() / 1000000;
        double ep= Math.abs(al.getBestDoubleValue() - al.getFunction().getExpectedBestValue());
        df.setMaximumFractionDigits(8);
        System.out.println(
                al.getClass().getSimpleName() + DL+
                        StringFormatter.format(al.getFunction().getClass().getSimpleName(), 23)+ DL+
                        StringFormatter.format(df.format(al.getBestDoubleValue()), 10) + DL+
                        StringFormatter.format(String.valueOf(Mathamatics.round(al.getFunction().getExpectedBestValue(),4)), 10) + DL+
                        StringFormatter.format(Mathamatics.getErrorPercentage(ep, 100), 25) + DL +
                        StringFormatter.format(Arrays.toString(al.getFunction().getMin()), 30)+ DL+
                        StringFormatter.format(Arrays.toString(al.getFunction().getMax()), 30) + DL+
                        StringFormatter.format(String.valueOf(al.getFunction().getNumberOfDimensions()),5) + DL+
                        StringFormatter.format(String.valueOf(duration), 10) +DL+
                        StringFormatter.format(al.getBestVariables(),80) + DL
        );
    }
}
