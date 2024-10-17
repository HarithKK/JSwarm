package examples.si.benchmarks.DynamicUnimodalObjectiveFunctions;

import examples.si.benchmarks.DynamicObjectiveFunction;

public class Schwefel12Function extends DynamicObjectiveFunction {

    public Schwefel12Function(int n){
        super(n, -100, 100, -30, 0);
    }

    public Schwefel12Function(int n, double p, double q){
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
