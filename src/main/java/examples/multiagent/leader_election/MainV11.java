package examples.multiagent.leader_election;

import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.comparators.ByIndex;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.multiagent.view.Button;
import org.usa.soc.multiagent.view.ChartSeries;
import org.usa.soc.multiagent.view.ProgressiveChart;
import org.usa.soc.multiagent.view.TextField;
import org.usa.soc.util.Commons;
import org.usa.soc.util.StringFormatter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.stream.Collectors;

public class MainV11 {

    private static final double K = 0.0001;
    Algorithm algorithm;

    Drone utmostLeader;

    private int MAX_LINKS = 8;

    private StateSpaceModel model;

    public static int agentsCount = 20;

    double av = 5;
    public long last_t;

    public MainV11(){

        model = new StateSpaceModel(agentsCount);
        double cx = 100, cy = 100, r = 80, md = 15, vc_minus=0.015, vc_plus=0.0001;

        last_t = System.nanoTime();

        algorithm = new Algorithm() {
            @Override
            public void initialize() {

                try {
                    this.addAgents("drones", Drone.class, agentsCount);
                    utmostLeader = (Drone) getFirstAgents().get(0);// findRoot();
                    utmostLeader.rank = 0;
                    utmostLeader.setX(cx);
                    utmostLeader.setY(cy);
                    utmostLeader.velocity.setValues(new double[]{0.0, 1.0});

                    Queue<AbsAgent> bfsQueue = new ArrayDeque<>();
                    bfsQueue.add(utmostLeader);

                    do{
                        List<AbsAgent> l = createNetwork(bfsQueue);
                        for(AbsAgent a: l){
                            bfsQueue.add(a);
                        }
                    }while(!bfsQueue.isEmpty());

                    getFirstAgents().sort(new ByIndex());

                    // Fill A and B Matrices
                    model.setK0(Commons.fill(1, agentsCount));
                    model.setK1(Commons.fill(1, agentsCount));
                    model.setKR(Commons.fill(1, agentsCount));
                    formStateSpaceModel();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


            private List<AbsAgent> createNetwork(Queue<AbsAgent> q) {

                List<AbsAgent> nextLayer = new ArrayList<>();

                while(!q.isEmpty()){
                    Drone root = (Drone)q.poll();
                    List<AbsAgent> agents = getFirstAgents();
                    agents.sort(new Comparator<AbsAgent>() {
                        @Override
                        public int compare(AbsAgent o1, AbsAgent o2) {
                            if (o1.getPosition().getDistance(root.getPosition()) < o2.getPosition().getDistance(root.getPosition())) {
                                return -1;
                            }else{
                                return 1;
                            }
                        }
                    });

                    int max_count = MAX_LINKS;

                    for(AbsAgent a: agents){
                        if(max_count < 0){
                            break;
                        }
                        if(root.getConncetions().contains(a)){
                            continue;
                        }
                        Drone d1 = (Drone)a;
                        if(d1.rank < 0){
                            d1.rank = root.rank+1;
                            root.addConnection(d1);
                            nextLayer.add(d1);
                            max_count--;
                        }
                    }
                }

                return nextLayer;
            }

            private Drone findRoot() {
                AbsAgent root= getFirstAgents().get(0);
                double m = root.getPosition().getMagnitude();
                for(AbsAgent agent: getFirstAgents()){
                    if(agent.getPosition().getMagnitude() > m){
                        root = agent;
                        m = agent.getPosition().getMagnitude();
                    }
                }
                return (Drone) root;
            }

            @Override
            public void step() throws Exception {

                try {
                    for(int i=0; i<agentsCount; i++){
                        Drone dr = (Drone) getFirstAgents().get(i);
                        if(utmostLeader != null && dr.getIndex() == utmostLeader.getIndex()){
                            double theta = Math.toRadians(currentStep % 360);
                            utmostLeader.getPosition().setValues(new double[]{cx + r * Math.cos(theta), cy + r * Math.sin(theta)});
                            utmostLeader.velocity.setValues(new double[]{0,0});
                            continue;
                        }

                        Drone leader = (Drone) getFirstAgents().get(getLeaderIndex(dr.getIndex()));
                        dr.velocity = calculateVelocity(leader, dr).operate(Vector.OPERATOR.MULP, 30);

                        for(int j = 0; j<model.GA.getRowDimension(); j++){
                            if(j == leader.getIndex() || j == dr.getIndex()){
                                continue;
                            }
                            if(model.GA.getEntry(dr.getIndex(), j) > 0 && model.GB.getEntry(dr.getIndex(), j) != -1)
                                dr.velocity.updateVector(calculateVelocity(dr, getFirstAgents().get(j)).toNeg());
                        }

                    }

                }catch (Exception e){

                }
            }

            private Vector calculateVelocity(AbsAgent d1, AbsAgent d2) {
                Vector vc = d1.getPosition().getClonedVector().operate(Vector.OPERATOR.SUB, d2.getPosition());
                double k = vc.getMagnitude()-md < 0 ? vc_minus : vc_plus;
                return vc.operate(Vector.OPERATOR.MULP, (vc.getMagnitude()-md)*k);
            }

            private int getLeaderIndex(int index) {
                for(int i = 0; i<model.GB.getRowDimension(); i++){
                    if(model.GB.getEntry(index, i) == 1.0){
                        return i;
                    }
                }
                return -1;
            }

        };

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    try{
                        Thread.sleep(100);
                        if(algorithm.isInitialized())
                        {
                            double mean_control_e = 0, mean_comm_e =0;
                            for(AbsAgent a: algorithm.getFirstAgents()){
                                Drone d = (Drone)a;
                                d.updateEnergyProfile();
                                mean_control_e += d.controlEnergy;
                                mean_comm_e += d.commEnergy;
                            }
                            Executor.getInstance().updateData("Energy","Avg_Comm_E", mean_comm_e/algorithm.getFirstAgents().size());
                            Executor.getInstance().updateData("Energy","Avg_Control_E",mean_control_e/algorithm.getFirstAgents().size());

                       }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void formStateSpaceModel() {
        for(int i=0; i< agentsCount; i++){
            for(int j=0; j< agentsCount; j++){
                Drone ai = (Drone)algorithm.getFirstAgents().get(i);
                Drone aj = (Drone)algorithm.getFirstAgents().get(j);

                if(i == j){
                    continue;
                }

                if(ai.rank == aj.rank){
                    model.GA.setEntry(ai.getIndex(), aj.getIndex(), 1);
                    model.GA.setEntry(aj.getIndex(), ai.getIndex(), 1);
                    continue;
                }
                if(ai.getConncetions().contains(aj)){
                    model.GA.setEntry(ai.getIndex(), aj.getIndex(), 1);
                    model.GA.setEntry(aj.getIndex(), ai.getIndex(), 1);
                    if(ai.rank < aj.rank){
                        model.GB.setEntry(ai.getIndex(), aj.getIndex(), -1);
                        model.GB.setEntry(aj.getIndex(), ai.getIndex(), 1);
                    }
                }
            }
        }
        model.derive();
    }

    public void performRandomLE() {

        List<Drone> layeredList = new ArrayList<>();

        for(AbsAgent a: algorithm.getFirstAgents()){
            Drone d = (Drone) a;
            if(d.rank == 1)
                layeredList.add(d);
        }

        updateLeaderShip(layeredList, 1, utmostLeader);
//        model.GA.scalarMultiply(0);
//        model.GB.scalarMultiply(0);
//        formStateSpaceModel();
    }

    private void updateLeaderShip(List<Drone> layer, int i, Drone preferredLeaderAgent) {

        if(layer.isEmpty()){
            return;
        }

        int index = new Critarian().selectCritarian(Critarians.RANDOM, layer, i);

        layer.get(index).moveUpper();

        if(preferredLeaderAgent == null){
            utmostLeader = layer.get(index);
            List<Drone> tmpConnections = utmostLeader.getConncetions().stream().map(d->(Drone)d).collect(Collectors.toList());
            utmostLeader.getConncetions().clear();
            for(Drone d: layer){
                if(d.getIndex() != index){
                    utmostLeader.addConnection(d);
                }
            }
            updateLeaderShip(tmpConnections, i+1, utmostLeader);
        }
        else{
            preferredLeaderAgent.addConnection(layer.get(index));
            List<Drone> tmpConnections = layer.get(index).getConncetions().stream().map(d->(Drone)d).collect(Collectors.toList());
            layer.get(index).getConncetions().clear();
            for(Drone d: layer){
                if(d.getIndex() != index){
                    preferredLeaderAgent.addConnection(d);
                }
            }
            updateLeaderShip(tmpConnections, i+1, layer.get(index));
        }
    }

    public void removeAgent(int index){
        algorithm.getFirstAgents().remove(index);
        model.replace(index, 0);
        if(index == 0)
            utmostLeader = null;

        Executor.getInstance().getChartView().getView2D().redrawNetwork();
        agentsCount--;
    }

    public static void main(String[] args) {
        MainV11 m = new MainV11();
        Executor.getInstance().executePlain2D("LF", m.algorithm, 700, 700, new Margins(0, 200, 0, 200));

        Executor.getInstance().registerTextButton(new Button("Calculate Gc").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RealMatrix ms = m.model.calcControllabilityGramian(0, 100);
                Executor.getInstance().updateData("Gc", m.model.getGcRank() + " is Model Controllable: " + m.model.isModelControllable());
                System.out.println(StringFormatter.toString(ms));
            }
        }));
        Executor.getInstance().registerTextButton(new Button("Remove Leader 0").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m.removeAgent(0);
            }
        }));
        Executor.getInstance().registerTextButton(new Button("Select Leader 0").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m.removeAgent(0);
                m.performRandomLE();
            }
        }));
        Executor.getInstance().registerTextBox(new TextField("Gc"));
        Executor.getInstance().registerChart(new ProgressiveChart(600, 300, "Energy", "E", "Step")
                .subscribe(new ChartSeries("Avg_Comm_E", 0))
                .subscribe(new ChartSeries("Avg_Control_E", 0).setColor(Color.RED))
                .setLegend(true)
                .setMaxLength(100));;

    }

}
