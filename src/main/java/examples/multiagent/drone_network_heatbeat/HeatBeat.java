package examples.multiagent.drone_network_heatbeat;

import examples.multiagent.common.NetworkGenerator;
import org.knowm.xchart.XYSeries;
import org.usa.soc.core.ds.ChartType;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Markers;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.multiagent.view.ChartSeries;
import org.usa.soc.multiagent.view.ProgressiveChart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class HeatBeat {
    public static HashMap<Integer, DroneAgent> dronesMap = new HashMap<>();
    public static final double OmegaLeader = 10.0;
    public static final double alpha = 0.7;

    public static int K = 30;

    public static final int DISCONNECTED_KEY = 1;

    public static AgentGroup agentGroup = new AgentGroup("Drones");
    public static AgentGroup cAgentGroup = new AgentGroup("Crashed Agents");
    public static AgentGroup dAgentGroup = new AgentGroup("Disconnected Agents");

    public static void main(String[] args) {

        dAgentGroup.setMarkerColor(Color.GREEN);
        cAgentGroup.setMarkerColor(Color.RED);
        cAgentGroup.setMarker(Markers.CROSS);

        Algorithm algorithm = new Algorithm() {

            int sampleCount = 0;

            @Override
            public void initialize() {

                double ix = 100.0;
                double iy = 10.0;

                dronesMap = NetworkGenerator.generateStatic(getMargins(), ix, iy);
                //dronesMap = new NetworkGenerator(ix, iy, 15, 50, getMargins()).getDroneMap();

                try {
                    for(Integer i : dronesMap.keySet()){
                        agentGroup.addAgent(dronesMap.get(i));
                    }
                    this.agents.put(agentGroup.name, agentGroup);
                    this.agents.put(dAgentGroup.name, dAgentGroup);
                    this.agents.put(cAgentGroup.name, cAgentGroup);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                this.setInterval(200);
            }

            @Override
            public void run() {

            }
        };

        Executor.getInstance().AddCustomActions("+V", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dronesMap.get(0).velocityStar.updateValue(1.0, 1);
            }
        }, true);
        Executor.getInstance().AddCustomActions("-V", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dronesMap.get(0).velocityStar.updateValue(-1.0, 1);
            }
        }, true);

        Executor.getInstance().AddCustomActions("DC", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // We can remove the drone from network for our implementation, bu not for Qian et al.
                // Thus we introduced a variable isDisconnected for that.
                DroneAgent a = dronesMap.remove(DISCONNECTED_KEY);
                algorithm.getAgents("Drones").removeAgent(a);
                cAgentGroup.getAgents().add(a);

            }
        }, true);

        XYSeries.XYSeriesRenderStyle linestyle = ChartType.Line;
        XYSeries.XYSeriesRenderStyle linestyle1 = ChartType.Line;
        int maxLength = 50;

        for(int i=0; i< 15;i++){
            Executor.getInstance().registerChart(
                    new ProgressiveChart(200, 130, String.valueOf(i), "","Drone "+i)
                            .subscribe(new ChartSeries("dOmega", 0.0).setColor(Color.BLUE).setStyle(linestyle))
                            .subscribe(new ChartSeries("dUOmega", 0.0).setColor(Color.GREEN).setStyle(linestyle1))
                            .setMaxLength(maxLength)
            );
        }

        Executor.getInstance().executePlain2D("Heartbeat Algorithm",algorithm, 700, 700, new Margins(-50, 300, -100, 1500));
    }
}

