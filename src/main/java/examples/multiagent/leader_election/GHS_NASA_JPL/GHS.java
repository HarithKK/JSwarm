package examples.multiagent.leader_election.GHS_NASA_JPL;

import examples.multiagent.leader_election.core.Drone;
import org.apache.commons.math3.linear.RealMatrix;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.StringFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GHS {

    Graph<Node, DefaultEdge> graph = new SimpleWeightedGraph<>(DefaultEdge.class);

    public GHS(List<Drone> agents, RealMatrix A){

        int w0 = 0;
        for(Drone d: agents){
            w0 += d.getIndex();
            graph.addVertex(new Node(d.getIndex(), d.calculateJ()));
        }

        for(Node ds: graph.vertexSet()){
            for(Node dd: graph.vertexSet()){
                if(A.getEntry(ds.index, dd.index) == 1){
                    if(graph.getEdge(ds, dd) == null){
                        int w = w0 << 16 + Math.max(ds.index, dd.index) << 8 + Math.min(ds.index, dd.index);
                        Graphs.addEdge(graph, ds, dd, w);
                    }
                }
            }
        }

    }

    private DefaultEdge findMinimumEdge(Node n) {
        DefaultEdge minEdge = null;
        for (DefaultEdge edge : graph.edgesOf(n)) {
            if (!graph.getEdgeSource(edge).inMST || !graph.getEdgeTarget(edge).inMST) {
                if (minEdge == null || graph.getEdgeWeight(edge) < graph.getEdgeWeight(minEdge)) {
                    try {
                        Thread.sleep(Randoms.rand(10, 50));
                    } catch (InterruptedException e) {

                    }
                    minEdge = edge;
                }
            }
        }
        return minEdge;
    }

    public DirectedAcyclicGraph<Node, DefaultEdge> getGraph(List<DefaultEdge> edges){
        DirectedAcyclicGraph<Node, DefaultEdge> newGraph = new DirectedAcyclicGraph<>(DefaultEdge.class);

        for(DefaultEdge e: edges){
            Node source = graph.getEdgeSource(e);
            Node target = graph.getEdgeTarget(e);

            if(!newGraph.vertexSet().contains(source)){
                newGraph.addVertex(source);
            }
            if(!newGraph.vertexSet().contains(target)){
                newGraph.addVertex(target);
            }
            if(newGraph.getEdge(source, target) == null){
                newGraph.addEdge(source, target, e);
            }
        }
        return newGraph;
    }

    public List<DefaultEdge> findMST() {

        List<DefaultEdge> mstEdges = new ArrayList<>();

        boolean hasChanges;
        do {
            hasChanges = false;

            for (Node node : graph.vertexSet()) {
                DefaultEdge minEdge = findMinimumEdge(node);
                if (minEdge != null && !mstEdges.contains(minEdge)) {
                    mstEdges.add(minEdge);
                    node.inMST=true;
                    Node t = graph.getEdgeSource(minEdge);
                    t.inMST = true;
                    hasChanges = true;

                    if(node.level == t.level){
                        t.level++;
                        node.level++;
                    } else if (node.level < t.level) {
                        node.level = t.level;
                    }

                }
            }
        } while (hasChanges);

        return mstEdges;
    }


}
