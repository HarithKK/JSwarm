package examples.si.benchmarks.DynamicMultiModalObjectiveFunctions;

import examples.si.benchmarks.DynamicObjectiveFunction;

import java.util.Arrays;

public class Function10 extends DynamicObjectiveFunction {

    public Function10(int n){
        super(n, -32, 32, 0, 0);
    }

    @Override
    public Double call() {
        Double y1 = Arrays.stream(super.getParameters()).mapToDouble(d->Math.pow((Double)d,2)).sum()/ getNumberOfDimensions();
        Double y2 = (-20)*Math.exp((-0.2)*Math.sqrt(y1));
        Double y3 = -Math.exp(Arrays.stream(super.getParameters()).mapToDouble(d->Math.cos(2*Math.PI*(double)d)).sum()/ getNumberOfDimensions());
        return y2+y3+20+Math.E;
    }

    public  Function10(int n, double p, double q){
        super(n, p, q, -30, 0);
    }

}
