package org.usa.soc.benchmarks.DynamicUnimodalObjectiveFunctions;

import org.usa.soc.benchmarks.DynamicObjectiveFunction;

public class Function2 extends DynamicObjectiveFunction {

    public  Function2(int n){
       super(n, -10, 10, 3, 0);
    }

    public  Function2(int n, double p, double q){
        super(n, p, q, -30, 0);
    }

    @Override
    public Double call() {
        Double sum = 0.0;
        Double dot = 0.0;
        for(int i=0;i< getNumberOfDimensions();i++){
            Double data = Math.abs((Double)super.getParameters()[i]);
            sum += data;
            dot *= data;
        }
        return sum + dot;
    }
}
