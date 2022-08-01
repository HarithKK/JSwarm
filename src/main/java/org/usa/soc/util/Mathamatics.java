package org.usa.soc.util;

import org.apache.commons.math3.analysis.function.Abs;
import org.apache.commons.math3.util.Precision;
import org.usa.soc.ObjectiveFunction;

public class Mathamatics {
    public static Double absRound(Double value, int r){
        return new Abs().value(Precision.round(value, r));
    }

    public static Double round(Double value, int r){
        return Precision.round(value, r);
    }

    public static Double[] getCenterPoint(int D, double[] min, double[] max){
        Double []indexes = new Double[D];
        for(int i=0;i<D;i++){
            indexes[i] = Math.abs(max[i] - min[i])/2;
        }
        return indexes;
    }
}
