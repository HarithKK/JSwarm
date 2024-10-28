package examples.multiagent.leader_election.core;

import examples.multiagent.leader_election.testcases.OF;
import examples.si.algo.tsoa.TSOA;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.action.StepAction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.runners.FunctionsFactory;
import org.usa.soc.si.runners.Main;
import org.usa.soc.util.Randoms;

import java.util.List;

public class Critarian {

    public enum Critarians {
        RANDOM
    }

    public int selectCritarian(Critarians ctype, List<Drone> layer){
        switch (ctype){
            case RANDOM:
                return random(0, layer.size()-1);
        }
        return 0;
    }

    public int random(int i, int j){
        return Randoms.rand(i, j);
    }

    private double findBestValue(StateSpaceModel model, Drone drone){
        OF objectiveFunction = new OF(model.calcGcStep(model.getNN(), 1), drone.getIndex(), drone.getPosition().getClonedVector().toPoint2D());

        SIAlgorithm algorithm = new TSOA(
                objectiveFunction,
                100,
                15,
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

        return algorithm.getBestDoubleValue();
    }

    public AbsAgent SI(StateSpaceModel model, List<AbsAgent> agents){

        AbsAgent minAgent = null;
        double minValue=0;

        for(AbsAgent agent: agents){
            double fValue = findBestValue(model, (Drone) agent);

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
