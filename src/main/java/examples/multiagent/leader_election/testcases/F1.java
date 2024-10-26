package examples.multiagent.leader_election.testcases;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

public class F1 extends ObjectiveFunction {

    public RealMatrix gc;
    int index;
    public F1(RealMatrix model, int index){
        this.gc = model;
        this.index = index;
    }

    @Override
    public Double call() {

        try{
            RealMatrix xf = this.getParametersCommonVector().getClonedVector().operate(
                    Vector.OPERATOR.ADD, Commons.levyFlightVector(numberOfDimensions, 0.5)).toRealMatrix();

            RealMatrix data = xf.transpose().scalarMultiply(MatrixUtils.inverse(gc).getEntry(index, index)).multiply(xf);

            return data.getTrace();
        }catch (Exception e){
            return Double.MAX_VALUE;
        }
    }

    @Override
    public double[] getMin() {
        return Commons.fill(-10, numberOfDimensions);
    }

    @Override
    public double[] getMax() {
        return Commons.fill(10, numberOfDimensions);
    }

    @Override
    public double getExpectedBestValue() {
        return 0;
    }

    @Override
    public double[] getExpectedParameters() {
        return Commons.fill(0, numberOfDimensions);
    }
}
