package examples.multiagent.drone_network_heatbeat;

import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.runners.Executor;

import java.util.HashMap;

public class HeatBeat {
    public static HashMap<Integer, DroneAgent> dronesMap = new HashMap<>();

    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm() {
            AgentGroup agentGroup = new AgentGroup("Drones");
            @Override
            public void initialize() {

                double ix = 100.0;
                double iy = 40.0;

                dronesMap.put(
                        0,
                        new DroneAgent(0, getMargins(), ix, iy, 0, new E(new int[]{0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        1,
                        new DroneAgent(1, getMargins(), ix-10, iy-10, 1, new E(new int[]{1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0}, new int[]{-1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        2,
                        new DroneAgent(2, getMargins(), ix, iy-10, 1, new E(new int[]{1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0}, new int[]{-1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0}))
                );
                dronesMap.put(
                        3,
                        new DroneAgent(3, getMargins(), ix+10, iy-10, 1, new E(new int[]{1, 0, 1, 0, 0, 0, 0,0, 0, 1, 1, 1}, new int[]{-1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1}))
                );
                dronesMap.put(
                        7,
                        new DroneAgent(7, getMargins(), ix-3, iy-20, 2, new E(new int[]{0, 0, 1, 0, 0, 0, 1,0, 1, 0, 0, 0}, new int[]{0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        8,
                        new DroneAgent(8, getMargins(), ix+3, iy-20, 2, new E(new int[]{0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0}, new int[]{0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        4,
                        new DroneAgent(4, getMargins(), ix-20, iy-20, 2, new E(new int[]{0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}, new int[]{0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        5,
                        new DroneAgent(5, getMargins(), ix-15, iy-20, 2, new E(new int[]{0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0}, new int[]{0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        6,
                        new DroneAgent(6, getMargins(), ix-10, iy-20, 2, new E(new int[]{0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0}, new int[]{0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        9,
                        new DroneAgent(9, getMargins(), ix+10, iy-20, 2, new E(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0}, new int[]{0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0}))

                );
                dronesMap.put(
                        10,
                        new DroneAgent(10, getMargins(), ix+15, iy-20, 2, new E(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1}, new int[]{0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        11,
                        new DroneAgent(11, getMargins(), ix+20, iy-20, 2, new E(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0}, new int[]{0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0}))
                );

                try {
                    for(Integer i : dronesMap.keySet()){
                        agentGroup.addAgent(dronesMap.get(i));
                    }
                    this.agents.put(agentGroup.name, agentGroup);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                this.setInterval(200);
            }

            @Override
            public void run() {

            }
        };

        Executor.getInstance().executePlain2D("Heartbeat Algorithm",algorithm, 700, 700, new Margins(0, 200, 0, 1000));
    }
}

class E{
    int[] A;
    int[] B;

    public E(int[] A, int[] B){
        this.A = A;
        this.B = B;
    }
}
