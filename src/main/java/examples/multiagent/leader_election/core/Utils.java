package examples.multiagent.leader_election.core;

import org.apache.commons.math3.util.Pair;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.util.Randoms;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    public static void addRandomProcessingWeight(int from, int to){
        try {
            Thread.sleep(Randoms.rand(from, to));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void addTransactionProcessingWeight(){
        Utils.addRandomProcessingWeight(10,30);
    }

    public static Drone findParent(List<AbsAgent> agents, AbsAgent agent){
        for(AbsAgent a: agents){
            if(a.getConncetions().contains(agent)){
                return (Drone)a;
            }
        }
        return null;
    }

    public static Pair<Drone, Drone> swapDrones(List<AbsAgent> agents, Drone t1, Drone t2){

        Drone upperAgent=null, lowerAgent=null;

        if(t1.rank < t2.rank){
            upperAgent = t1;
            lowerAgent = t2;
        }else{
            upperAgent = t2;
            lowerAgent = t1;
        }

        // isolate nodes
        List<AbsAgent> upperAgentChildren = upperAgent.getConncetions().stream().collect(Collectors.toList());
        List<AbsAgent> lowerAgentChildren = lowerAgent.getConncetions().stream().collect(Collectors.toList());

        int rankFirst = upperAgent.rank;
        int rankSecond = lowerAgent.rank;

        // swap
        upperAgent.rank = rankSecond;
        lowerAgent.rank = rankFirst;

        if(upperAgentChildren.contains(lowerAgent)){

            Drone upperParent = findParent(agents, upperAgent);
            if(upperParent != null)
                upperParent.removeConnection(upperAgent);

            upperAgentChildren.remove(lowerAgent);
            upperAgentChildren.add(upperAgent);

            upperAgent.setConncetions(lowerAgentChildren.stream().collect(Collectors.toSet()));
            lowerAgent.setConncetions(upperAgentChildren.stream().collect(Collectors.toSet()));

            if(upperParent != null){
                upperParent.addConnection(lowerAgent);
            }
        }else{
            Drone upperParent = findParent(agents, upperAgent);
            if(upperParent != null)
                upperAgent.removeConnection(upperParent);
            Drone lowerParent = findParent(agents, lowerAgent);
            if(lowerParent != null)
                lowerParent.removeConnection(lowerAgent);

            upperAgent.setConncetions(lowerAgentChildren.stream().collect(Collectors.toSet()));
            lowerAgent.setConncetions(upperAgentChildren.stream().collect(Collectors.toSet()));

            if(upperParent != null){
                upperParent.addConnection(lowerAgent);
            }
            if(lowerParent != null){
                lowerParent.addConnection(upperAgent);
            }
        }

        return new Pair<>(lowerAgent, upperAgent);
    }

    public static Map<Integer, List<Drone>> getLayers(List<Drone> firstAgents){
        Map<Integer, List<Drone>> layers = new HashMap<>();

        for(Drone agent: firstAgents){
            List<Drone> dr = layers.get(agent.rank);
            if(dr == null){
                dr = new ArrayList<>();
                layers.put(agent.rank, dr);
            }
            dr.add(agent);
        }
        return layers;
    }

    public static void drawModalTree(Drone agent) {
        int i =0;
        while(i<agent.rank){
            if(i==0){
                System.out.print("+");
            }
            if(i==agent.rank-1){
                System.out.print("\\---");
            }else{
                System.out.print("     ");
            }
            i++;
        }
        System.out.println(agent);
        for(AbsAgent a: agent.getConncetions()){
            drawModalTree((Drone)a);
        }
    }
}
