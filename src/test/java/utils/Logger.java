package utils;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.intefaces.IAlgorithm;

import java.util.Arrays;

public class Logger {
    private static final String DL  = " \t ";
    public static void showFunction(IAlgorithm al){
        if(al == null){
            return;
        }
        System.out.println(
                al.getClass().getSimpleName() + DL+
                al.getFunction().getClass().getSimpleName()+ DL+
                al.getBestValue() + DL+
                Arrays.toString(al.getFunction().getMin())+ DL+
                Arrays.toString(al.getFunction().getMax()) + DL+
                al.getFunction().getNumberOfDimensions() + DL+
                al.getBestVariables() + DL+
                (al.getNanoDuration() / 1000000));
    }
}
