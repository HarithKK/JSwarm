package examples.multiagent.leader_election.testcases;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Commons;

import java.awt.geom.Point2D;

public class OF extends ObjectiveFunction {

    public RealMatrix gc;
    int index;

    RealMatrix or;

    public OF(RealMatrix model, int index, Point2D origin){
        this.gc = model;
        this.index = index;
        this.or = MatrixUtils.createColumnRealMatrix(new double[]{origin.getX(), origin.getY()});
    }

    @Override
    public Double call() {

        try{
            RealMatrix xf = this.getParametersCommonVector().getClonedVector().operate(
                    Vector.OPERATOR.ADD, Commons.levyFlightVector(numberOfDimensions, 0.5)).toRealMatrix();

            RealMatrix data = xf.transpose().scalarMultiply(MatrixUtils.inverse(gc).getEntry(index, index)).multiply(xf);

            double f1 = data.getTrace();
            double f2 = xf.subtract(or).getNorm();

            return Math.min(f1, f2);

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
