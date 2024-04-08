package org.usa.soc.si.benchmarks.DynamicMultiModalObjectiveFunctions;

import org.usa.soc.si.benchmarks.DynamicObjectiveFunction;

public class Function11 extends DynamicObjectiveFunction {

    public Function11(int n){
        super(n, -600, 600, -400, 0);
    }

    @Override
    public Double call() {

        double y1 =0;
        double y2=0;

        for(int i=1; i<= getNumberOfDimensions(); i++){
            double x = (double) super.getParameters()[i-1];
            y1 += Math.pow(x,2);
            y2 *= Math.cos(x / Math.sqrt(i));
        }
        return (y1 - y2 +1)/4000;
    }

}
