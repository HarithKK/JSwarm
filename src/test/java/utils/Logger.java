package utils;

import org.usa.soc.Algorithm;
import org.usa.soc.IAlgorithm;
import org.usa.soc.multiRunner.MultiRunner;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.StringFormatter;

import java.util.Arrays;

public class Logger {
    private static final String DL  = " \t ";
    public static void showFunction(MultiRunner al){
        if(al == null){
            return;
        }
        double duration = al.getNanoDuration() / 1000000;
        double ep= Math.abs(al.getBestDValue() - al.getFunction().getExpectedBestValue());
        System.out.println(
                al.getAlgorithm().getClass().getSimpleName() + DL+
                StringFormatter.format(al.getFunction().getClass().getSimpleName(), 23)+ DL+
                StringFormatter.format(al.getBestValue(), 20) + DL+
                StringFormatter.format(String.valueOf(Mathamatics.round(al.getFunction().getExpectedBestValue(),4)), 10) + DL+
                StringFormatter.format(Mathamatics.getErrorPercentage(ep, 100), 25) + DL +
                StringFormatter.format("["+Mathamatics.round(al.getMinimumBestValue(),4)+","+Mathamatics.round(al.getMaximumBestValue(),4)+"]", 30) + DL+
                StringFormatter.format(Arrays.toString(al.getFunction().getMin()), 30)+ DL+
                StringFormatter.format(Arrays.toString(al.getFunction().getMax()), 30) + DL+
                StringFormatter.format(String.valueOf(al.getFunction().getNumberOfDimensions()),5) + DL+
                StringFormatter.format(String.valueOf(duration), 10) +DL+
                StringFormatter.format(al.getBestVariables(),80) + DL +
                StringFormatter.toString(al.getFunction().getExpectedParameters())
        );
    }

    public static void showFunction(Algorithm al){
        if(al == null){
            return;
        }
        double duration = al.getNanoDuration() / 1000000;
        double ep= Math.abs(al.getBestDoubleValue() - al.getFunction().getExpectedBestValue());
        System.out.println(
                al.getClass().getSimpleName() + DL+
                        StringFormatter.format(al.getFunction().getClass().getSimpleName(), 23)+ DL+
                        StringFormatter.format(al.getBestStringValue(), 20) + DL+
                        StringFormatter.format(String.valueOf(Mathamatics.round(al.getFunction().getExpectedBestValue(),4)), 10) + DL+
                        StringFormatter.format(Mathamatics.getErrorPercentage(ep, 100), 25) + DL +
                        StringFormatter.format(Arrays.toString(al.getFunction().getMin()), 30)+ DL+
                        StringFormatter.format(Arrays.toString(al.getFunction().getMax()), 30) + DL+
                        StringFormatter.format(String.valueOf(al.getFunction().getNumberOfDimensions()),5) + DL+
                        StringFormatter.format(String.valueOf(duration), 10) +DL+
                        StringFormatter.format(al.getBestVariables(),80) + DL +
                        StringFormatter.toString(al.getFunction().getExpectedParameters())
        );
    }
}
