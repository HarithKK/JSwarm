package examples.multiagent.leader_election.core;

import examples.multiagent.leader_election.Main;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;
import org.bson.Document;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.data.MongoClient;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class DataStore {

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    class NodeData{
        public double nodalEnergy;
        public double betweennessCentrality;
        public double x_traj, y_traj;
        public RealVector velocity;
        public int rank;

        public int index;

    }

    List<Double> controlEnergy = new ArrayList<>();
    List<Double> trackingError = new ArrayList<>();

    List<Integer> currentLeader = new ArrayList<>();
    List<RealMatrix> Ahistory = new ArrayList<>();

    List<Double> minEigenValue = new ArrayList<>();
    Map<Integer, List<NodeData>> nodeData = new HashMap<>();

    private long time;
    public void registerNode(AbsAgent agent){
        nodeData.put(agent.getIndex(), new ArrayList<>());
    }

    public void updateControlEnergy(double data){
        this.controlEnergy.add(data);
    }

    public void updateTrackingError(double data){
        this.trackingError.add(data);
    }

    public void updateA(RealMatrix A){
        this.Ahistory.add(A);
    }

    public void updateCurrentLeader(int data){
        this.currentLeader.add(data);
    }

    public void updateMinEigenValue(double data){
        this.minEigenValue.add(data);
    }

    public void updateNodalData(Drone d){
        NodeData n = new NodeData();
        n.nodalEnergy = d.nodalEnergy;
        n.betweennessCentrality = d.betweennessCentrality;
        n.x_traj = d.getX();
        n.y_traj = d.getY();
        n.velocity = d.velocity.getClonedVector().toRealVector();
        n.rank = d.rank;
        n.index = d.getIndex();

        this.nodeData.get(d.getIndex()).add(n);
    }

    private Document toDocument(List<RealMatrix> m){
        Document k = new Document();

        int r =0;
        for(RealMatrix i: m){
            List bk = new ArrayList<>();
            for(int j=0; j<i.getRowDimension(); j++){
                bk.add(Arrays.asList(i.getRow(j)));
            }
            k.put(String.valueOf(r++),bk);
        }
        return k;
    }

    public void uploadToMongo(String name, int testId, Main m, String leaderElectionAlgorithm){
        MongoClient mongoClient = new MongoClient("leader-election");

        Document doc = new Document();
        doc.append("desc", "Test-112");
        doc.append("test", name);
        doc.append("test-id", testId);
        doc.append("leader-election-algorithm", leaderElectionAlgorithm);
        doc.append("nc",m.controlLinks);
        doc.append("np",m.partialLinks);
        doc.append("agentsCount",m.agentsCount);
        doc.append("angularVelocity",m.angularVelocity);
        doc.append("r",m.r);
        doc.append("safeRage",m.safeRage);
        doc.append("kReject",m.kReject);
        doc.append("kAttract",m.kAttract);
        doc.append("exe_time",getTime());
        doc.append("walk_type",m.type.name());
        doc.append("pareto_front",m.pf.stream().map(d -> new Document().append("x", d.getX()).append("y", d.getY())).collect(Collectors.toList()));

//        doc.append("K0",m.model.K0);
//        doc.append("K1",m.model.K1);
//        doc.append("K2",m.model.K2);
//        doc.append("KR",m.model.KR);
//        doc.append("KC",m.model.KC);
//        doc.append("KD",m.model.KD);
//        doc.append("A",toDocument(Ahistory));
        doc.append("controlEnergy", controlEnergy);
        doc.append("trackingError", trackingError);
        doc.append("currentLeader", currentLeader);
        doc.append("minEigenValue", minEigenValue);

        Document dm = new Document();
        for(int i: nodeData.keySet()){
            List<NodeData> ns = nodeData.get(i);
            List<Document> nsn = new ArrayList<>();
            for(NodeData n: ns){
                Document ds = new Document()
                        .append("betweennessCentrality", n.betweennessCentrality)
                        .append("nodalEnergy",n.nodalEnergy)
                        .append("rank",n.rank)
                        .append("index",n.index)
                        .append("x_traj",n.x_traj)
                        .append("y_traj",n.y_traj)
                        .append("velocity.x",n.velocity.getEntry(0))
                        .append("velocity.y",n.velocity.getEntry(1));

                nsn.add(ds);
            }
            dm.put(String.valueOf(i), nsn);
        }

        doc.put("nodes", dm);
        mongoClient.updateDocument(doc, "results-2");
    }

}
