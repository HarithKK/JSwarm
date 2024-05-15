package org.usa.soc.si.benchmarks.DynamicUnimodalObjectiveFunctions;

import org.usa.soc.si.benchmarks.DynamicObjectiveFunction;

public class Function1 extends DynamicObjectiveFunction {

    public  Function1(int n){
        super(n, -100, 100, -30, 0);
    }

    public  Function1(int n, double p, double q){
        super(n, p, q, -30, 0);
    }

    @Override
    public Double call() {
        Double sum = 0.0;
        for(int i=0;i< getNumberOfDimensions();i++){
            sum += ( Math.pow((Double)super.getParameters()[i], 2));
        }
        return sum;
    }
}
