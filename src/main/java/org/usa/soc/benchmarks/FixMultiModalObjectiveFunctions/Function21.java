package org.usa.soc.benchmarks.FixMultiModalObjectiveFunctions;

import org.usa.soc.benchmarks.DynamicObjectiveFunction;

import java.util.Arrays;

public class Function21 extends DynamicObjectiveFunction {

    public Function21(){
        super(4, -10,10, 1,0 );
    }

    public  Function21(int n, double p, double q){
        super(n, p, q, -30, 0);
    }

    @Override
    public Double call() {
        Double x1 = (Double) super.getParameters()[0];
        Double x2 = (Double) super.getParameters()[1];
        Double x3 = (Double) super.getParameters()[2];
        Double x4 = (Double) super.getParameters()[3];

        Double d = 100 * Math.pow((Math.pow(x1,2) - x2), 2);
        d += Math.pow(x1-1, 2);
        d += Math.pow(x3-1, 2);
        d += 90*Math.pow(Math.pow(x3,2)-x4, 2);
        d += 10.1*(Math.pow(x2-1,2) + Math.pow(x4-1,2));
        d += 19.8*(x2-1)*(x4-1);
        return d;
    }

}
