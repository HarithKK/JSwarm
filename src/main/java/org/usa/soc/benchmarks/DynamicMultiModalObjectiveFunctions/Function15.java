package org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions;

import org.usa.soc.benchmarks.DynamicObjectiveFunction;

import java.util.Arrays;

public class Function15 extends DynamicObjectiveFunction {

    public Function15(int n){
        super(n, -100, 100, -100,-39.17);
    }

    @Override
    public Double call() {
        Double res1 =0.0;
        for(int i=0; i<getNumberOfDimensions(); i++){
            res1 += 0.5 * (i+1) * (Double) getParameters()[i];
        }
        return Arrays.asList(super.getParameters()).stream().mapToDouble(d -> Math.pow((double)d, 2)).sum()
                + Math.pow(res1, 2)
                + Math.pow(res1, 4);
    }

}
