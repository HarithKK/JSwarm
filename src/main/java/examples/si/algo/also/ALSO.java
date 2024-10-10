package examples.si.algo.also;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class ALSO extends SIAlgorithm {

    private int numberOfLizards =0;

    double totalMass, totalLength, globalBest, globalWorst, lb, lt, mb, mt, c1, c2, Ib, It;

    public ALSO(
            ObjectiveFunction objectiveFunction,
            int numberOfLizards,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
            double totalMass,
            double totalLength,
            double c1,
            double c2,
            double Ib,
            double It
    ) {
        this.objectiveFunction = objectiveFunction;
        this.numberOfLizards = numberOfLizards;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.c1 = c1;
        this.c2 = c2;
        this.Ib = Ib;
        this.It = It;

        setFirstAgents("lizards", new ArrayList<>(numberOfLizards));
        this.totalMass = totalMass;
        this.totalLength = totalLength;
        this.globalWorst = this.globalBest =0.0;

        this.lb = totalLength / 3;
        this.mb = totalMass * 9/10;
        this.lt = totalLength * (2/3);
        this.mt = totalLength/10;
    }

    @Override
    public void step() throws Exception{

            for(AbsAgent agent: getFirstAgents()){
                Lizard lizard = (Lizard)agent;
                lizard.generateNewTorque(globalBest, globalWorst);

                lizard.calculateValues(totalMass, totalLength);
                lizard.calculateAngle();

                Double deltaTheta = (lizard.getBodyAngle() - lizard.getTailAngle()) * (Math.PI / 180);
                Double p1 = lizard.getTourque() * 0.3 * deltaTheta;
                Vector p2 = lizard.getLbest().getClonedVector()
                        .operate(Vector.OPERATOR.SUB, lizard.getPosition().getClonedVector())
                        .operate(Vector.OPERATOR.MULP, c1)
                        .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                Vector p3 = this.gBest.getClonedVector()
                        .operate(Vector.OPERATOR.SUB, lizard.getPosition().getClonedVector())
                        .operate(Vector.OPERATOR.MULP, c2)
                        .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                Vector newP = lizard.getPosition()
                        .operate(Vector.OPERATOR.ADD, p1)
                        .operate(Vector.OPERATOR.ADD, p2)
                        .operate(Vector.OPERATOR.ADD, p3)
                        .fixVector(minBoundary, maxBoundary);
                lizard.setPosition(newP);
                lizard.setFitnessValue(getObjectiveFunction().setParameters(newP.getPositionIndexes()).call());

                if(lizard.getFitnessValue() < lizard.getLbestValue()){
                    lizard.setLbest(lizard.getPosition().getClonedVector());
                    lizard.setLbestValue(lizard.getFitnessValue());
                }
            }

            for(AbsAgent agent: getFirstAgents()){
              updateGBest((Lizard) agent);
            }

    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        for(int i=0; i<numberOfLizards; i++){
            Lizard lizard = new Lizard(numberOfDimensions, minBoundary, maxBoundary, lb, lt, mb, mt, Ib, It);
            lizard.setFitnessValue(getObjectiveFunction().setParameters(lizard.getPosition().getPositionIndexes()).call());
            lizard.setLbest(lizard.getPosition().getClonedVector());
            lizard.setLbestValue(lizard.getFitnessValue());
            getFirstAgents().add(lizard);
        }
        updateBestValueForce(Double.MAX_VALUE);
        for(AbsAgent agent: getFirstAgents()){
            updateGBest((Lizard) agent);
        }

        this.globalBest = Double.MAX_VALUE;
        this.globalWorst = Double.MIN_VALUE;
        return;
    }

    private void updateGBest(Lizard lizard) {
        if(Validator.validateBestValue(lizard.getLbestValue(), getBestValue(), isGlobalMinima.isSet())){
            this.gBest = lizard.getLbest().getClonedVector();
            this.updateBestValueForce(lizard.getLbestValue());
        }

        if(getBestValue() < this.globalBest){
            this.globalBest = getBestDoubleValue().doubleValue();
        }

        if(getBestValue() > this.globalWorst){
            this.globalWorst = getBestDoubleValue().doubleValue();
        }
    }
}
