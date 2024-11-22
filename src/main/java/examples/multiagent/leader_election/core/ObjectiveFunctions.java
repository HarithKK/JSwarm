package examples.multiagent.leader_election.core;

import examples.multiagent.leader_election.core.data_structures.Tree;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Commons;

public class ObjectiveFunctions {
    public static Pair<Double, Double> f(Tree t, RealMatrix gc, int index, Vector position){
        try{
            RealMatrix xf = t.position.getClonedVector().toRealMatrix();

            RealMatrix data = xf.transpose().scalarMultiply(MatrixUtils.inverse(gc).getEntry(index, index)).multiply(xf);

            double f1 = Math.pow(data.getTrace(), -1);
            double f2 = xf.subtract(position.toRealMatrix()).getNorm();

            return new Pair<>(f1, f2);

        }catch (Exception e){
            System.out.println("OF Error");
            return new Pair<>(Double.MAX_VALUE, Double.MAX_VALUE);
        }
    }

    public static Pair<Double, Double> f(Tree t, RealMatrix gc, RealMatrix A, int index, Vector position){
        try{
            RealMatrix x = position.toRealMatrix().scalarMultiply(A.getEntry(t.index,t.index));
            RealMatrix xf = t.position.getClonedVector().toRealMatrix().subtract(x);

            RealMatrix data = xf.transpose().scalarMultiply(MatrixUtils.inverse(gc).getEntry(index, index)).multiply(xf);

            double f1 = 1 / data.getTrace();
            double f2 = xf.subtract(position.toRealMatrix()).getNorm();

            return new Pair<>(f1, f2);

        }catch (Exception e){
            System.out.println("OF Error");
            return new Pair<>(Double.MAX_VALUE, Double.MAX_VALUE);
        }
    }
}
