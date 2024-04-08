package org.usa.soc.si.benchmarks.DynamicUnimodalObjectiveFunctions;

import org.usa.soc.si.benchmarks.DynamicObjectiveFunction;

public class Function3 extends DynamicObjectiveFunction {

    public Function3(int n){
        super(n, -100, 100, -30, 0);
    }

    public Function3(int n, double p, double q){
        super(n, p, q, -30, 0);
    }

    @Override
    public Double call() {

        Double []cols = new Double[getNumberOfDimensions()];
        Double sum = 0.0;
        for(int i=0;i< getNumberOfDimensions();i++){
            cols[i] = sum;
            sum += (Double)super.getParameters()[i];
        }
        sum = 0.0;
        for(int i=1;i< getNumberOfDimensions();i++){
            sum += Math.pow(cols[i],2);
        }
        return sum;
    }

}
