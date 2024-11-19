package examples.multiagent.leader_election.core;

import org.apache.commons.math3.analysis.function.Abs;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.scoring.BetweennessCentrality;
import org.jgrapht.alg.scoring.ClosenessCentrality;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.usa.soc.core.AbsAgent;

import java.util.List;
import java.util.Map;

public class Matric {

    public static double controlEnergy(List<AbsAgent> agent){
        return agent.stream().map(Drone::toDrone).mapToDouble(d -> Math.pow(d.velocity.getMagnitude(),2)).sum();
    }

    public static double MaxControlEnergy(Drone d, double energy){

        if(d == null)
            return -1;

        if(d.getConncetions().isEmpty()){
            return energy;
        }

        double maxEnergy = 0;
        for(AbsAgent a: d.getConncetions()){
            Drone di = (Drone)a;
            maxEnergy = Math.max(maxEnergy, MaxControlEnergy(di, energy + di.getPosition().getDistance(d.getPosition())));
        }
        return maxEnergy;
    }

    public static double calculateTrackingError(List<AbsAgent> agent, double d){
        double error = 0;
        try{
            for(AbsAgent a: agent){
                Drone s = (Drone) a;
                for(AbsAgent t: s.getConncetions()){
                    error += Math.pow(s.getPosition().getDistance(t.getPosition()) - d, 2);
                }
            }
        }catch (Exception e){

        }
        return error;
    }

    public static Map calculateBetweennessCentrality(List<AbsAgent> agent){

        Graph<AbsAgent, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

        for(AbsAgent a: agent){
            g.addVertex(a);
        }

        for(AbsAgent a: agent){
            for(AbsAgent a1: a.getConncetions()){
                if(g.containsVertex(a) && g.containsVertex(a1) && g.getEdge(a, a1) == null && a != a1){
                    g.addEdge(a,a1);
                }
            }
        }

        BetweennessCentrality bw = new BetweennessCentrality<>(g);
        return bw.getScores();
    }

    public static Map calculateClosenessCentrality(List<AbsAgent> agent){

        Graph<AbsAgent, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

        for(AbsAgent a: agent){
            g.addVertex(a);
        }

        for(AbsAgent a: agent){
            for(AbsAgent a1: a.getConncetions()){
                if(g.containsVertex(a) && g.containsVertex(a1) && g.getEdge(a, a1) == null && a != a1){
                    g.addEdge(a,a1);
                }
            }
        }

        ClosenessCentrality cc = new ClosenessCentrality<>(g);
        return cc.getScores();
    }

    public static double eigenReLambda(RealMatrix m){
        return new EigenDecomposition(m).getRealEigenvalue(0);
    }

}
