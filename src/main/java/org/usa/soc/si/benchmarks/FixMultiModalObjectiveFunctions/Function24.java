package org.usa.soc.si.benchmarks.FixMultiModalObjectiveFunctions;

import org.usa.soc.si.benchmarks.DynamicObjectiveFunction;

public class Function24 extends DynamicObjectiveFunction {

    public Function24(){
        super(2, new double[]{-1.5, -3}, new double[]{4,4},  new double[]{-0.54719,-1.54719},-1.9133 );
    }
    
    @Override
    public Double call() {
        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        return Math.sin(x + y) + Math.pow(x-y, 2) - (1.5*x) + (2.5*y) +1;
    }

}
