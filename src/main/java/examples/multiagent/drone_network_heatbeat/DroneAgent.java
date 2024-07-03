package examples.multiagent.drone_network_heatbeat;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.multiagent.runners.Executor;

import java.util.HashMap;

public class DroneAgent extends Agent {
    int index;
    int layer = 0;

    E edge;

    Vector velocityStar = new Vector(2).setValues(new double[]{0, 5});
    Vector velocity = new Vector(2).setValues(new double[]{0,0});

    double K1;

    public DroneAgent(int i, Margins m, double x, double y, int layer, E edge){
        this.index = i;
        this.layer = layer;
        this.edge = edge;
        this.initPosition(m, x, y);

        if(layer == 0){
            this.K1 = 1.0;
        }else if(layer == 1 ){
            this.K1 = -0.1;
        }else{
            this.K1 = -0.01;
        }
    }
    @Override
    public void step() {
        if(this.layer == 0){
            velocity.setVector(velocityStar);
        }else{
            Vector vu = new Vector(2);
            for(int i = 0; i< this.edge.B.length; i++){
                vu.setVector(vu.operate(Vector.OPERATOR.ADD,
                        this.getPosition()
                        .operate(Vector.OPERATOR.SUB, HeatBeat.dronesMap.get(i).getPosition())
                        .operate(Vector.OPERATOR.MULP, (double)this.edge.B[i])));
            }
            System.out.println(vu.toString());
            this.velocity.setVector(this.velocity.operate(Vector.OPERATOR.MULP,0.01).operate(Vector.OPERATOR.ADD,vu.operate(Vector.OPERATOR.MULP, 0.01)));
        }
        this.updatePosition(this.getPosition().operate(Vector.OPERATOR.ADD, this.velocity));
    }
}
