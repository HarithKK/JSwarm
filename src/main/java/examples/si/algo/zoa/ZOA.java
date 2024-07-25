package examples.si.algo.zoa;

import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class ZOA extends SIAlgorithm {

    private int populationSize;

    private Zebra pioneerZebra, attackedZebra;

    public ZOA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima
    ) {

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        setFirstAgents("zeebras", new ArrayList<>(populationSize));
        this.pioneerZebra = new Zebra(numberOfDimensions, minBoundary, maxBoundary);
    }

    @Override
    public void step() throws Exception{
        if(!this.isInitialized()){
            throw new RuntimeException("Squirrels Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        try{
            for(int step = 0; step< getStepsCount(); step++){

                for(Zebra zebra: (Zebra[]) getFirstAgents().toArray()){
                    // phase 1
                    long I = Math.round(1 + Randoms.rand(0,1));
                    Vector newX = pioneerZebra.getPosition()
                            .operate(Vector.OPERATOR.SUB, zebra.getPosition().operate(Vector.OPERATOR.MULP, (double)I))
                            .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1))
                            .operate(Vector.OPERATOR.ADD, zebra.getPosition());
                    zebra.updatePosition(objectiveFunction, newX, isGlobalMinima.isSet());

                    // phase 2
                    if(Randoms.rand(0,1) <= 0.5){
                        double c = 0.01*(2*Randoms.rand(0,1) -1)*(1 - (step+1)/stepsCount);
                        Vector S1 = zebra.getPosition().operate(Vector.OPERATOR.MULP, (1+c));
                        zebra.updatePosition(objectiveFunction, S1, isGlobalMinima.isSet());
                        attackedZebra = zebra;
                    }else{
                        Vector S2 = attackedZebra.getPosition()
                                .operate(Vector.OPERATOR.SUB, zebra.getPosition().operate(Vector.OPERATOR.MULP, (double)I))
                                .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1))
                                .operate(Vector.OPERATOR.ADD, zebra.getPosition());
                        zebra.updatePosition(objectiveFunction, S2, isGlobalMinima.isSet());
                    }

                }

                for(Zebra zebra: (Zebra[]) getFirstAgents().toArray()){
                    updateGbest(zebra);
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

        for(int i=0; i<populationSize; i++){
            Zebra zebra = new Zebra(numberOfDimensions, minBoundary, maxBoundary);
            zebra.setFitnessValue(objectiveFunction.setParameters(zebra.getPosition().getPositionIndexes()).call());
            getFirstAgents().set(i,zebra);
        }

        for(Zebra zebra: (Zebra[]) getFirstAgents().toArray()){
            updateGbest(zebra);
        }

        attackedZebra = (Zebra) getFirstAgents().get(Randoms.rand(populationSize-1));
    }

    private void updateGbest(Zebra zebra) {
        double fgbest = objectiveFunction.setParameters(gBest.getClonedVector().getPositionIndexes()).call();
        if(Validator.validateBestValue(zebra.getFitnessValue(), fgbest, isGlobalMinima.isSet())){
            gBest.setVector(zebra.getPosition());
            pioneerZebra = zebra;
        }
    }
}
