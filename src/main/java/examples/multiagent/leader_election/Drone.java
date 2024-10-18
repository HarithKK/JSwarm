package examples.multiagent.leader_election;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Agent;

public class Drone extends Agent {
    int rank = -1;

    Vector velocity = new Vector(2);

    @Override
    public void step() {
        this.getPosition().updateVector(velocity);
    }

    public RealMatrix getState() {
        return MatrixUtils.createColumnRealMatrix(new double[]{
                position.getValue(0),
                position.getValue(1),
                velocity.getValue(0),
                velocity.getValue(1)}
        );
    }
}
