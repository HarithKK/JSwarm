package examples.multiagent.leader_election;

import examples.multiagent.leader_election.core.Critarian;
import examples.multiagent.leader_election.core.Drone;
import examples.multiagent.leader_election.core.data_structures.StateSpaceModel;
import examples.multiagent.leader_election.core.data_structures.WalkType;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.comparators.ByIndex;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Randoms;

import java.awt.geom.Point2D;
import java.util.*;

public class Main {
    public static Algorithm algorithm;
    public Drone utmostLeader;
    public final int partialLinks, controlLinks;

    public StateSpaceModel model;

    public int agentsCount;
    final Point2D centerLocation;
    public final double r, angularVelocity;
    public final double safeRage;
    public final double kReject, kAttract;
    public long last_t;

    public final WalkType type;

    private double freeMemory;

    public List<Point2D> pf = new ArrayList<>();

    public Main(int ncLinks, int npLinks, int agentsCount, double cx, double cy, double r, double safeRage, double k1, double k2, double angularVelocity, WalkType type){

        this.partialLinks = npLinks;
        this.controlLinks = ncLinks;
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

        initAlgorithm(-1);
    }

    public Main(int ncLinks, int npLinks, int agentsCount){
        this(ncLinks, npLinks, agentsCount, 100, 100, 80, 25, 0.01, 0.001, 5, WalkType.CIRCLE);
    }

    public Main(int ncLinks, int npLinks, int nLayers, double safeRage, double k1, double k2, WalkType type){

        System.gc();
        this.partialLinks = npLinks;
        this.controlLinks = ncLinks;
        this.centerLocation = new Point2D.Double();
        this.centerLocation.setLocation(100, 100);
        this.r = 25;
        this.angularVelocity = 5;
        this.safeRage = safeRage;
        this.kReject = k2;
        this.kAttract = k1;
        this.type = type;

        initAlgorithm(nLayers);
    }

    private void initAlgorithm(int maxLayer) {
        last_t = System.nanoTime();

        algorithm = new Algorithm() {
            @Override
            public void initialize() {
                try {
                    if(maxLayer < 0){
                        this.addAgents("drones", Drone.class, agentsCount);
                        utmostLeader = findRoot();
                    }
                    utmostLeader = new Drone();
                    utmostLeader.rank = 0;
                    utmostLeader.setX(centerLocation.getX());
                    utmostLeader.setY(centerLocation.getY());
                    utmostLeader.velocity.setValues(new double[]{0.0, 0.0});

                     // find Second Layer
                    Queue<AbsAgent> bfsQueue = new ArrayDeque<>();
                    bfsQueue.add(utmostLeader);

                    if(maxLayer < 0){
                        do{
                            List<AbsAgent> l = createNetwork(bfsQueue);
                            for(AbsAgent a: l){
                                bfsQueue.add(a);
                            }
                        }while(!bfsQueue.isEmpty());
                    }else{
                        int index = 0;
                        while(!bfsQueue.isEmpty()){
                            Drone ag = (Drone)bfsQueue.poll();
                            this.addAgent("drones", ag);

                            if(ag.rank==maxLayer){
                                continue;
                            }else{
                                int mx = (int)(controlLinks*0.3);
                                if(ag.rank==0){
                                    mx = controlLinks;
                                    // Randoms.rand((int)(controlLinks*0.3), controlLinks-1);
                                }
                                for(int i=0; i<mx;i++){
                                    Drone d = new Drone();
                                    d.setIndex(++index);
                                    d.initPosition(getMargins());
                                    d.rank = ag.rank+1;
                                    ag.addConnection(d);
                                    bfsQueue.add(d);
                                }
                            }
                        }
                        agentsCount = getFirstAgents().size();
                        model = new StateSpaceModel(agentsCount);
                    }

                    getFirstAgents().sort(new ByIndex());
                    System.out.println("Agent Count = "+agentsCount);

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

                    int max_count = controlLinks;

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
                        else if(type == WalkType.STILL)
                            utmostLeader.hashCode();
                        else if(type == WalkType.FORWARD)
                            utmostLeader.velocity.setValues(new double[]{0, 2.5});
                        else if(type == WalkType.FOUTYFIVE)
                            utmostLeader.velocity.setValues(new double[]{1, 1});
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
                                    Pair<org.usa.soc.core.ds.Vector, Double> data = getK1Value(getFirstAgents().get(listIndex), dr, 0.05, 0.3);
                                    model.K1.setEntry(dr.getIndex(), j, data.getSecond());
                                    dr.velocity.updateVector(data.getFirst());
                                }
                            }
                        }
                    }

                    updateGCS();
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

