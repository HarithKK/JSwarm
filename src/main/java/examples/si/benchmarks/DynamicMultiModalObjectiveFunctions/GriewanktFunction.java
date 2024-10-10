package examples.si.benchmarks.DynamicMultiModalObjectiveFunctions;

import examples.si.benchmarks.DynamicObjectiveFunction;

public class GriewanktFunction extends DynamicObjectiveFunction {

    public GriewanktFunction(int n){
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
