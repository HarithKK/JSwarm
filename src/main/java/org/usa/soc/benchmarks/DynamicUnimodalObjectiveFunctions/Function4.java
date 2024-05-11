package org.usa.soc.benchmarks.DynamicUnimodalObjectiveFunctions;

import org.usa.soc.benchmarks.DynamicObjectiveFunction;

import java.util.Arrays;

public class Function4 extends DynamicObjectiveFunction {

    public Function4(int n){
        super(n, -100, 100, -30, 0);
    }

    public  Function4(int n, double p, double q){
        super(n, p, q, -30, 0);
    }

    @Override
    public Double call() {
        return Arrays.stream(super.getParameters()).mapToDouble(d-> Math.abs((double)d)).max().getAsDouble();
    }
}
