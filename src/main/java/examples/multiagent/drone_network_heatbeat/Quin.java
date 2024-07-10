package examples.multiagent.drone_network_heatbeat;

import examples.multiagent.common.NetworkGenerator;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.runners.Executor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class Quin {

    public static void main(String[] args) {

        Controller.dAgentGroup.setMarkerColor(Color.GREEN);

        Algorithm algorithm = new Algorithm() {
            @Override
            public void initialize() {
                double ix = 100.0;
                double iy = 10.0;

                Controller.dronesMap = NetworkGenerator.generateStatic(getMargins(), ix, iy);

                try {
                    for(Integer i : Controller.dronesMap.keySet()){
                        Controller.agentGroup.addAgent(Controller.dronesMap.get(i));
                    }
                    this.agents.put(Controller.agentGroup.name,Controller.agentGroup);
                    this.agents.put(Controller.dAgentGroup.name, Controller.dAgentGroup);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                this.setInterval(200);
            }

            @Override
            public void run() {

            }
        };

        Executor.getInstance().AddCustomActions("DC", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // We can remove the drone from network for our implementation, bu not for Qian et al.
                // Thus we introduced a variable isDisconnected for that.
                DroneAgent a = Controller.dronesMap.get(Controller.DISCONNECTED_KEY);

                // remove 0-1 link
                for(int i =0; i<a.edge.A.length; i++){
                    if(a.edge.A[i] == 1 && a.edge.B[i] != 1){
                        Controller.dronesMap.get(i).edge.A[a.index] = 0;
                        Controller.dronesMap.get(i).edge.B[a.index] = 0;
                        a.isDisconnected = true;
                        Controller.dronesMap.remove(a);
                        Controller.dAgentGroup.addAgent(a);
                    }
                }
            }
        }, true);

        Executor.getInstance().executePlain2D("Quin/Heartbeat Algorithm",algorithm, 700, 700, new Margins(-50, 300, -100, 1500));
    }
}
