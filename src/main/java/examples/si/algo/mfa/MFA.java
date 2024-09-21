package examples.si.algo.mfa;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.ds.Markers;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.si.Agent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
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
                for (AbsAgent agent: getAgents("moths").getAgents()) {
                    Moth m = (Moth) agent;
                    Flame f = new Flame(m.getPosition());
                    f.setFitnessValue(m.getFitnessValue());
                    getAgents("flames").getAgents().add(f);
                }
                getAgents("flames").sort(new FlameComparator());
            }else{

                for (AbsAgent agent: getAgents("moths").getAgents()) {
                    Moth m = (Moth) agent;
                    Flame f = new Flame(m.getPosition());
                    f.setFitnessValue(m.getFitnessValue());
                    getAgents("flames").getAgents().add(f);
                }
                getAgents("flames").sort(new FlameComparator());
                getAgents("flames").setAgents(getAgents("flames").getAgents().subList(0, numberOfMoths));
            }

            this.gBest.setVector(getAgents("flames").getAgents().get(0).getPosition().getClonedVector());

            double a = -1.0 + (double)(currentStep + 1) * (-1.0 / (double)stepsCount);
            int flameNo = (int) Math.round(numberOfMoths - (currentStep + 1) * ((double)(numberOfMoths - 1) / (double)stepsCount));
            System.out.println(flameNo);
            for (int i=0 ; i<numberOfMoths ;i++){
                Flame tmpFlame = i <= flameNo ? (Flame) getAgents("flames").getAgents().get(i) : (Flame) getAgents("flames").getAgents().get(flameNo);
                double distance = getAgents("flames").getAgents().get(i).getPosition().getDistance(getAgents("moths").getAgents().get(i).getPosition());

                double t = (a - 1.0) * Randoms.rand(0, 1) + 1.0;
                double firstComponent = distance * Math.exp(this.b * t) * Math.cos(2 * Math.PI * t);

                Vector position = tmpFlame.getPosition().operate(Vector.OPERATOR.ADD, firstComponent).fixVector(minBoundary, maxBoundary);
                getAgents("moths").getAgents().get(i).setPosition(position);
                ((Moth)getAgents("moths").getAgents().get(i)).setFitnessValue(objectiveFunction.setParameters(position.getPositionIndexes()).call());
            }

    }

    @Override
    public void initialize() {
        setInitialized(true);

        for(int i=0; i< numberOfMoths; i++){
            Moth moth = new Moth(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0 , 1).fixVector(minBoundary, maxBoundary));
            moth.setFitnessValue(objectiveFunction.setParameters(moth.getPosition().getPositionIndexes()).call());
            getAgents("moths").getAgents().add(moth);
        }
    }
}