                double k = K*eFactor(i, distance - safeRage, 0.005, 0.05);
                return new Pair<org.usa.soc.core.ds.Vector, Double>(velocityVector.operate(org.usa.soc.core.ds.Vector.OPERATOR.MULP, k), k);
            }

            private double eFactor(double j, double x, double fR, double fA){

                if(x < 0)
                    return -Math.exp(-(j*x-1))*fR;
                else if(x>0)
                    return (1+Math.exp(-(j*x-1)))*fA;
                else
                    return 0;
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

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                int step = 0;
//                RealMatrix Gc = null;
//
//                while(true){
//
//                    try {
//                        Thread.sleep(10);
//
//                        if (algorithm.isInitialized() && !algorithm.isPaused()){
//
//                            for (int idi = 0; idi < agentsCount; idi++){
//                                if (utmostLeader != null && idi == utmostLeader.getIndex()) {
//                                    continue;
//                                }
//                                Drone xi = (Drone) algorithm.getFirstAgents().get(idi);
//                                for (int idj = idi; idj < agentsCount; idj++){
//                                    if (idj == idi) {
//                                        continue;
//                                    }
//                                    Drone xj = (Drone) algorithm.getFirstAgents().get(idj);
//                                    if(xi.rank == xj.rank){
//                                        model.GA.setEntry(xi.getIndex(), xj.getIndex(), 0);
//                                        model.GA.setEntry(xj.getIndex(), xi.getIndex(), 0);
//                                    }
//                                }
//                            }
//
//                            for (int idi = 0; idi < agentsCount; idi++){
//                                if (utmostLeader != null && idi == utmostLeader.getIndex()) {
//                                    continue;
//                                }
//                                ((Drone) algorithm.getFirstAgents().get(idi)).nLayeredLinks =0;
//                            }
//
//                            for (int idi = 0; idi < agentsCount; idi++) {
//                                if (utmostLeader != null && idi == utmostLeader.getIndex()) {
//                                    continue;
//                                }
//                                Drone xi = (Drone) algorithm.getFirstAgents().get(idi);
//                                List<Drone> tmpDrones = new ArrayList<>();
//                                for (int idj = idi; idj < agentsCount; idj++) {
//                                    if (idj == idi) {
//                                        continue;
//                                    }
//                                    Drone xj = (Drone) algorithm.getFirstAgents().get(idj);
//                                    if(xi.rank == xj.rank){
//                                        tmpDrones.add(xj);
//                                    }
//                                }
//
//                                tmpDrones.sort(new Comparator<AbsAgent>() {
//                                    @Override
//                                    public int compare(AbsAgent o1, AbsAgent o2) {
//                                        if (o1.getPosition().getDistance(xi.getPosition()) < o2.getPosition().getDistance(xi.getPosition())) {
//                                            return -1;
//                                        }else{
//                                            return 1;
//                                        }
//                                    }
//                                });
//
//                                for(int ii=0; ii<tmpDrones.size();ii++){
//                                    Drone xj = (Drone)tmpDrones.get(ii);
//                                    if(xi.nLayeredLinks < partialLinks && xj.nLayeredLinks < partialLinks){
//                                        model.GA.setEntry(xi.getIndex(), xj.getIndex(), 1);
//                                        model.GA.setEntry(xj.getIndex(), xi.getIndex(), 1);
//                                        xi.nLayeredLinks++;
//                                        xj.nLayeredLinks++;
//                                        //System.out.println("Link Added ["+xi.getIndex()+","+xj.getIndex()+"]");
//                                    }else{
//                                        break;
//                                    }
//                                }
//                                algorithm.getFirstAgents().sort(new ByIndex());
//                            }
//                            model.derive();
//                            if(Gc == null){
//                                Gc= MatrixUtils.createRealMatrix(model.A.getRowDimension(), model.A.getColumnDimension());
//                            }
//                            Gc = Gc.add(model.A.power(step).multiply(model.B).multiply(model.B.transpose()).multiply(model.A.transpose().power(step)));
//                            step += 1;
//                            if(step > 10){
//                                step = 0;
//                                Gc = Gc.scalarMultiply(0.0);
//                            }
//                        }
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
    }

    private void updateGCS(){
        int step = 0;
        RealMatrix Gc = null;

        for (int idi = 0; idi < agentsCount; idi++){
            if (utmostLeader != null && idi == utmostLeader.getIndex()) {
                continue;
            }
            Drone xi = (Drone) algorithm.getFirstAgents().get(idi);
            for (int idj = idi; idj < agentsCount; idj++){
                if (idj == idi) {
                    continue;
                }
                Drone xj = (Drone) algorithm.getFirstAgents().get(idj);
                if(xi.rank == xj.rank){
                    model.GA.setEntry(xi.getIndex(), xj.getIndex(), 0);
                    model.GA.setEntry(xj.getIndex(), xi.getIndex(), 0);
                }
            }
        }

        for (int idi = 0; idi < agentsCount; idi++){
            if (utmostLeader != null && idi == utmostLeader.getIndex()) {
                continue;
            }
            ((Drone) algorithm.getFirstAgents().get(idi)).nLayeredLinks =0;
        }

        for (int idi = 0; idi < agentsCount; idi++) {
            if (utmostLeader != null && idi == utmostLeader.getIndex()) {
                continue;
            }
            Drone xi = (Drone) algorithm.getFirstAgents().get(idi);
            List<Drone> tmpDrones = new ArrayList<>();
            for (int idj = idi; idj < agentsCount; idj++) {
                if (idj == idi) {
                    continue;
                }
                Drone xj = (Drone) algorithm.getFirstAgents().get(idj);
                if(xi.rank == xj.rank){
                    tmpDrones.add(xj);
                }
            }

            tmpDrones.sort(new Comparator<AbsAgent>() {
                @Override
                public int compare(AbsAgent o1, AbsAgent o2) {
                    if (o1.getPosition().getDistance(xi.getPosition()) < o2.getPosition().getDistance(xi.getPosition())) {
                        return -1;
                    }else{
                        return 1;
                    }
                }
            });

            for(int ii=0; ii<tmpDrones.size();ii++){
                Drone xj = (Drone)tmpDrones.get(ii);
                if(xi.nLayeredLinks < partialLinks && xj.nLayeredLinks < partialLinks){
                    model.GA.setEntry(xi.getIndex(), xj.getIndex(), 1);
                    model.GA.setEntry(xj.getIndex(), xi.getIndex(), 1);
                    xi.nLayeredLinks++;
                    xj.nLayeredLinks++;
                    //System.out.println("Link Added ["+xi.getIndex()+","+xj.getIndex()+"]");
                }else{
                    break;
                }
            }
            algorithm.getFirstAgents().sort(new ByIndex());
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


    public void removeAgent(int index){
        int listIndex = algorithm.findAgentListIndex(index);
        algorithm.removeAgent(listIndex);
        model.replace(index, 0);
        if(index == 0)
            utmostLeader = null;

        if(Executor.getAlgorithm() != null){
            Executor.getInstance().getChartView().getView2D().redrawNetwork();
        }
        this.agentsCount--;
    }

    private void moveUp(Drone a){
        a.moveUpper();
        if(a.getConncetions().isEmpty())
            return;

        for(AbsAgent c: a.getConncetions()){
            moveUp((Drone)c);
        }
    }

    private void moveUp(Drone a, int layer){
        if(layer<0){
            return;
        }
        a.rank = layer;
        if(a.getConncetions().isEmpty())
            return;

        for(AbsAgent c: a.getConncetions()){
            moveUp((Drone)c, layer+1);
        }
    }

    public void formStateSpaceModel(){
        model.formStateSpaceModel(algorithm.getFirstAgents(), partialLinks);
    }

    public List<Drone> getLayer(int l){
        return getLayer(algorithm, l);
    }

    public static List<Drone> getLayer(Algorithm algorithm, int l){
        List<Drone> layer = new ArrayList<>();

        for(AbsAgent a: algorithm.getFirstAgents()){
            Drone d = (Drone) a;
            if(d.rank == l)
                layer.add(d);
        }
        return  layer;
    }

    private void electLeader(int index, int expectedLayer){
        for(AbsAgent a: algorithm.getFirstAgents()){
            ((Drone)a).removeConnection(index);
        }

        utmostLeader = (Drone) algorithm.getFirstAgents().stream().filter(d->d.getIndex() == index).findFirst().get();
        moveUp(utmostLeader, expectedLayer);

        for(AbsAgent a: algorithm.getFirstAgents()){
            Drone d = (Drone) a;
            if(d.rank == 1)
                utmostLeader.addConnection(d);
        }

        if(Executor.getAlgorithm() != null){
            Executor.getInstance().getChartView().getView2D().redrawNetwork();
        }
        model.GA = model.GA.scalarMultiply(0);
        model.GB = model.GB.scalarMultiply(0);
        formStateSpaceModel();
    }

    public void transferToUpperLayer(int index){

    }

    public void performLE(int index) {
        electLeader(index, 0);
    }

    public void performLE(int index, int expectedLayer) {
        electLeader(index, expectedLayer);
    }

    public void performLE(Drone d){
        System.out.println("new Leader="+d.getIndex());
        utmostLeader = d;
        model.GA = model.GA.scalarMultiply(0);
        model.GB = model.GB.scalarMultiply(0);
        formStateSpaceModel();
    }

    public double getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory() {
        this.freeMemory = Runtime.getRuntime().freeMemory();
    }
}
