package org.usa.soc.si.benchmarks.FixMultiModalObjectiveFunctions;

import org.usa.soc.si.benchmarks.DynamicObjectiveFunction;

public class Function25 extends DynamicObjectiveFunction {

    public Function25(){
        super(2, -5, 5,  0,0 );
    }
    
    @Override
    public Double call() {
        Double x = (Double) super.getParameters()[0];
        Double y = (Double) super.getParameters()[1];

        return (2*Math.pow(x,2)) - (1.05*Math.pow(x,4)) + (Math.pow(x,6)/6) + (x*y) + (Math.pow(y,2));
    }

}
