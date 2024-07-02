package examples.multiagent.drone_network_heatbeat;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.runners.Executor;

public class HeatBeat {
    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm() {

            AgentGroup agentGroup = new AgentGroup("Drones");
            @Override
            public void initialize() {

                double ix = 100.0;
                double iy = 40.0;

                DroneAgent[] a = new DroneAgent[]{
                        new DroneAgent(0, getMargins(), ix, iy, 0, new int[]{} , new int[]{}, new int[]{1,2,3}),

                        new DroneAgent(1, getMargins(), ix-10, iy-10, 1, new int[]{0} , new int[]{2}, new int[]{4, 5, 6}),
                        new DroneAgent(2, getMargins(), ix, iy-10, 1, new int[]{0} , new int[]{1,3}, new int[]{7, 8}),
                        new DroneAgent(3, getMargins(), ix+10, iy-10, 1, new int[]{0} , new int[]{2}, new int[]{9, 10, 11}),

                        new DroneAgent(7, getMargins(), ix-3, iy-20, 2, new int[]{2} , new int[]{6,8}, new int[]{}),
                        new DroneAgent(8, getMargins(), ix+3, iy-20, 2, new int[]{2} , new int[]{7,9}, new int[]{}),

                        new DroneAgent(4, getMargins(), ix-20, iy-20, 2, new int[]{1} , new int[]{5}, new int[]{}),
                        new DroneAgent(5, getMargins(), ix-15, iy-20, 2, new int[]{1} , new int[]{4,6}, new int[]{}),
                        new DroneAgent(6, getMargins(), ix-10, iy-20, 2, new int[]{1} , new int[]{5,7}, new int[]{}),

                        new DroneAgent(9, getMargins(), ix+10, iy-20, 2, new int[]{3} , new int[]{8,10}, new int[]{}),
                        new DroneAgent(10, getMargins(), ix+15, iy-20, 2, new int[]{3} , new int[]{9,11}, new int[]{}),
                        new DroneAgent(11, getMargins(), ix+20, iy-20, 2, new int[]{3} , new int[]{10}, new int[]{}),
                };

                try {
                    for(int i=0; i < a.length; i++){
                        agentGroup.addAgent(a[i]);
                    }
                    this.agents.put(agentGroup.name, agentGroup);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void run() {

            }
        };

        Executor.getInstance().executePlain2D("Heartbeat Algorithm",algorithm, 700, 700, new Margins(0, 200, 0, 200));
    }
}
