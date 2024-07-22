package examples.si.algo.choa;

import org.usa.soc.si.AgentComparator;
import org.usa.soc.si.Algorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CHOA extends Algorithm {

    private int populationSize;

    Chimp attacker, chaser, barrier, divider;

    double fUpper, f;
    private Chaotics.type chaoticType;


    public CHOA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
            double fUpper,
            Chaotics.type type
    ) {

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.fUpper = fUpper;
        this.chaoticType = type;

        this.agents = new ArrayList<>(populationSize);
    }

    @Override
    public void runOptimizer() throws Exception{
        if(!this.isInitialized()){
            throw new RuntimeException("Chimps Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        try{
            for(int step = 0; step< getStepsCount(); step++){

                Collections.sort(agents, new AgentComparator());
                f = fUpper*(1 - ((step+1)/ stepsCount));
                attacker = (Chimp) agents.get(populationSize-1);
                attacker.updateFMAC(f, chaoticType);
                chaser = (Chimp) agents.get(populationSize-2);
                chaser.updateFMAC(f, chaoticType);
                barrier = (Chimp) agents.get(populationSize-3);
                barrier.updateFMAC(f, chaoticType);
                divider = (Chimp) agents.get(populationSize-4);
                divider.updateFMAC(f, chaoticType);

                for(Chimp chimp: (Chimp[]) agents.toArray()){
                    chimp.updateFMAC(f, chaoticType);
                    chimp.updateDValues(attacker, chaser, barrier, divider);
                }

                for(Chimp chimp: (Chimp[]) agents.toArray()){
                    Vector newX;
                    double u = Randoms.rand(0,1);
                    if(u < 0.5){
                        if(Math.abs(chimp.getA()) < 1){
                            newX = chimp.getPositionFromPrey(this.gBest, f);
                        }else{
                            newX = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
                        }
                    }else if(u > 0.5){
                        Vector x1 = attacker.getPosition().operate(Vector.OPERATOR.SUB, chimp.getDa());
                        Vector x2 = attacker.getPosition().operate(Vector.OPERATOR.SUB, chimp.getDb());
                        Vector x3 = attacker.getPosition().operate(Vector.OPERATOR.SUB, chimp.getDc());
                        Vector x4 = attacker.getPosition().operate(Vector.OPERATOR.SUB, chimp.getDd());

                        newX = x1.operate(Vector.OPERATOR.ADD, x2)
                                .operate(Vector.OPERATOR.ADD, x3)
                                .operate(Vector.OPERATOR.ADD, x4)
                                .operate(Vector.OPERATOR.DIV, 4.0);
                    }else{
                        newX = chimp.getPosition();
                    }

                    chimp.setPosition(newX.fixVector(minBoundary, maxBoundary));
                    chimp.setFitnessValue(objectiveFunction.setParameters(chimp.getPosition().getPositionIndexes()).call());
                }

                for(Chimp chimp: (Chimp[]) agents.toArray()){
                    updateGBest(chimp);
                }

                if(this.stepAction != null)
                    this.stepAction.performAction(this.gBest.getClonedVector(), this.getBestDoubleValue(), step);
                stepCompleted(step);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    @Override
    public void initialize() {

        this.setInitialized(true);
        Validator.checkPopulationSize(populationSize, 4);

        for(int i=0;i <populationSize; i++){
            Chimp chimp = new Chimp(numberOfDimensions, minBoundary, maxBoundary);
            chimp.setFitnessValue(objectiveFunction.setParameters(chimp.getPosition().getPositionIndexes()).call());
            agents.add(chimp);

            updateGBest(chimp);
        }

    }

    private void updateGBest(Chimp chimp) {
        double fgbest = objectiveFunction.setParameters(this.gBest.getClonedVector().getPositionIndexes()).call();
        if(Validator.validateBestValue(chimp.getFitnessValue(), fgbest, isGlobalMinima.isSet())){
            this.gBest.setVector(chimp.getPosition());
        }
    }
}
