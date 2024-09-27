package examples.si.algo.geo;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class GEO extends SIAlgorithm {

    private int numberOfEagles;

    private double pa0, paT, pc0, pcT, pa, pc;

    public GEO(
            ObjectiveFunction objectiveFunction,
            int numberOfEagles,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
            double pa0,
            double paT,
            double pc0,
            double pcT
    ) {
        this.objectiveFunction = objectiveFunction;
        this.numberOfEagles = numberOfEagles;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.pa0 = pa0;
        this.paT = paT;
        this.pc0 = pc0;
        this.pcT = pcT;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        setFirstAgents("Eagles", new ArrayList<>(numberOfEagles));
    }

    @Override
    public void step() throws Exception{

            // update Pa and Pc
            pa = pa0 + (currentStep/ stepsCount)*(paT - pa0);
            pc = pc0 - (currentStep/ stepsCount)*(pcT - pc0);

            for(AbsAgent agent : getFirstAgents()){
                Eagle eagle = (Eagle)agent;
                // Random prey location
                Eagle prey = (Eagle) getFirstAgents().get(Randoms.rand(numberOfEagles));

                Vector attackVector = prey.getLocalBestPositon().operate(Vector.OPERATOR.SUB, eagle.getPosition());

                if(attackVector.getMagnitude() != 0){
                    double d = eagle.getPosition().operate(Vector.OPERATOR.MULP,attackVector).getNonDistanceMagnitude();
                    Vector c = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);

                    int r = Randoms.rand(numberOfDimensions);
                    double ck = (d - (attackVector.getNonDistanceMagnitude() - attackVector.getValue(r)))/attackVector.getValue(r);
                    c.setValue(ck, r);

                    Vector p1 = Randoms.getRandomVector(numberOfDimensions,  0,1)
                            .operate(Vector.OPERATOR.MULP, pa)
                            .operate(Vector.OPERATOR.MULP, attackVector.operate(Vector.OPERATOR.DIV, attackVector.getMagnitude()));
                    Vector p2 = Randoms.getRandomVector(numberOfDimensions,  0,1)
                            .operate(Vector.OPERATOR.MULP, pc)
                            .operate(Vector.OPERATOR.MULP, c.operate(Vector.OPERATOR.DIV, c.getMagnitude()));

                    Vector newX =  eagle.getPosition().operate(Vector.OPERATOR.ADD, p1.operate(Vector.OPERATOR.ADD, p2));
                    eagle.setPosition(newX.fixVector(minBoundary, maxBoundary));
                    eagle.setFitnessValue(getObjectiveFunction().setParameters(newX.getPositionIndexes()).call());
                    eagle.updateLocalBest(isGlobalMinima.isSet());
                }
            }

            for(AbsAgent agent : getFirstAgents()){
                Eagle eagle = (Eagle) agent;
                double globalBestFitnessValue = getObjectiveFunction().setParameters(this.gBest.getClonedVector().getPositionIndexes()).call();
                if(Validator.validateBestValue(eagle.getLocalBestFitnessValue(), globalBestFitnessValue, isGlobalMinima.isSet())){
                    this.gBest.setVector(eagle.getLocalBestPositon());
                }
            }
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        for (int i=0; i< numberOfEagles; i++){
            Eagle eagle = new Eagle(numberOfDimensions, minBoundary, maxBoundary);
            eagle.setFitnessValue(getObjectiveFunction().setParameters(eagle.getPosition().getPositionIndexes()).call());
            eagle.setLocalBestFitnessValue(getObjectiveFunction().setParameters(eagle.getLocalBestPositon().getPositionIndexes()).call());
            getFirstAgents().add(eagle);
        }

        for(AbsAgent agent : getFirstAgents()){
            Eagle eagle = (Eagle) agent;
            double globalBestFitnessValue = getObjectiveFunction().setParameters(this.gBest.getClonedVector().getPositionIndexes()).call();
            if(Validator.validateBestValue(eagle.getLocalBestFitnessValue(), globalBestFitnessValue, isGlobalMinima.isSet())){
                this.gBest.setVector(eagle.getLocalBestPositon());
            }
        }

    }
}
