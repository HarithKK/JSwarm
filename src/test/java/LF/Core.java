package LF;

import examples.multiagent.leader_election.Main;
import examples.multiagent.leader_election.core.*;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.action.AfterAll;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.StepCompleted;

import java.time.Duration;
import java.time.Instant;
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
                    dataStore.updateControlEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateControlEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),sr));

                if(step == leaderRemoveAt){
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    AbsAgent agent = new Critarian().SI(m.model, m.getLayer(1), c);
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(agent.getIndex());
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
                    dataStore.updateControlEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateControlEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),sr));

                if(step == leaderRemoveAt){
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    AbsAgent agent = new Critarian().Raft(m.model, m.algorithm.getFirstAgents().stream().map(Drone::toDrone).collect(Collectors.toList()), m.partialLinks);
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(agent.getIndex());
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
                    dataStore.updateControlEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateControlEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),sr));

                if(step == leaderRemoveAt){
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    int index = new Critarian().random(0, m.algorithm.getFirstAgents().size());
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(index);
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
                    dataStore.updateControlEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateControlEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),sr));

                if(step == leaderRemoveAt){
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    AbsAgent agent = new Critarian().GHS(m.model, m.algorithm.getFirstAgents().stream().map(Drone::toDrone).collect(Collectors.toList()));
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(agent.getIndex());
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

    public static void executeForwardTSOA(int n, int nc, int np, double sr, String testName, int testId, int leaderRemoveAt, int exitAt, WalkType w) throws Exception {
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
                    dataStore.updateControlEnergy(Matric.MaxControlEnergy(m.utmostLeader, 0));
                }else{

                    double e = 0;
                    List<Drone> ds = m.getLayer(1);

                    for(Drone a: ds){
                        e = Math.max(e, Matric.MaxControlEnergy(a, 0));
                    }
                    dataStore.updateCurrentLeader(-1);
                    dataStore.updateControlEnergy(e);
                }

                dataStore.updateA(m.model.GA);
                dataStore.updateMinEigenValue(Matric.eigenReLambda(m.model.GA));
                dataStore.updateTrackingError(Matric.calculateTrackingError(m.algorithm.getFirstAgents(),sr));

                if(step == leaderRemoveAt){
                    m.removeAgent(m.utmostLeader.getIndex());
                    long l = System.currentTimeMillis();
                    int index = new Critarian().TSOA(m.model, m.getLayer(1));
                    dataStore.setTime(System.currentTimeMillis() - l);
                    m.performLE(index);
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

}
