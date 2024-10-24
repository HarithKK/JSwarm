package examples.multiagent.leader_election;

import examples.multiagent.leader_election.core.Critarian;
import examples.multiagent.leader_election.core.Drone;
import examples.multiagent.leader_election.core.StateSpaceModel;
import examples.multiagent.leader_election.core.WalkType;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.comparators.ByIndex;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Randoms;

import java.awt.geom.Point2D;
import java.util.*;

public class Main {
    public Algorithm algorithm;
    public Drone utmostLeader;
    final int MAX_LINKS;

    public StateSpaceModel model;

    final int agentsCount;
    final Point2D centerLocation;
    final double r, angularVelocity;
    final double safeRage;
    final double kReject, kAttract;
    public long last_t;

    final private WalkType type;

    public Main(int nLinks, int agentsCount, double cx, double cy, double r, double safeRage, double k1, double k2, double angularVelocity, WalkType type){

        this.MAX_LINKS = nLinks;
        this.agentsCount = agentsCount;
        this.model = new StateSpaceModel(agentsCount);
        this.centerLocation = new Point2D.Double();
        this.centerLocation.setLocation(cx, cy);
        this.r = r;
        this.angularVelocity = angularVelocity;
        this.safeRage = safeRage;
        this.kReject = k2;
        this.kAttract = k1;
        this.type = type;

        initAlgorithm();
    }

    public Main(int nLinks, int agentsCount){
        this(nLinks, agentsCount, 100, 100, 80, 25, 0.01, 0.001, 5, WalkType.CIRCLE);
    }

    private void initAlgorithm() {
        last_t = System.nanoTime();

        algorithm = new Algorithm() {
            @Override
            public void initialize() {

                try {
                    this.addAgents("drones", Drone.class, agentsCount);
                    utmostLeader = findRoot();
                    utmostLeader.rank = 0;
                    utmostLeader.setX(centerLocation.getX());
                    utmostLeader.setY(centerLocation.getY());
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

                        if(type == WalkType.CIRCLE)
                            utmostLeader.getPosition().setValues(new double[]{centerLocation.getX() + r * Math.cos(theta), centerLocation.getY() + r * Math.sin(theta)});
                        else if(type == WalkType.FIX)
                            utmostLeader.getPosition().fixVector(Commons.fill(50, 2), Commons.fill(150,2));
                        else
                            utmostLeader.updateU(type, theta, angularVelocity);
                    }

                    for(int id=0; id<agentsCount; id++){

                        Drone dr = (Drone) getFirstAgents().get(id);
                        if(utmostLeader != null && dr.getIndex() == utmostLeader.getIndex()){
                            continue;
                        }

                        dr.velocity = dr.velocity.operate(org.usa.soc.core.ds.Vector.OPERATOR.MULP, model.K0.getEntry(dr.getIndex(), dr.getIndex()));
                        model.K1.scalarMultiply(0);

                        for(int j = 0; j<model.GA.getRowDimension(); j++){

                            if(j == dr.getIndex()){
                                continue;
                            }

                            if(model.GA.getEntry(dr.getIndex(), j) > 0){
                                if(model.GB.getEntry(dr.getIndex(), j) == 1.0){
                                    int listIndex = findAgentListIndex(j);
                                    Pair<org.usa.soc.core.ds.Vector, Double> data =  getKValue(getFirstAgents().get(listIndex), dr);
                                    model.K1.setEntry(dr.getIndex(), j, data.getSecond());
                                    dr.velocity.updateVector(data.getFirst());
                                }
                                else if(model.GB.getEntry(dr.getIndex(), j) ==0){
                                    int listIndex = findAgentListIndex(j);
                                    Pair<org.usa.soc.core.ds.Vector, Double> data = getK1Value(getFirstAgents().get(listIndex), dr, 0.02, 0.3);
                                    model.K1.setEntry(dr.getIndex(), j, data.getSecond());
                                    dr.velocity.updateVector(data.getFirst());
                                }
                            }
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            private Pair<org.usa.soc.core.ds.Vector, Double> getKValue(AbsAgent d1, AbsAgent d2) {
                double distance = d1.getPosition().getClonedVector().getDistance(d2.getPosition().getClonedVector());
                org.usa.soc.core.ds.Vector velocityVector = d1.getPosition().getClonedVector().operate(org.usa.soc.core.ds.Vector.OPERATOR.SUB, d2.getPosition().getClonedVector());

                if(distance < safeRage){
                    double k = Math.abs(distance - safeRage)*kReject;
                    return new Pair<org.usa.soc.core.ds.Vector, Double>(velocityVector.toNeg().operate(org.usa.soc.core.ds.Vector.OPERATOR.MULP, k), k);
                }else if(distance > safeRage){
                    double k = Math.abs(distance - safeRage)*kAttract;
                    return new Pair<org.usa.soc.core.ds.Vector, Double>(velocityVector.operate(org.usa.soc.core.ds.Vector.OPERATOR.MULP, k), k);
                }else{
                    return new Pair<org.usa.soc.core.ds.Vector, Double>(Randoms.getRandomVector(2, 0,2), 0.0);
                }
            }

            private Pair<org.usa.soc.core.ds.Vector, Double> getK1Value(AbsAgent d1, AbsAgent d2, double K, double i) {
                double distance = d1.getPosition().getClonedVector().getDistance(d2.getPosition().getClonedVector());
                org.usa.soc.core.ds.Vector velocityVector = d1.getPosition().getClonedVector().operate(org.usa.soc.core.ds.Vector.OPERATOR.SUB, d2.getPosition().getClonedVector());

                double k = K*eFactor(i, distance - safeRage, 0.01, 0.5);
                return new Pair<org.usa.soc.core.ds.Vector, Double>(velocityVector.operate(org.usa.soc.core.ds.Vector.OPERATOR.MULP, k), k);
            }

            private double eFactor(double j, double x, double fR, double fA){

                if(x < 0)
                    return -(1+Math.exp(-(j*x-1)))*fR;
                else if(x>0)
                    return (1+Math.exp(-(j*x-1)))*fA;
                else
                    return 0;

                //return i + (-i * Math.exp(x)/(100 + Math.exp(x)));
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
                                            model.GA.setEntry(xi.getIndex(), xj.getIndex(), 1);
                                            model.GA.setEntry(xj.getIndex(), xi.getIndex(), 1);
                                            System.out.println("Link Added ["+xi.getIndex()+","+xj.getIndex()+"]");
                                        }
                                    }else{
                                        if(model.GA.getEntry(xi.getIndex(), xj.getIndex()) == 1 && xi.rank == xj.rank){
                                            System.out.println("Link Removed ["+xi.getIndex()+","+xj.getIndex()+"]");
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
                                Gc = Gc.scalarMultiply(0.0);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void moveUp(Drone a){
        a.moveUpper();
        if(a.getConncetions().isEmpty())
            return;

        for(AbsAgent c: a.getConncetions()){
            moveUp((Drone)c);
        }
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

        int index = new Critarian().selectCritarian(Critarian.Critarians.RANDOM, layer);
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

}
