package org.usa.soc.util;

import org.apache.commons.math3.analysis.function.Abs;
import org.apache.commons.math3.util.Precision;

public class Mathamatics {
    public static Double absRound(Double value, int r){
        return new Abs().value(Precision.round(value, r));
    }

    public static Double round(Double value, int r){
        return Precision.round(value, r);
    }
}
