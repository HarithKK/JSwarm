package examples.multiagent.leader_election;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.comparators.ByIndex;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.util.StringFormatter;

import java.util.*;

public class Main {

    private static final double K = 0.0001;
    Algorithm algorithm;

    Drone utmostLeader;

    private int MAX_LINKS = 3;
    private int MIN_DIST=30;

    private RealMatrix A;
    private RealMatrix B;

    public Main(int count){

        A = MatrixUtils.createRealMatrix(count, count);
        B = MatrixUtils.createRealMatrix(count, count);

        algorithm = new Algorithm() {
            @Override
            public void initialize() {

                try {
                    this.addAgents("drones", Drone.class, count);
                    utmostLeader = (Drone) getFirstAgents().get(0);// findRoot();
                    utmostLeader.rank = 0;
                    utmostLeader.setX(100);
                    utmostLeader.setY(10);
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
//                    for(AbsAgent ai: getFirstAgents()){
//                        for(AbsAgent aj: ai.getConncetions()){
//                            Drone i = (Drone) ai;
//                            Drone j = (Drone) aj;
//                            if(i.getIndex() != j.getIndex()){
//                                A.setEntry(i.getIndex(), j.getIndex(), 1);
//                                if(i.rank < j.rank){
//                                    B.setEntry(i.getIndex(), j.getIndex(), -1);
//                                    B.setEntry(j.getIndex(), i.getIndex(), 1);
//                                }
//                            }
//                        }
//                    }

                    for(int i=0; i< count; i++){
                        for(int j=0; j< count; j++){
                            Drone ai = (Drone)getFirstAgents().get(i);
                            Drone aj = (Drone)getFirstAgents().get(j);

                            if(ai.rank == aj.rank){
                                A.setEntry(ai.getIndex(), aj.getIndex(), 1);
                                A.setEntry(aj.getIndex(), ai.getIndex(), 1);
                                continue;
                            }
                            if(ai.getConncetions().contains(aj)){
                                A.setEntry(ai.getIndex(), aj.getIndex(), 1);
                                if(ai.rank < aj.rank){
                                    B.setEntry(ai.getIndex(), aj.getIndex(), -1);
                                    B.setEntry(aj.getIndex(), ai.getIndex(), 1);
                                }
                            }
                        }
                    }

                    //System.out.println(StringFormatter.toString(B));
                    System.out.println("ROOT: "+utmostLeader.getIndex());

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

                for(int i=0; i<count; i++){
                    Drone dr = (Drone) getFirstAgents().get(i);
                    if(dr.getIndex() == utmostLeader.getIndex()){
                        continue;
                    }

                    Drone leader = (Drone) getFirstAgents().get(getLeaderIndex(dr.getIndex()));
                    dr.velocity = calculateVelocity(leader, dr);

                    for(int j=0; j<A.getRowDimension(); j++){
                        if(j == leader.getIndex() || j == dr.getIndex()){
                            continue;
                        }
                        //if(A.getEntry(dr.getIndex(), j) > 0 && B.getEntry(dr.getIndex(), j) == 0)
                        if(dr.rank == ((Drone)getFirstAgents().get(j)).rank)
                            dr.velocity.updateVector(calculateVelocity(dr, getFirstAgents().get(j)).toNeg());
                    }

//                    for(AbsAgent sub: dr.getConncetions()){
//                        if(!(sub.getIndex() < dr.getIndex())){
//                            dr.velocity.operate(Vector.OPERATOR.ADD, calculateVelocity(dr, sub));
//                        }
//                    }
                }

            }

            private Vector calculateVelocity(AbsAgent d1, AbsAgent d2) {
                Vector vc = d1.getPosition().getClonedVector().operate(Vector.OPERATOR.SUB, d2.getPosition());
                return vc.operate(Vector.OPERATOR.MULP, (vc.getMagnitude()-MIN_DIST)*K);
            }

            private int getLeaderIndex(int index) {
                for(int i=0; i<B.getRowDimension(); i++){
                    if(B.getEntry(index, i) == 1.0){
                        return i;
                    }
                }
                return -1;
            }


        };

    }

    public static void main(String[] args) {
        Executor.getInstance().executePlain2D("LF", new Main(15).algorithm, 700, 700, new Margins(0, 200, 0, 1000));
    }
}
