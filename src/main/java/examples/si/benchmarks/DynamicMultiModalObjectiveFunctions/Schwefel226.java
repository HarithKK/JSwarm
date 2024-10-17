package examples.si.benchmarks.DynamicMultiModalObjectiveFunctions;

import examples.si.benchmarks.DynamicObjectiveFunction;

import java.util.Arrays;

public class Schwefel226 extends DynamicObjectiveFunction {

    public Schwefel226(int n){
        super(n, -500,500, -300,-418.9829 * n );
    }

    public Schwefel226(int n, double p, double q){
        super(n, p, q, -30, 0);
    }

    @Override
    public Double call() {
        return Arrays.stream(super.getParameters()).mapToDouble(d-> (-(double)d)*Math.sin(Math.sqrt(Math.abs((double)d)))).sum();
    }

}
