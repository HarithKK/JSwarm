package examples.si.algo.mfa;

import examples.si.algo.tsoa.Tree;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.Flag;
import org.usa.soc.core.ds.Markers;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.si.Agent;
import org.usa.soc.si.AgentComparator;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MFA extends SIAlgorithm {

    private int numberOfMoths;

    private double b;

    public MFA (ObjectiveFunction<Double> objectiveFunction,
                int stepsCount,
                int numberOfDimensions,
                int numberOfMoths,
                double[] minBoundary,
                double[] maxBoundary,
                double b){

        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfMoths = numberOfMoths;
        this.b = b;

        this.gBest = isGlobalMinima.isSet() ? new Vector(numberOfDimensions).setMaxVector() : new Vector(numberOfDimensions).setMinVector();

        try{
            addAgents("moths", Markers.CIRCLE, Color.BLUE);
            addAgents("flames", Markers.CIRCLE, Color.RED);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void step() throws Exception {

            if(currentStep == 0){
                for (int i=0; i<numberOfMoths; i++) {
                    Moth m = (Moth)  getAgents("moths").getAgents().get(i);
                    Flame f = new Flame(m.getPosition());
                    f.setFitnessValue(m.getFitnessValue());
                    getAgents("flames").getAgents().add(f);
                }
                getAgents("flames").sort(1);
                this.gBest.setVector(getAgents("flames").getAgents().get(0).getPosition());
            }else{
                List<AbsAgent> af = new ArrayList<>(getAgents("flames").getAgents());

                for(AbsAgent a: getAgents("moths").getAgents()){
                    Moth m = (Moth) a;
                    Flame f = new Flame(m.getPosition());
                    f.calcFitnessValue(objectiveFunction);
                    af.add(f);
                }

                Collections.sort(af, (Comparator) new AgentComparator());
                getAgents("flames").getAgents().clear();
                getAgents("flames").setAgents(af.subList(0, numberOfMoths));

                updateGBest();
            }

            double a = -1.0 + (double)(currentStep + 1) * (-1.0 / (double)stepsCount);
            int flameNo = (int) Math.ceil(numberOfMoths - (currentStep + 1) * ((double)(numberOfMoths - 1) / (double)stepsCount));
            Flame flameP = (Flame)  getAgents("flames").getAgents().get(flameNo-1);

            for (int i=0 ; i<numberOfMoths ;i++){
                Moth moth = (Moth)getAgents("moths").getAgents().get(i);
                Flame flame = (Flame)getAgents("flames").getAgents().get(i);

                double distance = flame.getPosition().getDistance(moth.getPosition());

                Vector t = Randoms.getRandomVector(numberOfDimensions, 0, 1)
                        .operate(Vector.OPERATOR.MULP, (a - 1.0))
                        .operate(Vector.OPERATOR.ADD, 1.0);
                Vector c1 = t.getClonedVector().operate(Vector.OPERATOR.MULP, 2 * Math.PI).toCos();
                Vector c2 = t.getClonedVector().operate(Vector.OPERATOR.MULP, this.b).toExp();
                Vector firstComponent = c1.operate(Vector.OPERATOR.MULP, c2).operate(Vector.OPERATOR.MULP, distance);
                Vector position;
                if(i <= flameNo){
                    position = flame.getPosition().operate(Vector.OPERATOR.ADD, firstComponent);
                }else{
                    position = flameP.getPosition().operate(Vector.OPERATOR.ADD, firstComponent);
                }
                ((Moth)getAgents("moths").getAgents().get(i)).setPosition(position.getClonedVector().fixVector(minBoundary, maxBoundary));
                ((Moth)getAgents("moths").getAgents().get(i)).setFitnessValue(objectiveFunction.setParameters(position.getPositionIndexes()).call());
            }
    }

    private void updateGBest() {
        Flame f = (Flame) getAgents("flames").getAgents().get(0);
        if (f.fitnessValue < this.objectiveFunction.setParameters(gBest.getPositionIndexes()).call()) {
            this.gBest.setVector(f.getPosition());
        }
    }

    @Override
    public void initialize() {
        setInitialized(true);

        for(int i=0; i< numberOfMoths; i++){
            Moth moth = new Moth(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary));
            moth.calcFitnessValue(objectiveFunction);
            getAgents("moths").getAgents().add(moth);
        }
    }
}



