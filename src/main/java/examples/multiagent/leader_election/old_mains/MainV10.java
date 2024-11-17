package examples.multiagent.leader_election.old_mains;

import examples.multiagent.leader_election.core.Drone;
import examples.multiagent.leader_election.core.StateSpaceModel;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.comparators.ByIndex;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.multiagent.view.Button;
import org.usa.soc.multiagent.view.ChartSeries;
import org.usa.soc.multiagent.view.ProgressiveChart;
import org.usa.soc.multiagent.view.TextField;
import org.usa.soc.util.Commons;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class MainV10 {

    private static final double K = 0.0001;
    Algorithm algorithm;

    Drone utmostLeader;

    private int MAX_LINKS = 8;

    private StateSpaceModel model;

    public static int agentsCount = 30;

    double av = 5;
    public long last_t;

    public MainV10(int count){

        model = new StateSpaceModel(count);
        double cx = 100, cy = 100, r = 80, md = 15, vc_minus=0.015, vc_plus=0.0001;

        last_t = System.nanoTime();

        algorithm = new Algorithm() {
            @Override
            public void initialize() {

                try {
                    this.addAgents("drones", Drone.class, count);
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

                    for(int i=0; i< count; i++){
                        for(int j=0; j< count; j++){
                            Drone ai = (Drone)getFirstAgents().get(i);
                            Drone aj = (Drone)getFirstAgents().get(j);

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

                    model.setK0(Commons.fill(1, count));
                    model.setK1(Commons.fill(1, count));
                    model.setKR(Commons.fill(1, count));
                    model.derive();

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
                    for(int i=0; i<count; i++){
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

    public void removeAgent0(){
        model.replace(0, 0);
        utmostLeader = null;
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
        MainV10 m = new MainV10(agentsCount);
        Executor.getInstance().executePlain2D("LF", m.algorithm, 700, 700, new Margins(0, 200, 0, 200));
        Executor.getInstance().AddCustomActions("R-0", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m.removeAgent(0);
            }
        }, true);

        Executor.getInstance().registerTextButton(new Button("Press").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m.model.calcContineousControllabilityGramian(0, 100);
                Executor.getInstance().updateData("Gc", m.model.getGcRank() + " is Model Controllable: " + m.model.isModelControllable());

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
