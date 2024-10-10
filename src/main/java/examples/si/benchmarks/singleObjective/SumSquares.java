package examples.si.benchmarks.singleObjective;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class SumSquares extends ObjectiveFunction {
    @Override
    public Double call() {
        Double sum = 0.0;
        for(int i=0;i< getNumberOfDimensions();i++){
            sum += ( Math.pow((Double)super.getParameters()[i], 2));
        }
        return sum;
    }

    @Override
    public int getNumberOfDimensions() {
        return numberOfDimensions;
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-10, this.getNumberOfDimensions());
    }

    @Override
    public double[] getMax() {
        return Commons.fill(10, this.getNumberOfDimensions());
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(0, this.getNumberOfDimensions());
    }
}
