package org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.DynamicObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class Function14 extends DynamicObjectiveFunction {

    public Function14(int n){
        super(n, -100, 100, -100,-39.17);
    }

    private double Y(int i){
        return 1 + (((double)super.getParameters()[i]+1)/4);
    }

    private double U(double x, int a, int k, int m){
        if(x < -a){
            return k*Math.pow(-x-a, m);
        }else if(x >= -a && x <= a){
            return 0;
        }else{
            return k*Math.pow(x-a, m);
        }
    }

    @Override
    public Double call() {

       return 0.5 * Arrays.stream(super.getParameters()).mapToDouble(d->Math.pow((double)d,4)- 16*Math.pow((double)d,2) + (5*(double)d)).sum();
    }

}
