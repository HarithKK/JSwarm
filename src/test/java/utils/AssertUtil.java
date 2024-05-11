package utils;

import org.junit.jupiter.api.Assertions;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;

public class AssertUtil {

    public static void evaluate(double aBest, double eBest, Vector aVariables, double[] eVariables, int D, double variance, int limit){
        double p = Math.abs(Mathamatics.round(aBest,limit));
        double l = Math.abs(Mathamatics.round(eBest,limit));
        //Assertions.assertTrue(p >= (l - variance) && p <= (l + variance), "Best Value: "+(l - variance) + "<=" +p +"<="+(l + variance));


        for(int i=0;i<D; i++){
            p = aVariables.toList(limit).get(i);
            p = Math.abs(Mathamatics.round(p, limit));
            l = Math.abs(Mathamatics.round(eVariables[i], limit));
//            Assertions.assertTrue(p >= (l- variance) && p <= (l + variance),
//                    "Best Variable ["+i+"]: "+ (l - variance) + "<=" + p +"<="+(l + variance));
        }
    }

    public static void evaluate(double aBest, double eBest, double variance, int limit){
        double p = Math.abs(Mathamatics.round(aBest,limit));
        double l = Math.abs(Mathamatics.round(eBest,limit));
        Assertions.assertTrue(p >= (l - variance) && p <= (l + variance), "Best Value: "+(l - variance) + "<=" +p +"<="+(l + variance));
    }

    public static void evaluate(Vector aVariables, double[] eVariables, int D, double variance, int limit){
        double p,l;
        for(int i=0;i<D; i++){
            p = aVariables.toList(limit).get(i);
            p = Math.abs(Mathamatics.round(p, limit));
            l = Math.abs(Mathamatics.round(eVariables[i], limit));
            Assertions.assertTrue(p >= (l- variance) && p <= (l + variance),
                    "Best Variable ["+i+"]: "+ (l - variance) + "<=" + p +"<="+(l + variance));
        }
    }
}
