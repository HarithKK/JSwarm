package examples.multiagent.drone_network_heatbeat;

import examples.multiagent.common.NetworkGenerator;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Markers;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.multiagent.view.ChartSeries;
import org.usa.soc.multiagent.view.ProgressiveChart;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.PrintToFile;

import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

class StateMap{
    Vector velocity;
    Vector position;

    public StateMap(Vector p, Vector v){
        position = p;
        velocity = v;
    }

    public StateMap(){
        position = new Vector(2).resetAllValues(0.0);
        velocity = new Vector(2).resetAllValues(0.0);
    }
}

class DoubleStateMap{
    double velocity = 0.0;
    double position = 0.0;

    int index = 0;

    public DoubleStateMap(double p, double v){
        position = p;
        velocity = v;
    }

    public DoubleStateMap toAbs(){
        this.position = Math.abs(position);
        this.velocity = Math.abs(velocity);
        return this;
    }

    public DoubleStateMap(){}
    public void update(DoubleStateMap m){
        velocity += m.velocity;
        position += m.position;
    }

    public DoubleStateMap(int i, double p, double v){
        index = i;
        velocity = v;
        position = p;
    }
}

public class Quin {

    public static void main(String[] args) {

        Controller.dAgentGroup.setMarkerColor(Color.GREEN);
        Controller.cAgentGroup.setMarker(Markers.CROSS);
        Controller.cAgentGroup.setMarkerColor(Color.RED);

        Serializer serializer = new Serializer(PrintToFile.getInstance().build("data/"+ new Date().getTime()+".json"));

        Algorithm algorithm = new Algorithm() {
            @Override
            public void initialize() {
                double ix = 100.0;
                double iy = 10.0;

                Controller.dronesMap = NetworkGenerator.generateStatic(getMargins(), ix, iy);
                //Controller.dronesMap = new NetworkGenerator(ix, iy,50, 10, getMargins()).getDroneMap();
                //Controller.dronesMap = NetworkGenerator.generateStaticlayers4(getMargins(), ix, iy);
                try {
                    for(Integer i : Controller.dronesMap.keySet()){
                        Controller.agentGroup.addAgent(Controller.dronesMap.get(i));
                    }
                    this.agents.put(Controller.agentGroup.name,Controller.agentGroup);
                    this.agents.put(Controller.dAgentGroup.name, Controller.dAgentGroup);
                    this.agents.put(Controller.cAgentGroup.name, Controller.cAgentGroup);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                this.setInterval(200);
            }

            public StateMap calculateDtl(int t, int l, int k){

                StateMap vt = Controller.stateSamplesMap.get(t).get(k);
                StateMap vl = Controller.stateSamplesMap.get(l).get(k);
                return new StateMap(
                        vt.position.operate(Vector.OPERATOR.SUB, vl.position),
                        vt.velocity.operate(Vector.OPERATOR.SUB, vl.velocity)
                );
            }

            @Override
            public void run() {

                if(Controller.incrementor <= Controller.K){
                    Iterator<DroneAgent> agentsIterator = this.agents.get(Controller.agentGroup.name).getAgents().stream().map(d -> (DroneAgent)d).iterator();
                    while (agentsIterator.hasNext()){
                        updateStateList(agentsIterator.next());
                    }
                    Iterator<DroneAgent> agentsIterator2 = this.agents.get(Controller.dAgentGroup.name).getAgents().stream().map(d -> (DroneAgent)d).iterator();
                    while (agentsIterator2.hasNext()){
                        updateStateList(agentsIterator2.next());
                    }
                    Controller.incrementor++;
                }else{
                    Controller.incrementor = 0;
                    Controller.pKtlMap.values().stream().forEach(i -> i.clear());

                    try{
                        for(int i=0; i< Controller.stateSamplesMap.size(); i++){
                            for(int j=0; j< Controller.stateSamplesMap.size(); j++){

                                DoubleStateMap upperSum = new DoubleStateMap();
                                DoubleStateMap lowerSum = new DoubleStateMap();

                                if(Controller.dronesMap.containsKey(i) && Controller.dronesMap.containsKey(j)) {
                                    for (int k = 1; k < Controller.K; k++) {
                                        StateMap vk = calculateDtl(i, j, k);
                                        StateMap vk1 = calculateDtl(i, j, k - 1);

                                        upperSum.update(calculateInnerProduct(vk, vk1));
                                        lowerSum.update(calculateInnerProduct(vk1, vk1));
                                    }
                                }else{
                                    upperSum.velocity = 1;
                                    upperSum.position = 1;
                                    lowerSum.velocity = 1;
                                    lowerSum.position = 1;
                                }

                                if (!Controller.pKtlMap.containsKey(i)) {
                                    Controller.pKtlMap.put(i, new ArrayList<>());
                                }
                                Controller.pKtlMap.get(i).add(new DoubleStateMap(
                                        j,
                                        upperSum.position / lowerSum.position,
                                        upperSum.velocity / lowerSum.velocity
                                ));
                            }
                        }

//                        for(List<DoubleStateMap> l: Controller.pKtlMap.values()){
//                            for(DoubleStateMap m: l){
//                                m.position = Mathamatics.round(Math.abs(m.position -1), 3);
//                                m.velocity = Mathamatics.round(Math.abs(m.velocity -1), 3);
//                            }
//                        }

                        Executor.getInstance().updateData("Pklt-v", "0-1", Controller.pKtlMap.get(0).get(1).velocity);
                        Executor.getInstance().updateData("Pklt-p", "0-1", Controller.pKtlMap.get(0).get(1).position);

                        Executor.getInstance().updateData("Pklt-v", "1-2", Controller.pKtlMap.get(1).get(2).velocity);
                        Executor.getInstance().updateData("Pklt-p", "1-2", Controller.pKtlMap.get(1).get(2).position);

                        Executor.getInstance().updateData("Pklt-v", "6-7", Controller.pKtlMap.get(6).get(7).velocity);
                        Executor.getInstance().updateData("Pklt-p", "6-7", Controller.pKtlMap.get(6).get(7).position);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Controller.stateSamplesMap.values().stream().forEach(i -> i.clear());
                }

                if(Controller.logging)
                    serializer.build(this).save();
            }

            private DoubleStateMap calculateInnerProduct(StateMap vk, StateMap vk1) {
                return new DoubleStateMap(vk.position.innerProduct(vk1.position), vk.velocity.innerProduct(vk1.velocity)).toAbs();
            }

            public void updateStateList(DroneAgent agent) {
                if(!Controller.stateSamplesMap.containsKey(agent.index)){
                    Controller.stateSamplesMap.put(agent.index, new ArrayList<>());
                }
                Controller.stateSamplesMap.get(agent.index).add(new StateMap(agent.getPosition().getClonedVector(), agent.velocity.getClonedVector()));
            }
        };

        Executor.getInstance().registerChart(
                new ProgressiveChart(200, 130, "Pklt-v", "Pklt-v","kTau")
                        .subscribe(new ChartSeries("0-1", 0.0).setColor(Color.BLUE))
                        .subscribe(new ChartSeries("1-2", 0.0).setColor(Color.GREEN))
                        .subscribe(new ChartSeries("6-7", 0.0).setColor(Color.ORANGE))
                        .setLegend(true)

        );

        Executor.getInstance().registerChart(
                new ProgressiveChart(200, 130, "Pklt-p", "Pklt-p","kTau")
                        .subscribe(new ChartSeries("0-1", 0.0).setColor(Color.BLUE))
                        .subscribe(new ChartSeries("1-2", 0.0).setColor(Color.GREEN))
                        .subscribe(new ChartSeries("6-7", 0.0).setColor(Color.ORANGE))
                        .setLegend(true)
        );

        for(int i=0; i< 12;i++){
            Executor.getInstance().registerChart(
                    new ProgressiveChart(200, 130, String.valueOf(i), "","Drone "+i)
                            .subscribe(new ChartSeries("dOmega", 0.0).setColor(Color.BLUE))
                            .subscribe(new ChartSeries("dUOmega", 0.0).setColor(Color.GREEN))
                            .setMaxLength(50)
            );
        }

        Executor.getInstance().AddCustomActions("V", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Integer index: Controller.pKtlMap.keySet()){
                    System.out.print(index + " [");
                    for(DoubleStateMap map: Controller.pKtlMap.get(index)) {
                        System.out.print("("+map.index+": "+ Mathamatics.round(map.position,4) +","+Mathamatics.round(map.velocity,4)+") ");
                    }
                    System.out.println(" ]");
                }
            }
        }, true);

