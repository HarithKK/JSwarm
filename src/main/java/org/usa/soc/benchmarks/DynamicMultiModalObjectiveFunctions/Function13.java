package org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.DynamicObjectiveFunction;
import org.usa.soc.util.Commons;

import java.util.Arrays;

public class Function13 extends DynamicObjectiveFunction {

    public Function13(int n){
        super(n, -50, 50, -100, 0);
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

        double y1 = Math.pow(Math.sin(3*Math.PI*(double)super.getParameters()[0]),2);
        double y2 = 0;

        for(int i=0; i<getNumberOfDimensions(); i++){
            y2 += Math.pow((double)super.getParameters()[i]-1,2) * (1 + Math.pow(Math.sin(3*Math.PI*(double)super.getParameters()[i] + 1),2));
        }

        double y3 = Math.pow((double)super.getParameters()[getNumberOfDimensions()-1]-1,2) * (1+ Math.pow(Math.sin(2* Math.PI*(double)super.getParameters()[getNumberOfDimensions()-1]),2));
        double y4 = Arrays.stream(super.getParameters()).mapToDouble(d-> U((double)d, 5, 100, 4)).sum();

        return 0.1*(y1 + y2+ y3) + y4;
    }

}
