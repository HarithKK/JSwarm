package examples.si.benchmarks.DynamicMultiModalObjectiveFunctions;

import examples.si.benchmarks.DynamicObjectiveFunction;

import java.util.Arrays;

public class Function12 extends DynamicObjectiveFunction {

    public Function12(int n){
        super(n, -50, 50, -30, 0);
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

        double y1 = 10*Math.sin(Math.PI * Y(0));
        double y2 = 0;

        for(int i=0; i<getNumberOfDimensions()-1; i++){
            y2 += Math.pow(Y(i)-1,2) * (1 + 10*Math.pow(Math.sin(Math.PI*Y(i+1)),2));
        }

        double y3 = Math.pow(Y(getNumberOfDimensions()-1)-1,2);
        double y4 = Arrays.stream(super.getParameters()).mapToDouble(d-> U((double)d, 10, 100, 4)).sum();

        return (Math.PI * (y1 + y2+ y3) / getNumberOfDimensions()) + y4;
    }

}
