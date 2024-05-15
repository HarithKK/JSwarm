package org.usa.soc.si.benchmarks.DynamicMultiModalObjectiveFunctions;

import org.usa.soc.si.benchmarks.DynamicObjectiveFunction;

import java.util.Arrays;

public class Function8 extends DynamicObjectiveFunction {

    public Function8(int n){
        super(n, -500,500, -300,-418.9829 * n );
    }

    public  Function8(int n, double p, double q){
        super(n, p, q, -30, 0);
    }

    @Override
    public Double call() {
        return Arrays.stream(super.getParameters()).mapToDouble(d-> (-(double)d)*Math.sin(Math.sqrt(Math.abs((double)d)))).sum();
    }

}
