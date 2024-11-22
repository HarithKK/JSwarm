package LF;

import examples.multiagent.leader_election.Main;
import examples.multiagent.leader_election.core.*;
import examples.multiagent.leader_election.core.data_structures.WalkType;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.action.AfterAll;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.StepCompleted;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Core {

    public static void executeForwardSI(int n, int nc, int np, double sr, String testName, int testId, int leaderRemoveAt, int exitAt, Critarian.SICritatianType c, WalkType w) throws Exception {
        DataStore dataStore = new DataStore();
        Main m = new Main(nc, np, n, 100, 100, 80, sr, 0.001, 0.0001, 5, w);

        m.algorithm.setMargins(new Margins(0, 200, 0, 600));
        m.algorithm.initialize();
        m.algorithm.setInitialized(true);

        for(AbsAgent a: m.algorithm.getFirstAgents()){
            dataStore.registerNode(a);
        }

        m.algorithm.setStepCompleted(new StepCompleted() {
            @Override
            public void performAction(long step) {

                Map<Drone, Double> bw = Matric.calculateBetweennessCentrality(m.algorithm.getFirstAgents());
                for(AbsAgent ag: m.algorithm.getFirstAgents()){
                    Drone a = (Drone)ag;
                    (a).updateEnergyProfile();
                    if(bw.containsKey(a)){
                        a.betweennessCentrality = bw.get(a);
                    }
                    dataStore.updateNodalData(a);
                }

                if(m.utmostLeader != null){
                    dataStore.updateCurrentLeader(m.utmostLeader.getIndex());
                    dataStore.updateComEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateComEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),sr));
                dataStore.updateControlEnergy(Matric.controlEnergy(m.algorithm.getFirstAgents()));
                if(step == leaderRemoveAt){
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    m.setFreeMemory();
                    AbsAgent agent = new Critarian().SI(m.model, m.getLayer(1), c);
                    dataStore.setMemory(m);
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(agent.getIndex(), 0);
                }

            }
        });

        m.algorithm.executionCompleted(new AfterAll() {
            @Override
            public void execute() {
                dataStore.uploadToMongo(testName, testId, m, c.name());
            }
        });
        m.algorithm.changeStepCount(exitAt);
        m.algorithm.setInterval(10);

        m.algorithm.run();
    }

    public static void executeForwardRaft(int n, int nc, int np, double sr, String testName, int testId, int leaderRemoveAt, int exitAt, WalkType w) throws Exception {
        DataStore dataStore = new DataStore();
        Main m = new Main(nc, np, n, 100, 100, 80, sr, 0.001, 0.0001, 5, w);

        m.algorithm.setMargins(new Margins(0, 200, 0, 600));
        m.algorithm.initialize();
        m.algorithm.setInitialized(true);

        for(AbsAgent a: m.algorithm.getFirstAgents()){
            dataStore.registerNode(a);
        }

        m.algorithm.setStepCompleted(new StepCompleted() {
            @Override
            public void performAction(long step) {

                Map<Drone, Double> bw = Matric.calculateBetweennessCentrality(m.algorithm.getFirstAgents());
                for(AbsAgent ag: m.algorithm.getFirstAgents()){
                    Drone a = (Drone)ag;
                    (a).updateEnergyProfile();
                    if(bw.containsKey(a)){
                        a.betweennessCentrality = bw.get(a);
                    }
                    dataStore.updateNodalData(a);
                }

                if(m.utmostLeader != null){
                    dataStore.updateCurrentLeader(m.utmostLeader.getIndex());
                    dataStore.updateComEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateComEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),sr));
                dataStore.updateControlEnergy(Matric.controlEnergy(m.algorithm.getFirstAgents()));
                if(step == leaderRemoveAt){
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    m.setFreeMemory();
                    AbsAgent agent = new Critarian().Raft(m.model, m.algorithm.getFirstAgents().stream().map(Drone::toDrone).collect(Collectors.toList()), m.partialLinks);
                    dataStore.setMemory(m);
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(agent.getIndex(), 0);
                }

            }
        });

        m.algorithm.executionCompleted(new AfterAll() {
            @Override
            public void execute() {
                dataStore.uploadToMongo(testName, testId, m, "Raft");
            }
        });
        m.algorithm.changeStepCount(exitAt);
        m.algorithm.setInterval(10);

        m.algorithm.run();
    }

    public static void executeForwardRandom(int n, int nc, int np, double sr, String testName, int testId, int leaderRemoveAt, int exitAt, WalkType w) throws Exception {
        DataStore dataStore = new DataStore();
        Main m = new Main(nc, np, n, 100, 100, 80, sr, 0.001, 0.0001, 5, w);

        m.algorithm.setMargins(new Margins(0, 200, 0, 600));
        m.algorithm.initialize();
        m.algorithm.setInitialized(true);

        for(AbsAgent a: m.algorithm.getFirstAgents()){
            dataStore.registerNode(a);
        }

        m.algorithm.setStepCompleted(new StepCompleted() {
            @Override
            public void performAction(long step) {

                Map<Drone, Double> bw = Matric.calculateBetweennessCentrality(m.algorithm.getFirstAgents());
                for(AbsAgent ag: m.algorithm.getFirstAgents()){
                    Drone a = (Drone)ag;
                    (a).updateEnergyProfile();
                    if(bw.containsKey(a)){
                        a.betweennessCentrality = bw.get(a);
                    }
                    dataStore.updateNodalData(a);
                }

                if(m.utmostLeader != null){
                    dataStore.updateCurrentLeader(m.utmostLeader.getIndex());
                    dataStore.updateComEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateComEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),sr));
                dataStore.updateControlEnergy(Matric.controlEnergy(m.algorithm.getFirstAgents()));
                if(step == leaderRemoveAt){
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    m.setFreeMemory();
                    int index = new Critarian().random(m.algorithm.getFirstAgents());
                    dataStore.setMemory(m);
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(index, 0);
                }

            }
        });

        m.algorithm.executionCompleted(new AfterAll() {
            @Override
            public void execute() {
                dataStore.uploadToMongo(testName, testId, m, "Random");
            }
        });
        m.algorithm.changeStepCount(exitAt);
        m.algorithm.setInterval(10);

        m.algorithm.run();
    }

    public static void executeForwardGHS(int n, int nc, int np, double sr, String testName, int testId, int leaderRemoveAt, int exitAt, WalkType w) throws Exception {
        DataStore dataStore = new DataStore();
        Main m = new Main(nc, np, n, 100, 100, 80, sr, 0.001, 0.0001, 5, w);

        m.algorithm.setMargins(new Margins(0, 200, 0, 600));
        m.algorithm.initialize();
        m.algorithm.setInitialized(true);

        for(AbsAgent a: m.algorithm.getFirstAgents()){
            dataStore.registerNode(a);
        }

        m.algorithm.setStepCompleted(new StepCompleted() {
            @Override
            public void performAction(long step) {

                Map<Drone, Double> bw = Matric.calculateBetweennessCentrality(m.algorithm.getFirstAgents());
                for(AbsAgent ag: m.algorithm.getFirstAgents()){
                    Drone a = (Drone)ag;
                    (a).updateEnergyProfile();
                    if(bw.containsKey(a)){
                        a.betweennessCentrality = bw.get(a);
                    }
                    dataStore.updateNodalData(a);
                }

                if(m.utmostLeader != null){
                    dataStore.updateCurrentLeader(m.utmostLeader.getIndex());
                    dataStore.updateComEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateComEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),sr));
                dataStore.updateControlEnergy(Matric.controlEnergy(m.algorithm.getFirstAgents()));
                if(step == leaderRemoveAt){
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    m.setFreeMemory();
                    AbsAgent agent = new Critarian().GHS(m.model, m.algorithm.getFirstAgents().stream().map(Drone::toDrone).collect(Collectors.toList()));
                    dataStore.setMemory(m);
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(agent.getIndex(), 0);
                }

            }
        });

        m.algorithm.executionCompleted(new AfterAll() {
            @Override
            public void execute() {
                dataStore.uploadToMongo(testName, testId, m, "GHS");
            }
        });
        m.algorithm.changeStepCount(exitAt);
        m.algorithm.setInterval(10);

        m.algorithm.run();
    }

    public static void executeForwardTSOAInd(Main m, String testName, int testId, int leaderRemoveAt, int exitAt) throws Exception {

        DataStore dataStore = new DataStore();

        m.algorithm.setMargins(new Margins(0, 200, 0, 600));
        m.algorithm.initialize();
        m.algorithm.setInitialized(true);

        for(AbsAgent a: m.algorithm.getFirstAgents()){
            dataStore.registerNode(a);
        }

        m.algorithm.setStepCompleted(new StepCompleted() {
            @Override
            public void performAction(long step) throws InterruptedException {

                Map<Drone, Double> bw = Matric.calculateBetweennessCentrality(m.algorithm.getFirstAgents());
                for(AbsAgent ag: m.algorithm.getFirstAgents()){
                    Drone a = (Drone)ag;
                    (a).updateEnergyProfile();
                    if(bw.containsKey(a)){
                        a.betweennessCentrality = bw.get(a);
                    }
                    dataStore.updateNodalData(a);
                }

                if(m.utmostLeader != null){
                    dataStore.updateCurrentLeader(m.utmostLeader.getIndex());
                    dataStore.updateComEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateComEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),m.safeRage));
                dataStore.updateControlEnergy(Matric.controlEnergy(m.algorithm.getFirstAgents()));
                if(step == leaderRemoveAt){
                    System.out.println(step);
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    m.setFreeMemory();
                    int index = new Critarian().TSOA(m.model, m.getLayer(1)).index;
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(index, 0);
                    System.gc();
                    dataStore.setMemory(m);
                }
            }
        });

        m.algorithm.executionCompleted(new AfterAll() {
            @Override
            public void execute() {
                dataStore.uploadToMongo(testName, testId, m, "TSOA_LE");

            }
        });
        m.algorithm.changeStepCount(exitAt);
        m.algorithm.setInterval(10);

        m.algorithm.run();
    }

    public static void executeForwardRaft(Main m, String testName, int testId, int leaderRemoveAt, int exitAt) throws Exception {
        DataStore dataStore = new DataStore();

        m.algorithm.setMargins(new Margins(0, 200, 0, 600));
        m.algorithm.initialize();
        m.algorithm.setInitialized(true);

        for(AbsAgent a: m.algorithm.getFirstAgents()){
            dataStore.registerNode(a);
        }

        m.algorithm.setStepCompleted(new StepCompleted() {
            @Override
            public void performAction(long step) {

                Map<Drone, Double> bw = Matric.calculateBetweennessCentrality(m.algorithm.getFirstAgents());
                for(AbsAgent ag: m.algorithm.getFirstAgents()){
                    Drone a = (Drone)ag;
                    (a).updateEnergyProfile();
                    if(bw.containsKey(a)){
                        a.betweennessCentrality = bw.get(a);
                    }
                    dataStore.updateNodalData(a);
                }

                if(m.utmostLeader != null){
                    dataStore.updateCurrentLeader(m.utmostLeader.getIndex());
                    dataStore.updateComEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateComEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),m.safeRage));
                dataStore.updateControlEnergy(Matric.controlEnergy(m.algorithm.getFirstAgents()));
                if(step == leaderRemoveAt){
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    m.setFreeMemory();
                    AbsAgent agent = new Critarian().Raft(m.model, m.algorithm.getFirstAgents().stream().map(Drone::toDrone).collect(Collectors.toList()), m.partialLinks);
                    dataStore.setMemory(m);
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(agent.getIndex(), 0);
                }

            }
        });

        m.algorithm.executionCompleted(new AfterAll() {
            @Override
            public void execute() {
                dataStore.uploadToMongo(testName, testId, m, "Raft");
            }
        });
        m.algorithm.changeStepCount(exitAt);
        m.algorithm.setInterval(10);

        m.algorithm.run();
    }

    public static void executeForwardRandom(Main m, String testName, int testId, int leaderRemoveAt, int exitAt) throws Exception {
        DataStore dataStore = new DataStore();

        m.algorithm.setMargins(new Margins(0, 200, 0, 600));
        m.algorithm.initialize();
        m.algorithm.setInitialized(true);

        for(AbsAgent a: m.algorithm.getFirstAgents()){
            dataStore.registerNode(a);
        }

        m.algorithm.setStepCompleted(new StepCompleted() {
            @Override
            public void performAction(long step) {

                Map<Drone, Double> bw = Matric.calculateBetweennessCentrality(m.algorithm.getFirstAgents());
                for(AbsAgent ag: m.algorithm.getFirstAgents()){
                    Drone a = (Drone)ag;
                    (a).updateEnergyProfile();
                    if(bw.containsKey(a)){
                        a.betweennessCentrality = bw.get(a);
                    }
                    dataStore.updateNodalData(a);
                }

                if(m.utmostLeader != null){
                    dataStore.updateCurrentLeader(m.utmostLeader.getIndex());
                    dataStore.updateComEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateComEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),m.safeRage));
                dataStore.updateControlEnergy(Matric.controlEnergy(m.algorithm.getFirstAgents()));
                if(step == leaderRemoveAt){
                    int iy = m.utmostLeader.getIndex();
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    m.setFreeMemory();
                    int index = new Critarian().random(m.algorithm.getFirstAgents());
                    dataStore.setMemory(m);
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(index, 0);
                }

            }
        });

        m.algorithm.executionCompleted(new AfterAll() {
            @Override
            public void execute() {
                dataStore.uploadToMongo(testName, testId, m, "Random");
            }
        });
        m.algorithm.changeStepCount(exitAt);
        m.algorithm.setInterval(10);

        m.algorithm.run();
    }

    public static void executeForwardGHS(Main m, String testName, int testId, int leaderRemoveAt, int exitAt) throws Exception {
        DataStore dataStore = new DataStore();

        m.algorithm.setMargins(new Margins(0, 200, 0, 600));
        m.algorithm.initialize();
        m.algorithm.setInitialized(true);

        for(AbsAgent a: m.algorithm.getFirstAgents()){
            dataStore.registerNode(a);
        }

        m.algorithm.setStepCompleted(new StepCompleted() {
            @Override
            public void performAction(long step) {

                Map<Drone, Double> bw = Matric.calculateBetweennessCentrality(m.algorithm.getFirstAgents());
                for(AbsAgent ag: m.algorithm.getFirstAgents()){
                    Drone a = (Drone)ag;
                    (a).updateEnergyProfile();
                    if(bw.containsKey(a)){
                        a.betweennessCentrality = bw.get(a);
                    }
                    dataStore.updateNodalData(a);
                }

                if(m.utmostLeader != null){
                    dataStore.updateCurrentLeader(m.utmostLeader.getIndex());
                    dataStore.updateComEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateComEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),m.safeRage));
                dataStore.updateControlEnergy(Matric.controlEnergy(m.algorithm.getFirstAgents()));
                if(step == leaderRemoveAt){
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    m.setFreeMemory();
                    AbsAgent agent = new Critarian().GHS(m.model, m.algorithm.getFirstAgents().stream().map(Drone::toDrone).collect(Collectors.toList()));
                    dataStore.setMemory(m);
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(agent.getIndex(), 0);
                }

            }
        });

        m.algorithm.executionCompleted(new AfterAll() {
            @Override
            public void execute() {
                dataStore.uploadToMongo(testName, testId, m, "GHS");
            }
        });
        m.algorithm.changeStepCount(exitAt);
        m.algorithm.setInterval(10);

        m.algorithm.run();
    }

}
