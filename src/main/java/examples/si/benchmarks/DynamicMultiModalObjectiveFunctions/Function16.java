package examples.si.benchmarks.DynamicMultiModalObjectiveFunctions;

import examples.si.benchmarks.DynamicObjectiveFunction;

import java.util.Arrays;

public class Function16 extends DynamicObjectiveFunction {

    // dixen price
    public Function16(int n){
        super(n, -10, 10, 0,0);
    }

    @Override
    public Double call() {
        Double res1 =0.0;
        for(int i=1; i<getNumberOfDimensions(); i++){
            res1 += i * Math.pow(2*Math.pow((Double) getParameters()[i],2)-(Double) getParameters()[i]-1,2);
        }
        return Math.pow((Double)getParameters()[0] - 1,2) - res1;
    }

}
