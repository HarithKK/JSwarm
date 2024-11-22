package examples.multiagent.leader_election.core;

import examples.multiagent.leader_election.core.data_structures.*;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;
import org.usa.soc.comparators.ParetoComparator;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.util.HomogeneousTransformer;
import org.usa.soc.util.ParetoUtils;
import org.usa.soc.util.Randoms;

import java.util.*;

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

    public int rank = -1;

    public Vector velocity = new Vector(2);

    public double controlEnergy = 0;
    public double commEnergy = 0, nodalEnergy =0, betweennessCentrality=0;

    public double nLayeredLinks;

    public void moveUpper() {
        this.rank -=1;
    }

    private long getElectionTimeOut(){
        return (long) Randoms.rand(1000,3000);
    }

    @Override
    public void step() {
        this.getPosition().updateVector(velocity);
        updateEnergyProfile();

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
        RealMatrix vm = velocity.toRealMatrix();
        nodalEnergy = vm.transpose().multiply(vm).getNorm();
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
        return log.get(log.size()-1).getTerm();
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

    @Override
    public String toString(){
        return String.valueOf(getIndex());
    }

    /**
     * For RAFT
     */
    private void initLeader() {
        currentState = RaftState.LEADER;
    }

    void updateCandidate(RealMatrix m, List<Drone> agents, int npLinks) throws InterruptedException {
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

    public Pair<Integer, Boolean> requestVoteRPC(int term, Drone candidate, int lastLogIndex, int lastLogTerm) throws InterruptedException {
        if(term < currentTerm){
            return new Pair<>(currentTerm, false);
        }else if(term > currentTerm){
            initFollower(term);
        }
        Thread.sleep(Randoms.rand(500, 2000));
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

    /**
     * For GHS
     */
    public double calculateJ(){
        return commEnergy + controlEnergy;
    }

    public static Drone toDrone(AbsAgent a){
        return (Drone)a;
    }

    public void removeConnection(int index) {
        Optional<AbsAgent> a = getConncetions().stream().filter(d->d.getIndex() == index).findFirst();
        if(!a.isEmpty()){
            this.getConncetions().remove(a.get());
        }
    }

    /**
     * TSOA
     * @param count
     * @param model
     * @return
     */

    public Tree executeTSOA(int count, StateSpaceModel model){

        try {
            Thread.sleep(Randoms.rand(30,100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RealMatrix gc = model.calcGcStep(model.getNN(), 1);
        ParetoComparator<Tree> comparators = new ParetoComparator<>();
        comparators.add(new Comparator<Tree>() {
            @Override
            public int compare(Tree o1, Tree o2) {
                return Double.compare(o1.f1, o2.f1);
            }
        });
        comparators.add(new Comparator<Tree>() {
            @Override
            public int compare(Tree o1, Tree o2) {
                return Double.compare(o1.f2, o2.f2);
            }
        });

        Vector gbest = new Vector(2);
        Vector z = new Vector(2);

        List<Tree> trees = new ArrayList<>();
        Tree tt1 = new Tree(this.getPosition().getClonedVector());
        tt1.setFitnessValues(ObjectiveFunctions.f(tt1,gc, getIndex(), getPosition()));
        trees.add(tt1);

        final int deligator = 7;
        final int seedsCount = 10;

        for(int step=0; step<count; step++){

            double totalLabmda = 0;
            double totalFitnessValue = 0.0;
            double totalDistanceWithP = 0.0;

            for(int i =0; i <  trees.size(); i++){
                Tree t = trees.get(i);
                totalLabmda += t.lambda;
                totalDistanceWithP += Math.pow(t.getCalculatedDistance(z), -1);
                totalFitnessValue += t.f1;
            }

            int zSize = (int) Math.round(trees.size() - (step + 1) * ((double)(trees.size() - 1) / (double)step));

            for(int i=0; i<zSize ; i++){
                Tree t = trees.get(i);
                z = z.operate(Vector.OPERATOR.ADD, t.position.operate(Vector.OPERATOR.MULP, t.lambda));
                t.updateWeight(totalFitnessValue);
                t.updateLambda(-1, totalLabmda, totalDistanceWithP);
            }

            double minimumDistance = Double.MAX_VALUE;
            for(int i =0; i < trees.size(); i++){
                Tree t = trees.get(i);
                minimumDistance = Math.min(t.getCalculatedDistance(z), minimumDistance);
            }

            Tree t = trees.get(0);

            for(int j =0; j< seedsCount; j++){
                Tree newTree = new Tree(new Vector(2));
                double r = Randoms.rand();

                if(r < 0.3){
                    newTree.position = Randoms.getRandomVector(2, -2, 2);
                    newTree.setFitnessValues(ObjectiveFunctions.f(newTree,gc, model.GA, getIndex(), getPosition()));
                    newTree.lambda = 1.0;
                }else if(r < 0.6){
                    newTree.position = t.position.operate(Vector.OPERATOR.ADD,
                            Randoms.getRandomGaussianVector(2, -2, 2, 0,1)
                    );
                    newTree.setFitnessValues(ObjectiveFunctions.f(newTree,gc, model.GA , getIndex(), getPosition()));
                    newTree.lambda = 1.0;
                }else{
                    Vector v1 = z.getClonedVector().operate(Vector.OPERATOR.SUB, t.position)
                            .operate(Vector.OPERATOR.MULP, 2)
                            .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                    Vector v2 = gbest.getClonedVector()
                            .operate(Vector.OPERATOR.SUB,  t.position)
                            .operate(Vector.OPERATOR.MULP, 2)
                            .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                    Vector vx = v1.operate(Vector.OPERATOR.ADD, v2);
                    newTree.position = t.position.operate(Vector.OPERATOR.ADD, vx);
                    newTree.setFitnessValues(ObjectiveFunctions.f(newTree,gc, model.GA, getIndex(), getPosition()));
                    newTree.lambda = 1.0;
                }
                trees.add(newTree);
            }

            Collection<Tree> po = ParetoUtils.getMinimalFrontierOf(trees, comparators);
            trees = new ArrayList<>();
            Iterator<Tree> itr = po.iterator();
            while(itr.hasNext()) {
                trees.add(itr.next());
            }

            trees.sort(new Comparator<Tree>() {
                @Override
                public int compare(Tree o1, Tree o2) {
                    if(o1.f1 < o2.f1){
                        return -1;
                    }else{
                        return 1;
                    }
                }
            });
            //Tree ty = trees.get(0);
            int u = trees.size()/2 <= 0 ? 1 : trees.size()/2;
            trees = trees.subList(0, u);
            //trees.add(ty);
        }

        Tree t = trees.get(0);
        t.index = getIndex();
        trees = null;
        gbest = null;
        System.gc();
        return t;
    }
}
