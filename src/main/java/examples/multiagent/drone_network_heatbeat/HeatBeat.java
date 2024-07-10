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

    public static void main(String[] args) {

        Controller.dAgentGroup.setMarkerColor(Color.GREEN);
        Controller.cAgentGroup.setMarkerColor(Color.RED);
        Controller.cAgentGroup.setMarker(Markers.CROSS);

        Algorithm algorithm = new Algorithm() {

            int sampleCount = 0;

            @Override
            public void initialize() {

                double ix = 100.0;
                double iy = 10.0;

                Controller.dronesMap = NetworkGenerator.generateStatic(getMargins(), ix, iy);
                //dronesMap = new NetworkGenerator(ix, iy, 15, 50, getMargins()).getDroneMap();

                try {
                    for(Integer i : Controller.dronesMap.keySet()){
                        Controller.agentGroup.addAgent(Controller.dronesMap.get(i));
                    }
                    this.agents.put(Controller.agentGroup.name, Controller.agentGroup);
                    this.agents.put(Controller.dAgentGroup.name, Controller.dAgentGroup);
                    this.agents.put(Controller.cAgentGroup.name, Controller.cAgentGroup);

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
                Controller.dronesMap.get(0).velocityStar.updateValue(1.0, 1);
            }
        }, true);
        Executor.getInstance().AddCustomActions("-V", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.dronesMap.get(0).velocityStar.updateValue(-1.0, 1);
            }
        }, true);

        Executor.getInstance().AddCustomActions("DC", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // We can remove the drone from network for our implementation, bu not for Qian et al.
                // Thus we introduced a variable isDisconnected for that.
                DroneAgent a = Controller.dronesMap.remove(Controller.DISCONNECTED_KEY);
                algorithm.getAgents("Drones").removeAgent(a);
                Controller.cAgentGroup.getAgents().add(a);

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