        Executor.getInstance().AddCustomCheckBox("Log", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.logging = !Controller.logging;
            }
        }, true);

        Executor.getInstance().AddCustomActions("Disconnect", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // We can remove the drone from network for our implementation, bu not for Qian et al.
                // Thus we introduced a variable isDisconnected for that.

                DroneAgent a = Controller.dronesMap.get(Controller.DISCONNECTED_KEY);
                // remove 0-1 link
                for(int i =0; i<a.edge.A.length; i++){
                    if(a.edge.A[i] == 1){
                        Controller.dronesMap.get(i).edge.A[a.index] = 0;
                        Controller.dronesMap.get(i).edge.B[a.index] = 0;
                        a.isDisconnected = true;
                        Controller.dronesMap.remove(a);
                        Controller.dAgentGroup.addAgent(a);
                    }
                }
            }
        }, true);

        Executor.getInstance().AddCustomActions("Crash", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // We can remove the drone from network for our implementation, bu not for Qian et al.
                // Thus we introduced a variable isDisconnected for that.

                DroneAgent a = Controller.dronesMap.remove(Controller.DISCONNECTED_KEY);
                algorithm.getAgents("Drones").removeAgent(a);
                Controller.cAgentGroup.getAgents().add(a);
                algorithm.getAgents("Crashed Agents").addAgent(a);
            }
        }, true);

        Executor.getInstance().AddCustomActions("5-0", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // We can remove the drone from network for our implementation, bu not for Qian et al.
                // Thus we introduced a variable isDisconnected for that.

                // Agent 6
                DroneAgent a = Controller.dronesMap.get(6);
                a.edge.A = new int[]{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                a.edge.B = new int[]{-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

                // Agent 0
                a = Controller.dronesMap.get(0);
                a.edge.A = new int[]{0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0};
                a.edge.B = new int[]{0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0};

                // Agent 1
                a = Controller.dronesMap.get(1);
                a.edge.A = new int[]{1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0};
                a.edge.B = new int[]{-1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0};

                // Agent 2
                a = Controller.dronesMap.get(2);
                a.edge.A = new int[]{1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0};
                a.edge.B = new int[]{-1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0};

                // Agent 5
                a = Controller.dronesMap.get(5);
                a.edge.A = new int[]{0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0};
                a.edge.B = new int[]{0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

                // Agent 7
                a = Controller.dronesMap.get(7);
                a.edge.A = new int[]{0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0};
                a.edge.B = new int[]{0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            }
        }, true);

        Executor.getInstance().executePlain2D("Quin/Heartbeat Algorithm",algorithm, 700, 700, new Margins(-50, 300, -100, 1500));
    }
}
