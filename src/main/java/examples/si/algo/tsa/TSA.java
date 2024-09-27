package examples.si.algo.tsa;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class TSA extends SIAlgorithm {
    private int populationSize;

    private Vector A, G, F, M;

    private double pmin, pmax;

    public TSA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima
    ){

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isGlobalMinima.setValue(isGlobalMinima);

        setFirstAgents("tunicates", new ArrayList<>(populationSize));
        this.pmax = 4;
        this.pmin = 1;
        this.A = new Vector(numberOfDimensions);
        this.G = new Vector(numberOfDimensions);
        this.F = new Vector(numberOfDimensions);
        this.M = new Vector(numberOfDimensions);
    }

    @Override
    public void step() throws Exception{

            for (AbsAgent agent: getFirstAgents()) {
                Tunicate tunicate = (Tunicate)agent;
                calculateConstVectors();
                Vector VPD = this.getGBest().getClonedVector()
                        .operate(Vector.OPERATOR.SUB, tunicate.getPosition().operate(Vector.OPERATOR.MULP, Randoms.rand(0,1)));
                Double PD = VPD.getMagnitude();
                Vector newP;
                if(Randoms.rand(0,1) <= 0.5){
                    newP = tunicate.getPosition().operate(Vector.OPERATOR.ADD, this.A.operate(Vector.OPERATOR.MULP, PD));
                }else{
                    newP = tunicate.getPosition().operate(Vector.OPERATOR.SUB, this.A.operate(Vector.OPERATOR.MULP, PD));
                }

                newP = tunicate.getPosition().operate(Vector.OPERATOR.ADD, newP.getClonedVector())
                        .operate(Vector.OPERATOR.DIV, ( 2 + Randoms.rand(0,1)));
                tunicate.setPosition(newP.fixVector(minBoundary, maxBoundary));
                tunicate.setFitnessValue(getObjectiveFunction().setParameters(tunicate.getPosition().getPositionIndexes()).call());
            }

            for (AbsAgent agent: getFirstAgents()) {
                Tunicate tunicate = (Tunicate)agent;
                updateGBest(tunicate);
            }
    }

    private void calculateConstVectors() {

        this.F.setVector(Randoms.getRandomVector(numberOfDimensions, 0, 1)
                .operate(Vector.OPERATOR.MULP, 2.0));
        this.G.setVector(Randoms.getRandomVector(numberOfDimensions, 0, 1)
                .operate(Vector.OPERATOR.ADD, Randoms.getRandomVector(numberOfDimensions, 0, 1))
                .operate(Vector.OPERATOR.SUB, this.F.getClonedVector()));
        this.M.setVector(Randoms.getRandomVector(numberOfDimensions, 0, 1)
                .operate(Vector.OPERATOR.MULP, (this.pmax - this.pmin))
                .operate(Vector.OPERATOR.ADD, pmin));
        this.A.setVector(this.G.getClonedVector()
                .operate(Vector.OPERATOR.DIV, this.M.getClonedVector()));

    }


    @Override
    public void initialize() {
        setInitialized(true);

        for(int i=0; i<populationSize; i++){
            Tunicate tunicate = new Tunicate(numberOfDimensions, minBoundary, maxBoundary);
            tunicate.setFitnessValue(getObjectiveFunction().setParameters(tunicate.getPosition().getPositionIndexes()).call());
            getFirstAgents().add( tunicate);
            updateGBest(tunicate);
        }

    }

    private void updateGBest(Tunicate tunicate) {
        Double fgbest = this.getObjectiveFunction().setParameters(gBest.getPositionIndexes()).call();
        Double fpbest = this.getObjectiveFunction().setParameters(tunicate.getPosition().getPositionIndexes()).call();
        if (Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())) {
            this.gBest.setVector(tunicate.getPosition());
        }
    }

}
