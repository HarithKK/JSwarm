package examples.multiagent.drone_network_heatbeat;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.multiagent.runners.Executor;

public class DroneAgent extends Agent {
    int index;
    int layer = 0;
    int[] links;

    int[] ml;
    int[] m0;
    int[] mf;

    public DroneAgent(int i, Margins m, double x, double y, int layer, int[] ml, int[] m0, int[] mf){
        this.index = i;
        this.layer = layer;
        this.initPosition(m, x, y);
        this.m0 = m0;
        this.ml = ml;
        this.mf = mf;
    }
    @Override
    public void step() {
        Executor.getAlgorithm().getAgents("Drones");
    }
}
