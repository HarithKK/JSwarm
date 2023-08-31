package org.usa.soc.also;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class ALSO extends Algorithm {

    private int numberOfLizards =0;

    private Lizard[] lizards;

    double totalMass, totalLength, globalBest, globalWorst, lb, lt, mb, mt, c1, c2, Ib, It;

    public ALSO(
            ObjectiveFunction objectiveFunction,
            int numberOfLizards,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isLocalMinima,
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
        this.isLocalMinima = isLocalMinima;
        this.c1 = c1;
        this.c2 = c2;
        this.Ib = Ib;
        this.It = It;

        this.lizards = new Lizard[numberOfLizards];
        this.totalMass = totalMass;
        this.totalLength = totalLength;
        this.globalWorst = this.globalBest =0.0;

        this.lb = totalLength / 3;
        this.mb = totalMass * 9/10;
        this.lt = totalLength * (2/3);
        this.mt = totalLength/10;
    }

    @Override
    public void runOptimizer(int time) throws Exception{
        if (!this.isInitialized()) {
            throw new RuntimeException("Lizards Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();

        for (int step = 0; step < stepsCount; step++) {

            for(Lizard lizard: lizards){
                lizard.generateNewTorque(globalBest, globalWorst);

                lizard.calculateValues(totalMass, totalLength);
                lizard.calculateAngle();

                Double deltaTheta = (lizard.getBodyAngle() - lizard.getTailAngle()) * (Math.PI / 180);
                Double p1 = lizard.getTourque() * 0.3 * deltaTheta;
                Vector p2 = lizard.getLbest()
                        .operate(Vector.OPERATOR.SUB, lizard.getPosition())
                        .operate(Vector.OPERATOR.MULP, c1)
                        .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                Vector p3 = this.gBest.getClonedVector()
                        .operate(Vector.OPERATOR.SUB, lizard.getPosition())
                        .operate(Vector.OPERATOR.MULP, c2)
                        .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                Vector newP = lizard.getPosition()
                        .operate(Vector.OPERATOR.ADD, p1)
                        .operate(Vector.OPERATOR.ADD, p2)
                        .operate(Vector.OPERATOR.ADD, p3)
                        .fixVector(minBoundary, maxBoundary);
                lizard.setPosition(newP);
                lizard.setFitnessValue(objectiveFunction.setParameters(newP.getPositionIndexes()).call());

                if(Validator.validateBestValue(lizard.getFitnessValue(), lizard.getLbestValue(), isLocalMinima)){
                    lizard.setLbest(lizard.getPosition());
                    lizard.setLbestValue(lizard.getFitnessValue());
                }
            }

            for(Lizard lizard: lizards){
                updateGBest(lizard);
            }

            if (this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
            stepCompleted(time, step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        for(int i=0; i<numberOfLizards; i++){
            Lizard lizard = new Lizard(numberOfDimensions, minBoundary, maxBoundary, lb, lt, mb, mt, Ib, It);
            lizard.setFitnessValue(objectiveFunction.setParameters(lizard.getPosition().getPositionIndexes()).call());
            lizard.setLbest(lizard.getPosition());
            lizard.setLbestValue(lizard.getFitnessValue());
            this.lizards[i] = lizard;
        }

        for(Lizard lizard: lizards){
            updateGBest(lizard);
        }
    }

    private void updateGBest(Lizard lizard) {
        Double fpbest = this.objectiveFunction.setParameters(lizard.getLbest().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.gBest.setVector(lizard.getPosition());
            this.globalBest = fpbest;
        }

        if(Validator.validateBestValue(fpbest, fgbest, !isLocalMinima)){
            this.globalWorst = fpbest;
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.numberOfLizards*2];
        for(int i=0; i< this.numberOfLizards; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.lizards[i].getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
