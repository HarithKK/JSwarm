package examples.multiagent.drone_network_heatbeat;

import org.knowm.xchart.XYSeries;
import org.usa.soc.core.ds.ChartType;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Markers;
import org.usa.soc.core.ds.Vector;
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

    public static final int DISCONNECTED_KEY = 2;

    public static AgentGroup agentGroup = new AgentGroup("Drones");
    public static AgentGroup cAgentGroup = new AgentGroup("Crashed Agents");
    public static AgentGroup dAgentGroup = new AgentGroup("Disconnected Agents");

    public static void main(String[] args) {

        dAgentGroup.setMarkerColor(Color.GREEN);
        cAgentGroup.setMarkerColor(Color.RED);
        cAgentGroup.setMarker(Markers.CROSS);
        Algorithm algorithm = new Algorithm() {

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
                        new DroneAgent(1, getMargins(), ix-10, iy-30, 1, new E(new int[]{1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0}, new int[]{-1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        2,
                        new DroneAgent(2, getMargins(), ix, iy-30, 1, new E(new int[]{1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0}, new int[]{-1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0}))
                );
                dronesMap.put(
                        3,
                        new DroneAgent(3, getMargins(), ix+10, iy-30, 1, new E(new int[]{1, 0, 1, 0, 0, 0, 0,0, 0, 1, 1, 1}, new int[]{-1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1}))
                );
                dronesMap.put(
                        7,
                        new DroneAgent(7, getMargins(), ix-3, iy-60, 2, new E(new int[]{0, 0, 1, 0, 0, 0, 1,0, 1, 0, 0, 0}, new int[]{0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        8,
                        new DroneAgent(8, getMargins(), ix+3, iy-60, 2, new E(new int[]{0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0}, new int[]{0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        4,
                        new DroneAgent(4, getMargins(), ix-20, iy-60, 2, new E(new int[]{0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}, new int[]{0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        5,
                        new DroneAgent(5, getMargins(), ix-15, iy-60, 2, new E(new int[]{0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0}, new int[]{0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        6,
                        new DroneAgent(6, getMargins(), ix-10, iy-60, 2, new E(new int[]{0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0}, new int[]{0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        9,
                        new DroneAgent(9, getMargins(), ix+10, iy-60, 2, new E(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0}, new int[]{0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0}))

                );
                dronesMap.put(
                        10,
                        new DroneAgent(10, getMargins(), ix+15, iy-60, 2, new E(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1}, new int[]{0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0}))
                );
                dronesMap.put(
                        11,
                        new DroneAgent(11, getMargins(), ix+20, iy-60, 2, new E(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0}, new int[]{0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0}))
                );

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
                DroneAgent a = dronesMap.remove(DISCONNECTED_KEY);
                algorithm.getAgents("Drones").removeAgent(a);
                a.velocityStar.resetAllValues(0.0);
                a.velocity.resetAllValues(0.0);
                cAgentGroup.getAgents().add(a);
            }
        }, true);

        XYSeries.XYSeriesRenderStyle linestyle = ChartType.Line;
        XYSeries.XYSeriesRenderStyle linestyle1 = ChartType.Line;
        int maxLength = 50;

        for(int i=0; i< 12;i++){
            Executor.getInstance().registerChart(
                    new ProgressiveChart(200, 130, String.valueOf(i), "","Drone "+i)
                            .subscribe(new ChartSeries("dOmega", 0.0).setColor(Color.BLUE).setStyle(linestyle))
                            .subscribe(new ChartSeries("dUOmega", 0.0).setColor(Color.GREEN).setStyle(linestyle1))
                            .setMaxLength(maxLength)
            );
        }

        Executor.getInstance().registerChart(
                new ProgressiveChart(200, 130, "Constants", "","step ")
                        .subscribe(new ChartSeries("K1", 0.0).setColor(Color.BLUE).setStyle(ChartType.Scatter))
                        .setMaxLength(maxLength)
        );

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
