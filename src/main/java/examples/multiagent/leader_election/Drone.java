package examples.multiagent.leader_election;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Agent;

import java.util.OptionalDouble;

public class Drone extends Agent {
    int rank = -1;

    Vector velocity = new Vector(2);

    double controlEnergy = 0;
    double commEnergy = 0;

    public void moveUpper() {
        this.rank -=1;
    }

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

    public void setState(RealMatrix matrix){
        this.position.setValue(matrix.getEntry(0,0), 0);
        this.position.setValue(matrix.getEntry(1,0), 1);
        this.velocity.setValue(matrix.getEntry(2,0), 0);
        this.velocity.setValue(matrix.getEntry(3,0), 1);
    }

    public void updateEnergyProfile(){
        controlEnergy = this.velocity.getMagnitude();
        OptionalDouble od = getConncetions().stream().mapToDouble(c -> c.getPosition().getDistance(this.getPosition().getClonedVector())).max();
        if(od.isPresent()){
            commEnergy = od.getAsDouble();
        }
    }
}
