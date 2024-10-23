package examples.multiagent.leader_election;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.comparators.ByIndex;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.multiagent.view.*;
import org.usa.soc.multiagent.view.Button;
import org.usa.soc.multiagent.view.TextField;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Randoms;
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

    private int MAX_LINKS = 5;

    private StateSpaceModel model;

    public static int agentsCount = 5;

    double av = 0.8;
    public long last_t;



    public MainV11(){

        model = new StateSpaceModel(agentsCount);
        double cx = 100, cy = 100, r = 80, medianDistanceLow = 15, kReject=0.01, kAttact=0.0005;
        Vector c = new Vector(2);

        double[] min = Commons.fill(50, 2), max=Commons.fill(150,2);

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
                    utmostLeader.velocity.setValues(new double[]{0.0, 0.0});

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
                    model.setK0(Commons.fill(0.1, agentsCount));
                    model.setK1(Commons.fill(1, agentsCount));
                    model.setK2(Commons.fill(1, agentsCount));
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

                    if(utmostLeader != null){
                        double theta = Math.toRadians(currentStep % 360);

                        //utmostLeader.getPosition().setValues(new double[]{cx + r * Math.cos(theta), cy + r * Math.sin(theta)});
                        utmostLeader.updateU(Drone.U_WALK_TYPE.RANDOM_THETA, theta, av);
                        //utmostLeader.getPosition().fixVector(min, max);
                    }

                    for(int id=0; id<agentsCount; id++){

                        Drone dr = (Drone) getFirstAgents().get(id);
                        if(utmostLeader != null && dr.getIndex() == utmostLeader.getIndex()){
                            continue;
                        }

                        dr.velocity = dr.velocity.operate(Vector.OPERATOR.MULP, model.K0.getEntry(dr.getIndex(), dr.getIndex()));
                        model.K1.scalarMultiply(0);

                        for(int j = 0; j<model.GA.getRowDimension(); j++){

                            if(j == dr.getIndex()){
                                continue;
                            }

                            if(model.GA.getEntry(dr.getIndex(), j) > 0){
                                if(model.GB.getEntry(dr.getIndex(), j) == 1.0){
                                    int listIndex = findAgentListIndex(j);
                                    Pair<Vector, Double> data =  getKValue(getFirstAgents().get(listIndex), dr);
                                    model.K1.setEntry(dr.getIndex(), j, data.getSecond());
                                    dr.velocity.updateVector(data.getFirst());
                                }
                                else if(model.GB.getEntry(dr.getIndex(), j) ==0){
                                    int listIndex = findAgentListIndex(j);
                                    Pair<Vector, Double> data = getK1Value(getFirstAgents().get(listIndex), dr, -kReject, 6);
                                    model.K1.setEntry(dr.getIndex(), j, data.getSecond());
                                    dr.velocity.updateVector(data.getFirst());
                                }
                            }
                        }
                    }

                }catch (Exception e){
                   //e.printStackTrace();
                }
            }

            private Pair<Vector, Double> getKValue(AbsAgent d1, AbsAgent d2) {
                double distance = d1.getPosition().getClonedVector().getDistance(d2.getPosition().getClonedVector());
                Vector velocityVector = d1.getPosition().getClonedVector().operate(Vector.OPERATOR.SUB, d2.getPosition().getClonedVector());

                if(distance < medianDistanceLow){
                    double k = Math.abs(distance - medianDistanceLow)*kReject;
                    return new Pair<Vector, Double>(velocityVector.toNeg().operate(Vector.OPERATOR.MULP, k), k);
                }else if(distance > medianDistanceLow){
                    double k = Math.abs(distance - medianDistanceLow)*kAttact;
                    return new Pair<Vector, Double>(velocityVector.operate(Vector.OPERATOR.MULP, k), k);
                }else{
                    return new Pair<Vector, Double>(Randoms.getRandomVector(2, 0,2), 0.0);
                }
            }

            private Pair<Vector, Double> getK1Value(AbsAgent d1, AbsAgent d2, double K, double i) {
                double distance = d1.getPosition().getClonedVector().getDistance(d2.getPosition().getClonedVector());
                Vector velocityVector = d1.getPosition().getClonedVector().operate(Vector.OPERATOR.SUB, d2.getPosition().getClonedVector());

                double k = K*eFactor(i, distance - medianDistanceLow);
                return new Pair<Vector, Double>(velocityVector.operate(Vector.OPERATOR.MULP, k), k);
            }

            private double eFactor(double i, double x){
                return i + (-i * Math.exp(x)/(100 + Math.exp(x)));
            }

            private Vector calculateVelocity(AbsAgent d1, AbsAgent d2) {
                Vector vc = d1.getPosition().getClonedVector().operate(Vector.OPERATOR.SUB, d2.getPosition());
                double k = vc.getMagnitude()-medianDistanceLow < 0 ? kReject : kAttact;
                return vc.operate(Vector.OPERATOR.MULP, (vc.getMagnitude()-medianDistanceLow)*k);
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

                int step = 0;
                RealMatrix Gc = null;

                while(true){

                    try {
                        Thread.sleep(50);

                        if (algorithm.isInitialized()){
                            for (int idi = 0; idi < agentsCount; idi++) {
                                if (utmostLeader != null && idi == utmostLeader.getIndex()) {
                                    continue;
                                }
                                for (int idj = idi; idj < agentsCount; idj++) {
                                    if (idj == idi) {
                                        continue;
                                    }
                                    Drone xi = (Drone) algorithm.getFirstAgents().get(idi);
                                    Drone xj = (Drone) algorithm.getFirstAgents().get(idj);
                                    if (xi.getPosition().getClonedVector().operate(Vector.OPERATOR.SUB, xj.getPosition()).getMagnitude() < 30){
                                        if(model.GA.getEntry(xi.getIndex(), xj.getIndex()) == 0 & xi.rank == xj.rank){
                                            //System.out.println("Link Added ["+xi.getIndex()+","+xj.getIndex()+"]");
                                            model.GA.setEntry(xi.getIndex(), xj.getIndex(), 1);
                                            model.GA.setEntry(xj.getIndex(), xi.getIndex(), 1);
                                        }
                                    }else{
                                        if(model.GA.getEntry(xi.getIndex(), xj.getIndex()) == 1 && xi.rank == xj.rank){
                                            //System.out.println("Link Removed ["+xi.getIndex()+","+xj.getIndex()+"]");
                                            model.GA.setEntry(xi.getIndex(), xj.getIndex(), 0);
                                            model.GA.setEntry(xj.getIndex(), xi.getIndex(), 0);
                                        }
                                    }
                                }
                            }
                            model.derive();
                            if(Gc == null){
                                 Gc= MatrixUtils.createRealMatrix(model.A.getRowDimension(), model.A.getColumnDimension());
                            }
                            Gc = Gc.add(model.A.power(step).multiply(model.B).multiply(model.B.transpose()).multiply(model.A.transpose().power(step)));
                            step += 1;
                            if(step > 10){
                                step = 0;
                                Executor.getInstance().updateData("Gc table", Gc);
                                Executor.getInstance().updateData("A table", model.GA);
                                Gc.scalarMultiply(0.0);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

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
                            Executor.getInstance().updateData("Agents Count:",String.valueOf(agentsCount));

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

    public void performRandomOnlyForFirstLayer() {

        List<Drone> layer = new ArrayList<>();

        for(AbsAgent a: algorithm.getFirstAgents()){
            Drone d = (Drone) a;
            if(d.rank == 1)
                layer.add(d);
        }

        int index = new Critarian().selectCritarian(Critarians.RANDOM, layer);
        utmostLeader = layer.get(index);
        moveUp(utmostLeader);

        for(AbsAgent a: algorithm.getFirstAgents()){
            Drone d = (Drone) a;
            if(d.rank == 1)
                utmostLeader.addConnection(d);
        }

        Executor.getInstance().getChartView().getView2D().redrawNetwork();
        model.GA = model.GA.scalarMultiply(0);
        model.GB = model.GB.scalarMultiply(0);
        formStateSpaceModel();

    }

    private void moveUp(Drone a){
        a.moveUpper();
        if(a.getConncetions().isEmpty())
            return;

        for(AbsAgent c: a.getConncetions()){
            moveUp((Drone)c);
        }
    }

    private void updateLeaderShip(List<Drone> layer, int i, Drone preferredLeaderAgent) {

        if(layer.isEmpty()){
            return;
        }

        int index = new Critarian().selectCritarian(Critarians.RANDOM, layer);

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
        int listIndex = algorithm.findAgentListIndex(index);
        Executor.getInstance().getChartView().getView2D().removeAgent(listIndex);
        model.replace(index, 0);
        if(index == 0)
            utmostLeader = null;

        Executor.getInstance().getChartView().getView2D().redrawNetwork();
        agentsCount--;
    }

    public static void main(String[] args) {
        MainV11 m = new MainV11();
        Executor.getInstance().executePlain2D("LF", m.algorithm, 700, 700, new Margins(0, 200, 0, 1000));

        Executor.getInstance().registerTextButton(new Button("Calculate Gc").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                RealMatrix ms = m.model.calcContineousControllabilityGramian(0, 100);
//                Executor.getInstance().updateData("Gc", m.model.getGcRank() + " is Model Controllable: " + m.model.isModelControllable());
//                System.out.println(StringFormatter.toString(ms));

                RealMatrix dc = m.model.calcDiscreteControllabilityGramian(0, 5);
                System.out.println(StringFormatter.toString(dc));
            }
        }));


        Executor.getInstance().registerTable(new Table("Gc table", 5, 5));
        Executor.getInstance().registerTable(new Table("A table", 5, 5));
//        Executor.getInstance().registerTextBox(new TextField("Agents Count:"));
        Executor.getInstance().registerTextButton(new Button("Remove Leader 0").addAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m.removeAgent(m.algorithm.findAgentListIndex(m.utmostLeader.getIndex()));
            }
        }));
//        Executor.getInstance().registerTextButton(new Button("Remove and Select Leader 0").addAction(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                m.removeAgent(m.algorithm.findAgentListIndex(m.utmostLeader.getIndex()));
//                m.performRandomOnlyForFirstLayer();
//            }
//        }));
//        Executor.getInstance().registerChart(new ProgressiveChart(300, 300, "Energy", "E", "Step")
//                .subscribe(new ChartSeries("Avg_Comm_E", 0))
//                .subscribe(new ChartSeries("Avg_Control_E", 0).setColor(Color.RED))
//                .setLegend(true)
//                .setLegendPosition("S", false)
//                .setMaxLength(100));
//
//
//

    }

}
