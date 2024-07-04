package examples.multiagent.drone_network_heatbeat;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.multiagent.runners.Executor;

public class DroneAgent extends Agent {
    int index;
    int layer = 0;

    E edge;

    public Vector velocityStar = new Vector(2).setValues(new double[]{0, 1});
    Vector velocity = new Vector(2).setValues(new double[]{0,0});

    private double omega = 0;
    private double lastOmega = 0;
    private double lastUOmega = 0;

    private long lastTime = 0;

    double K1 = -1;

    public DroneAgent(int i, Margins m, double x, double y, int layer, E edge){
        this.index = i;
        this.layer = layer;
        this.edge = edge;
        this.initPosition(m, x, y);
    }

    @Override
    public void step() {
        if(HeatBeat.cAgentGroup.getAgents().contains(this)){
            return;
        }
        if(this.layer == 0){
            velocity.setVector(velocityStar);
        }else{
            Vector vStar = new Vector(2);
            Vector vU = new Vector(2);

            for(int i = 0; i< this.edge.B.length; i++){
                if(!HeatBeat.dronesMap.containsKey(i)){
                    continue;
                }
                vU.updateVector(this.getPosition().getClonedVector()
                        .operate(Vector.OPERATOR.SUB, HeatBeat.dronesMap.get(i).getPosition().getClonedVector()
                                .operate(Vector.OPERATOR.MULP, (double)this.edge.B[i])
                                .operate(Vector.OPERATOR.MULP, (double)this.edge.A[i])));
                if(this.edge.B[i] < 0){
                    vStar.setVector(HeatBeat.dronesMap.get(i).velocity.getClonedVector());
                }
            }
            this.velocity.setVector(vStar.operate(Vector.OPERATOR.ADD,vU.operate(Vector.OPERATOR.MULP, generateK1())));
        }
        this.updatePosition(this.getPosition().operate(Vector.OPERATOR.ADD, this.velocity));

        if(this.layer == 0){
            this.omega = HeatBeat.OmegaLeader;
        }else{
            double l = 0;
            for(int i = 0; i< this.edge.B.length; i++){
                if(!HeatBeat.dronesMap.containsKey(i)){
                    continue;
                }
                if(this.edge.A[i] != 0){
                    l = Math.max(HeatBeat.dronesMap.get(i).omega * HeatBeat.alpha, l);
                }
            }
            this.omega = l;
        }

        long t = System.currentTimeMillis();

        if(t - lastTime > 500){

            double uOmega =0;

            for(int i=0; i< this.edge.B.length; i++){
                if(!HeatBeat.dronesMap.containsKey(i)){
                    continue;
                }
                if(this.edge.A[i] != 0){
                    uOmega += this.edge.B[i]*(1-HeatBeat.alpha)*HeatBeat.OmegaLeader;
                }
            }

            double dOmega = (omega - lastOmega)/500;
            double dUOmega = (uOmega - lastUOmega)/500;
            Executor.getInstance().updateData(String.valueOf(index), "dOmega", omega);
            Executor.getInstance().updateData(String.valueOf(index), "dUOmega", uOmega);
            Executor.getInstance().updateData("Constants", "K1", K1);
            if(dOmega != 0){
                lastOmega = omega;
                if(dOmega < 0){
                    this.velocity.resetAllValues(0.0);
                    try{
                        Executor.getAlgorithm().getAgents(HeatBeat.dAgentGroup.name).removeAgent(this);
                    }catch (Exception e){

                    }
                    Executor.getAlgorithm().getAgents(HeatBeat.dAgentGroup.name).addAgent(this);
                }
            }
        }
    }

    private Double generateK1() {
        if(K1 > 2){
            K1 = 0;
        }
        K1 += 1;
        return Math.sin(K1 * Math.PI);
    }
}
