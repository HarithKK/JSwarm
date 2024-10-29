package examples.multiagent.leader_election.core;

import examples.multiagent.leader_election.testcases.OF;
import examples.si.algo.also.ALSO;
import examples.si.algo.cso.CSO;
import examples.si.algo.mfa.MFA;
import examples.si.algo.pso.PSO;
import examples.si.algo.tsoa.TSOA;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.action.StepAction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.util.Randoms;

import java.util.List;

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

    public AbsAgent SI(StateSpaceModel model, List<AbsAgent> agents, SICritatianType critatianType){

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
}
