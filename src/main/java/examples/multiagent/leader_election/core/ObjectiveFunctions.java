package examples.multiagent.leader_election.core;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Commons;

public class ObjectiveFunctions {
    public static Pair<Double, Double> f(Drone.Tree t, RealMatrix gc, int index, Vector position){
        try{
            RealMatrix xf = t.position.getClonedVector().operate(
                    Vector.OPERATOR.ADD, Commons.levyFlightVector(2, 0.5)).toRealMatrix();

            RealMatrix data = xf.transpose().scalarMultiply(MatrixUtils.inverse(gc).getEntry(index, index)).multiply(xf);

            double f1 = Math.pow(data.getTrace(), -1);
            double f2 = xf.subtract(position.toRealMatrix()).getNorm();

            return new Pair<>(f1, f2);

        }catch (Exception e){
            System.out.println("OF Error");
            return new Pair<>(Double.MAX_VALUE, Double.MAX_VALUE);
        }
    }
}
