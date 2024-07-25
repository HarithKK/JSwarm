package examples.si.benchmarks.DynamicUnimodalObjectiveFunctions;

import examples.si.benchmarks.DynamicObjectiveFunction;

import java.util.Arrays;

public class Function6 extends DynamicObjectiveFunction {

    public Function6(int n){
        super(n, -100, 100, -750, 0);
    }

    public  Function6(int n, double p, double q){
        super(n, p, q, -30, 0);
    }

    @Override
    public Double call() {
        return Arrays.stream(super.getParameters()).mapToDouble(d-> Math.pow(Math.abs((Double)d + 0.5),2)).sum();
    }
}
