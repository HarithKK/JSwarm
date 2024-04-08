package org.usa.soc.si.benchmarks.FixMultiModalObjectiveFunctions;

import org.usa.soc.si.benchmarks.DynamicObjectiveFunction;

public class Function23 extends DynamicObjectiveFunction {

    public Function23(){
        super(2, -2,2,  new double[]{0,-1},3 );
    }

    @Override
    public Double call() {
        double x = (double)super.getParameters()[0];
        double y = (double)super.getParameters()[1];

        double z1 = 1+Math.pow((x + y+ 2),2)*(19-(14*x)+(3*(Math.pow(x,2))) - (14*y)+ (6*x*y) + (3 * Math.pow(y,2)));
        double z2 = 30+Math.pow((2*x - 3*y),2)*(18-(32*x)+(12*(Math.pow(x,2))) + (48*y) - (36*x*y) + (27 * Math.pow(y,2)));

        return z1 * z2;
    }

}
