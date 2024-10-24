package examples.multiagent.leader_election.core;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.util.Commons;
import org.usa.soc.util.HomogeneousTransformer;
import org.usa.soc.util.Randoms;

import java.util.OptionalDouble;

public class Drone extends Agent {
    public int rank = -1;

    public Vector velocity = new Vector(2);

    public double controlEnergy = 0;
    public double commEnergy = 0;

    public double nLayeredLinks;

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

    public void updateU(WalkType selection, double theta, double av) {
        switch (selection){
            /*Circular Motion With Velocity*/
            case RANDOM_CIRCLE: velocity.setValues(new double[]{av*Math.sin(theta),av*Math.cos(theta)}); break;
            case RANDOM_THETA: {
                double angle = Randoms.rand(0, 180);
                RealMatrix vc = MatrixUtils.createRealMatrix(3, 1);
                vc.setEntry(0,0, velocity.getValue(0));
                vc.setEntry(1,0, velocity.getValue(1));
                vc.setEntry(2,0, 1);
                this.velocity.setVector(
                        HomogeneousTransformer.getRotationMatrix(Math.toRadians(angle),2)
                                .multiply(vc)
                                .getColumnVector(0)
                );
            }; break;
        }
    }
}
