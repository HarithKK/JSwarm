package examples.si.algo.gwo;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class GWO extends SIAlgorithm {

    private static final Double MAX_A = 2.0;
    private int numberOfWolfs;
    private Wolf alpha, beta, delta;

    public GWO (ObjectiveFunction<Double> objectiveFunction,
                int stepsCount,
                int numberOfDimensions,
                int numberOfWolfs,
                double[] minBoundary,
                double[] maxBoundary,
                boolean isGlobalMinima){

        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.numberOfWolfs = numberOfWolfs;

        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        setFirstAgents("Wolves", new ArrayList<>(numberOfWolfs));
    }

    @Override
    public void step() throws Exception{
            double aDecrement = 2 - currentStep * (2 / stepsCount);
            for(int i=0; i<numberOfWolfs; i++){
                Wolf w = (Wolf)getFirstAgents().get(i);
                Vector X1 = getUpdatedPositionVector(w, alpha, aDecrement);
                Vector X2 = getUpdatedPositionVector(w, beta, aDecrement);
                Vector X3 = getUpdatedPositionVector(w, delta, aDecrement);

                Vector newX = X1
                        .operate(Vector.OPERATOR.ADD, X2)
                        .operate(Vector.OPERATOR.ADD, X3)
                        .operate(Vector.OPERATOR.DIV, 3.0)
                        .fixVector(minBoundary, maxBoundary);

                if( objectiveFunction.setParameters(newX.getPositionIndexes()).call() < w.getFitnessValue()){
                    w.setPosition(newX);
                    w.calcFitnessValue(objectiveFunction);
                }
            }

            updateWolfHirarchy();
    }

    private Vector getUpdatedPositionVector(Wolf w, Wolf prey, double a){

        double A = a*(2*Randoms.rand(0,1)- 1);
        double C = 2*Randoms.rand(0,1);
        Vector D = prey.getPosition().getClonedVector()
                .operate(Vector.OPERATOR.MULP, C)
                .operate(Vector.OPERATOR.SUB, w.getPosition().getClonedVector());
        return prey.getPosition().getClonedVector().operate(Vector.OPERATOR.SUB, D.toAbs().operate(Vector.OPERATOR.MULP, A)).fixVector(minBoundary, maxBoundary);
    }

    @Override
    public void initialize() {

        if(numberOfWolfs < 3){
            throw new RuntimeException("Wolfs count should be greater than 3");
        }

        setInitialized(true);

        for(int i =0;i< numberOfWolfs; i++){
            Wolf w = new Wolf(numberOfDimensions, minBoundary, maxBoundary);
            w.calcFitnessValue(objectiveFunction);
            getFirstAgents().add(w);
        }

        sort();

        this.alpha = ((Wolf) getFirstAgents().get(0)).getClonedWolf();
        this.beta = ((Wolf) getFirstAgents().get(1)).getClonedWolf();
        this.delta = ((Wolf) getFirstAgents().get(2)).getClonedWolf();

        updateWolfHirarchy();
    }

    private void updateWolfHirarchy(){

        sort();

        this.alpha = ((Wolf) getFirstAgents().get(0)).getClonedWolf();
        this.beta = ((Wolf) getFirstAgents().get(1)).getClonedWolf();
        this.delta = ((Wolf) getFirstAgents().get(2)).getClonedWolf();

        if(Validator.validateBestValue(alpha.getFitnessValue(), getBestDoubleValue(), isGlobalMinima.isSet())){
            this.gBest.setVector(this.alpha.getPosition());
        }
    }
}
