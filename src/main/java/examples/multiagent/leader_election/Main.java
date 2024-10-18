package examples.multiagent.leader_election;

import examples.multiagent.initial.InitialTestAgent;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.runners.Executor;

import java.util.*;

public class Main {

    Algorithm algorithm;

    Drone utmostLeader;
    public Main(int count){

        algorithm = new Algorithm() {
            @Override
            public void initialize() {

                try {
                    this.addAgents("drones", Drone.class, count);
                    utmostLeader = findRoot();
                    utmostLeader.rank = 0;

                    Queue<AbsAgent> bfsQueue = new ArrayDeque<>();
                    bfsQueue.add(utmostLeader);

                    do{
                        List<AbsAgent> l = createNetwork(bfsQueue);
                        for(AbsAgent a: l){
                            bfsQueue.add(a);
                        }
                    }while(!bfsQueue.isEmpty());

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

                    int max_count = 6;

                    for(AbsAgent a: agents){
                        if(max_count < 0){
                            break;
                        }
                        if(root.conncetions.contains(a)){
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

            }
        };

    }

    public static void main(String[] args) {
        Executor.getInstance().executePlain2D("LF", new Main(50).algorithm, 700, 700, new Margins(0, 100, 0, 100));
    }
}
