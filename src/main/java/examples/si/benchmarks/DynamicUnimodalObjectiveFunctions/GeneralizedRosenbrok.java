package examples.si.benchmarks.DynamicUnimodalObjectiveFunctions;

import examples.si.benchmarks.DynamicObjectiveFunction;

public class GeneralizedRosenbrok extends DynamicObjectiveFunction {

    public GeneralizedRosenbrok(int n){
        super(n, -30,30, -15, 0);
    }

    public GeneralizedRosenbrok(int n, double p, double q){
        super(n, p, q, -30, 0);
    }

    @Override
    public Double call() {
        Double sum = 0.0;
        for(int i=0;i< getNumberOfDimensions()-1;i++){
            double i1 = (Double)super.getParameters()[i+1];
            double i0 = (Double)super.getParameters()[i];
            sum += Math.abs( 100* Math.pow(i1 - Math.pow(i0,2),2) + Math.pow((i0 - 1), 2));
        }
        return sum;
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

}
