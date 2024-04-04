package org.usa.soc.benchmarks.DynamicCompositeBenchmarkFunctions;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions.Function10;
import org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions.Function8;
import org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions.Function9;
import org.usa.soc.benchmarks.DynamicUnimodalObjectiveFunctions.*;
import org.usa.soc.util.Commons;

import java.util.ArrayList;
import java.util.List;

public class Function20 extends ObjectiveFunction {

    private int numberOfDimensions = 2;
    private double[] min;
    private double[] max;

    private double[] expected;

    private List<ObjectiveFunction> functions;
    private double [] sigmas;
    private double [] lambdas;

    public Function20(int n){
        this.numberOfDimensions = n;
        min = Commons.fill(-5, n);
        max = Commons.fill(5, n);
        expected = Commons.fill(-400, n);

        functions = new ArrayList<>();

        functions.add(new Function1(n, -5, 5));
        functions.add(new Function2(n, -5, 5));
        functions.add(new Function3(n, -5, 5));
        functions.add(new Function4(n, -5, 5));
        functions.add(new Function5(n, -5, 5));
        functions.add(new Function6(n, -5, 5));
        functions.add(new Function7(n, -5, 5));
        functions.add(new Function8(n, -5, 5));
        functions.add(new Function9(n, -5, 5));
        functions.add(new Function10(n, -5, 5));

        sigmas = new double[]{
                0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1
        };
        lambdas = new double[]{
             0.1*0.2, 0.2*0.2, 3, 4, 0.5*0.05, 0.6*0.05, 0.7*0.15625, 0.8*0.15625, 0.9*0.05, 0.05
        };
    }

    @Override
    public Double call() {
        double sum =0;
        for(int i=0; i< 10; i++){
            ObjectiveFunction fn = functions.get(i);
            fn.setParameters(super.getParameters());
            sum += fn.call() * Commons.computeWeight(sigmas[i], lambdas[i]);
        }
        return sum;
    }

    @Override
    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    @Override
    public double[] getMin() {
        return min;
    }

    @Override
    public double[] getMax() {
        return max;
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return expected;
    }
}
