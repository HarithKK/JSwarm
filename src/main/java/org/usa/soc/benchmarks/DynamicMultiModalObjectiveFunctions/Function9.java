package org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions;

import org.usa.soc.benchmarks.DynamicObjectiveFunction;

import java.util.Arrays;

public class Function9 extends DynamicObjectiveFunction {
    public Function9(int n){
        super(n, -5.12, 5.12, -2, 0);
    }

    public  Function9(int n, double p, double q){
        super(n, p, q, -30, 0);
    }

    @Override
    public Double call() {
        return Arrays.stream(super.getParameters()).mapToDouble(d->
                (Math.pow((double)d,2) - (10* Math.cos(2*Math.PI*(double)d)) + 10)).sum();
    }
}
