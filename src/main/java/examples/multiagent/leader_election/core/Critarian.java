package examples.multiagent.leader_election.core;

import examples.multiagent.leader_election.GHS_NASA_JPL.GHS;
import examples.multiagent.leader_election.GHS_NASA_JPL.Node;
import examples.multiagent.leader_election.testcases.OF;
import examples.si.algo.also.ALSO;
import examples.si.algo.cso.CSO;
import examples.si.algo.mfa.MFA;
import examples.si.algo.pso.PSO;
import examples.si.algo.tsoa.TSOA;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.usa.soc.comparators.ParetoComparator;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.action.StepAction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.util.ParetoUtils;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.StringFormatter;

import javax.swing.plaf.nimbus.State;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Critarian {

    private int populationSize = 10;
    private int iterationCount = 30;

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public int random(List<AbsAgent> firstAgents) {
        List<Integer>ty = firstAgents.stream().map(d->d.getIndex()).collect(Collectors.toList());
        return ty.get(Randoms.rand(0, ty.size()-1));
    }

    public int TSOA_IND(StateSpaceModel model, List<Drone> layer) {
        List<Drone.Tree> trees = new ArrayList<>();
        ParetoComparator<Drone.Tree> comparators = new ParetoComparator<>();
        comparators.add(new Comparator<Drone.Tree>() {
            @Override
            public int compare(Drone.Tree o1, Drone.Tree o2) {
                return Double.compare(o1.f1, o2.f1);
            }
        });
        comparators.add(new Comparator<Drone.Tree>() {
            @Override
            public int compare(Drone.Tree o1, Drone.Tree o2) {
                return Double.compare(o1.f2, o2.f2);
            }
        });

        for(Drone d: layer){
            trees.add(d.executeTSOA(10, model));
        }

        Collection<Drone.Tree> po = ParetoUtils.getMinimalFrontierOf(trees, comparators);
        int tx = po.iterator().next().index;
        po.clear();
        po = null;

//        trees.sort(new Comparator<Drone.Tree>() {
//            @Override
//            public int compare(Drone.Tree o1, Drone.Tree o2) {
//                if(o1.f1 < o2.f1){
//                    return -1;
//                }else{
//                    return 1;
//                }
//            }
//        });
//        int tx = trees.get(0).index;

        trees = null;
        comparators = null;
        System.gc();
        return tx;
    }

    public enum Critarians {
        RANDOM
    }

    public enum SICritatianType {
        TSOA, CSO, ALSO, MFA, PSO
    }

    public int selectCritarian(Critarians ctype, List<Drone> layer){
        switch (ctype){
            case RANDOM:
                return random(0, layer.size()-1);
        }
        return 0;
    }

    public int random(int i, int j){
        int x = Randoms.rand(i, j);
        System.out.println("Random:"+ x);
        return x;
    }

    private SIAlgorithm getSIAlgorithm(ObjectiveFunction objectiveFunction, SICritatianType critatianType){
        switch (critatianType){
            case TSOA: return new TSOA(
                    objectiveFunction,
                    populationSize,
                    iterationCount,
                    objectiveFunction.getNumberOfDimensions(),
                    objectiveFunction.getMin(),
                    objectiveFunction.getMax(),
                    true,
                    10,
                    0.3,
                    1,
                    1.49,
                    1.5
            );
            case ALSO:return new ALSO(
                    objectiveFunction,
                    populationSize,
                    iterationCount,
                    objectiveFunction.getNumberOfDimensions(),
                    objectiveFunction.getMin(),
                    objectiveFunction.getMax(),
                    true,
                    10,
                    210,
                    2.5,
                    0.1,
                    1.0,
                    1.0
            );
            case MFA:
                return new MFA(
                        objectiveFunction,
                        iterationCount,
                        objectiveFunction.getNumberOfDimensions(),
                        populationSize,
                        objectiveFunction.getMin(),
                        objectiveFunction.getMax(),
                        1.0
                );
            case PSO:
                return new PSO(
                        objectiveFunction,
                        populationSize,
                        objectiveFunction.getNumberOfDimensions(),
                        iterationCount,
                        1.496180,
                        1.496180,
                        0.729844,
                        objectiveFunction.getMin(),
                        objectiveFunction.getMax(),
                        true);
            case CSO:
                return new CSO(
                        objectiveFunction,
                        objectiveFunction.getNumberOfDimensions(),
                        iterationCount,
                        populationSize,
                        0.1,
                        objectiveFunction.getMin(),
                        objectiveFunction.getMax(),
                        3,
                        0.85,
                        0.2,
                        true,
                        2.05,
                        0.2,
                        true
                );

        }
        return null;
    }

    private double findBestValue(StateSpaceModel model, Drone drone, SICritatianType critatianType){
        OF objectiveFunction = new OF(model.calcGcStep(model.getNN(), 1), drone.getIndex(), drone.getPosition().getClonedVector().toPoint2D());

        SIAlgorithm algorithm = getSIAlgorithm(objectiveFunction, critatianType);

        if(algorithm == null){
            throw new RuntimeException("Invalid Selection of Algorithm");
        }

        algorithm.addStepAction(new StepAction() {
            @Override
            public void performAction(Vector best, Double bestValue, int step) {
                objectiveFunction.gc = model.calcGcStep(objectiveFunction.gc, step+1);
            }
        });


        try {
            algorithm.initialize();
            algorithm.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(critatianType.name()+": "+algorithm.getBestDoubleValue());
        return algorithm.getBestDoubleValue();
    }

    public AbsAgent SI(StateSpaceModel model, List<Drone> agents, SICritatianType critatianType){

        AbsAgent minAgent = null;
        double minValue=0;

        for(AbsAgent agent: agents){
            double fValue = findBestValue(model, (Drone) agent, critatianType);

            if(minAgent == null){
                minAgent = agent;
                minValue = fValue;
            }else{
                if(fValue < minValue){
                    minAgent = agent;
                    minValue = fValue;
                }
            }
        }

        return minAgent;
    }

    private long getRandomTimeOut(){
        return Randoms.rand(500, 1000);
    }

    public AbsAgent Raft(StateSpaceModel model, List<Drone> agents, int npLinks){

        // convert all agents as followers
        for(Drone dr: agents){
            dr.currentState = RaftState.FOLLOWER;
            dr.log.add(new LogEntry(1, -1, "start"));
        }

        int candidateId =0;
        while(true){
            try {
                Thread.sleep(getRandomTimeOut());

                Drone candidate = (Drone)agents.get(candidateId++);
                candidate.initCandidate();
                candidate.updateCandidate(model.GA, agents, npLinks);

                System.out.println(candidate.getIndex() +": " +candidate.currentState.name()+", id="+candidateId);
                if(candidate.currentState == RaftState.LEADER){
                    return candidate;
                }

                if(candidateId >= agents.size()){
                    candidateId =0;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Node findMinJ(DirectedAcyclicGraph<Node,DefaultEdge> graph){

        Node minNode =  null;

        for(Node s: graph.vertexSet()){

            if(minNode == null){
                minNode = s;
            }else{
                minNode = minNode.J < s.J ? minNode : s;
            }

            for(DefaultEdge e: graph.edgesOf(s)){
                Node t = graph.getEdgeTarget(e);
                minNode = minNode.J < t.J ? minNode : t;
            }
        }

        return minNode;
    }

    public Drone GHS(StateSpaceModel model, List<Drone> layer) {
        GHS ghs = new GHS(layer, model.GA);
        Node m = findMinJ(ghs.getGraph(ghs.findMST()));

        return layer.stream().filter(d -> d.getIndex() == m.index).findFirst().get();
    }

    public int TSOA(StateSpaceModel model, List<Drone> layer){
        LE_TSOA algo = new LE_TSOA(model, layer, 10);
        algo.run();
        return algo.getBestIndex();
    }

    public LE_TSOA TSOA_WPF(StateSpaceModel model, List<Drone> layer){
        LE_TSOA algo = new LE_TSOA(model, layer, 15);
        algo.run();
        return algo;
    }

}
