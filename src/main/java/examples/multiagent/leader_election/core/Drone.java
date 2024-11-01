package examples.multiagent.leader_election.core;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.util.HomogeneousTransformer;
import org.usa.soc.util.Randoms;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Drone extends Agent {

    /**
     * RAFT Vars
     */

    int currentTerm = 0, voteCount = 1;
    RaftState currentState = RaftState.FOLLOWER;
    long startTime = System.currentTimeMillis();
    long electionTimeDuration = getElectionTimeOut();
    List<LogEntry> log = new ArrayList<LogEntry>();

    Drone votedFor = null;

    private long getElectionTimeOut(){
        return (long) Randoms.rand(500,1000);
    }

    public int rank = -1;

    public Vector velocity = new Vector(2);

    public double controlEnergy = 0;
    public double commEnergy = 0;

    public double nLayeredLinks;

    public void moveUpper() {
        this.rank -=1;
    }

    @Override
    public void step() {
        this.getPosition().updateVector(velocity);
    }

    public RealMatrix getState() {
        return MatrixUtils.createColumnRealMatrix(new double[]{
                position.getValue(0),
                position.getValue(1),
                velocity.getValue(0),
                velocity.getValue(1)}
        );
    }

    public void setState(RealMatrix matrix){
        this.position.setValue(matrix.getEntry(0,0), 0);
        this.position.setValue(matrix.getEntry(1,0), 1);
        this.velocity.setValue(matrix.getEntry(2,0), 0);
        this.velocity.setValue(matrix.getEntry(3,0), 1);
    }

    public void updateEnergyProfile(){
        controlEnergy = this.velocity.getMagnitude();
        OptionalDouble od = getConncetions().stream().mapToDouble(c -> c.getPosition().getDistance(this.getPosition().getClonedVector())).max();
        if(od.isPresent()){
            commEnergy = od.getAsDouble();
        }
    }

    public void updateU(WalkType selection, double theta, double av) {
        switch (selection){
            /*Circular Motion With Velocity*/
            case RANDOM_CIRCLE: velocity.setValues(new double[]{av*Math.sin(theta),av*Math.cos(theta)}); break;
            case RANDOM_THETA: {
                double angle = Randoms.rand(0, 180);
                RealMatrix vc = MatrixUtils.createRealMatrix(3, 1);
                vc.setEntry(0,0, velocity.getValue(0));
                vc.setEntry(1,0, velocity.getValue(1));
                vc.setEntry(2,0, 1);
                this.velocity.setVector(
                        HomogeneousTransformer.getRotationMatrix(Math.toRadians(angle),2)
                                .multiply(vc)
                                .getColumnVector(0)
                );
            }; break;
        }
    }

    private int getLogTerm() {
        if(log.isEmpty())
            return 0;
        return log.get(log.size()-1).term;
    }

    private int getLogIndex() {
        return log.size()-1;
    }

    public void initCandidate() {
        currentState = RaftState.CANDIDATE;
        currentTerm += 1;
        electionTimeDuration = getElectionTimeOut();
        startTime = System.currentTimeMillis();
    }

    public void initFollower(int term) {
        currentState = RaftState.FOLLOWER;
        currentTerm = term;
    }

    private void initLeader() {
        currentState = RaftState.LEADER;
    }

    void updateCandidate(RealMatrix m, List<Drone> agents, int npLinks) {
        votedFor = this;
        for(Drone d: agents){

            if(m.getEntry(this.getIndex(), d.getIndex()) == 0){
                continue;
            }
            int lastLogIndex = getLogIndex();
            int lastLogTerm = getLogTerm();
            Pair <Integer, Boolean> response = d.requestVoteRPC(currentTerm, this, lastLogIndex, lastLogTerm);

            if(response.getFirst() > currentTerm){
                initFollower(response.getFirst());
                break;
            }

            if(response.getSecond() == true){
                voteCount+=1;
            }
        }

        if(voteCount > Math.ceil((npLinks+1)/2.0)){
            initLeader();
        }else{
            initFollower(currentTerm);
            electionTimeDuration = getElectionTimeOut();
            startTime = System.currentTimeMillis();
        }
    }

    public Pair<Integer, Boolean> requestVoteRPC(int term, Drone candidate, int lastLogIndex, int lastLogTerm) {
        if(term < currentTerm){
            return new Pair<>(currentTerm, false);
        }else if(term > currentTerm){
            initFollower(term);
        }

        if(votedFor == null || votedFor.equals(candidate)){
            if(getLogTerm() > lastLogTerm || getLogTerm() == lastLogTerm && getLogIndex() > lastLogIndex){
                return new Pair<>(currentTerm, false);
            }else{
                currentState = RaftState.CANDIDATE;
                return new Pair<>(currentTerm, true);
            }
        }
        return new Pair<>(currentTerm, false);
    }

    public double calculateJ(){
        return commEnergy + controlEnergy;
    }
}
