package org.usa.soc.avoa;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AVOA extends Algorithm {

    private int populationSize;

    private Vulture[] vultures;

    private double alpha, beta, omega, p1, p2, p3;

    public AVOA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isLocalMinima,
            double omega,
            double alpha,
            double beta,
            double p1,
            double p2,
            double p3
    ) {

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = isLocalMinima ? new Vector(this.numberOfDimensions).setMaxVector() : new Vector(this.numberOfDimensions).setMinVector();
        this.isLocalMinima = isLocalMinima;
        this.omega = omega;
        this.alpha = alpha;
        this.beta = beta;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

        this.vultures = new Vulture[populationSize];
    }

    @Override
    public void runOptimizer(int time) {
        if(!this.isInitialized()){
            throw new RuntimeException("Vultures Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        try{
            for(int step = 0; step< getStepsCount(); step++){

                Vulture[] firstBestVultures = findBestVultures();
                double F = calculateF(step+1);

                for (Vulture vulture: vultures) {
                    Vulture randomVulture = randomSelectVulture(firstBestVultures[0], firstBestVultures[1]);
                    if(vulture != randomVulture){
                        Vector newPosition = null;
                        if(Math.abs(F) >= 1){
                            if(Randoms.rand(0,1) < p1){
                                Vector DF = randomVulture.getPosition().
                                        operate(Vector.OPERATOR.SUB, vulture.getPosition()).
                                        operate(Vector.OPERATOR.MULP, (2 * Randoms.rand(0,1))).
                                        operate(Vector.OPERATOR.MULP, F);
                                newPosition = randomVulture.getPosition().operate(Vector.OPERATOR.SUB, DF);
                            }else{
                                Vector randomVector = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
                                newPosition = randomVector.operate(Vector.OPERATOR.SUB, F)
                                        .operate(Vector.OPERATOR.ADD, randomVector.operate(Vector.OPERATOR.MULP, Randoms.rand(0,1)));
                            }
                        }else if(Math.abs(F) >= 0.5){
                            if(Randoms.rand(0,1) < p2){
                                Vector DF = randomVulture.getPosition()
                                        .operate(Vector.OPERATOR.SUB, vulture.getPosition())
                                        .operate(Vector.OPERATOR.MULP, (2 * Randoms.rand(0,1)))
                                        .operate(Vector.OPERATOR.MULP, (F * Randoms.rand(0,1)));
                                newPosition = DF.operate(Vector.OPERATOR.SUB, randomVulture.getPosition().operate(Vector.OPERATOR.SUB, vulture.getPosition()));
                            }else{
                                Vector s0 = vulture.getPosition()
                                        .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1))
                                        .operate(Vector.OPERATOR.MULP, (2 * Math.PI));
                                Vector s1 = randomVulture.getPosition()
                                        .operate(Vector.OPERATOR.MULP, s0)
                                        .operate(Vector.OPERATOR.MULP, Math.cos(vulture.getPosition().getMagnitude()));
                                Vector s2 = randomVulture.getPosition()
                                        .operate(Vector.OPERATOR.MULP, s0)
                                        .operate(Vector.OPERATOR.MULP, Math.sin(vulture.getPosition().getMagnitude()));
                                newPosition = randomVulture.getPosition()
                                        .operate(Vector.OPERATOR.SUB,s1.operate(Vector.OPERATOR.ADD, s2));
                            }
                        }else{
                            if(Randoms.rand(0,1) < p3){
                                Vector CA11 = firstBestVultures[0].getPosition().operate(Vector.OPERATOR.MULP, vulture.getPosition());
                                Vector CA12 = firstBestVultures[0].getPosition().operate(Vector.OPERATOR.SUB, vulture.getPosition().operate(Vector.OPERATOR.MULP,vulture.getPosition()));
                                Vector CA1 = CA11.operate(Vector.OPERATOR.DIV, CA12).operate(Vector.OPERATOR.MULP, F);
                                Vector A1 = firstBestVultures[0].getPosition().operate(Vector.OPERATOR.SUB, CA1);

                                Vector CA21 = firstBestVultures[0].getPosition().operate(Vector.OPERATOR.MULP, vulture.getPosition());
                                Vector CA22 = firstBestVultures[0].getPosition().operate(Vector.OPERATOR.SUB, vulture.getPosition().operate(Vector.OPERATOR.MULP,vulture.getPosition()));
                                Vector CA2 = CA21.operate(Vector.OPERATOR.DIV, CA22).operate(Vector.OPERATOR.MULP, F);
                                Vector A2 = firstBestVultures[0].getPosition().operate(Vector.OPERATOR.SUB, CA2);

                                newPosition = A1.operate(Vector.OPERATOR.ADD, A2).operate(Vector.OPERATOR.DIV, 2.0);
                            }else{
                                Vector s2 = randomVulture.getPosition()
                                        .operate(Vector.OPERATOR.SUB, vulture.getPosition())
                                        .toAbs()
                                        .operate(Vector.OPERATOR.MULP, F * Commons.levyflight(numberOfDimensions));
                                newPosition = randomVulture.getPosition()
                                        .operate(Vector.OPERATOR.SUB, s2);
                            }
                        }

                        vulture.setPosition(newPosition.fixVector(minBoundary, maxBoundary));
                        vulture.setFitnessValue(objectiveFunction.setParameters(vulture.getPosition().getPositionIndexes()).call());

                        Double fgbest = this.objectiveFunction.setParameters(vulture.getLbest().getPositionIndexes()).call();
                        Double fpbest = this.objectiveFunction.setParameters(vulture.getPosition().getPositionIndexes()).call();

                        vulture.setLbest(vulture.getPosition());
                        if (Validator.validateBestValue(fpbest, fgbest, isLocalMinima)) {
                            //  vulture.setLbest(vulture.getPosition());
                        }
                    }
                }

                for(Vulture vulture: vultures){
                    updateGBest(vulture);
                }

                if(this.stepAction != null)
                    this.stepAction.performAction(this.gBest.getClonedVector(), this.getBestDoubleValue(), step);
                stepCompleted(time, step);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private void updateGBest(Vulture vulture) {
        Double fgbest = this.objectiveFunction.setParameters(gBest.getPositionIndexes()).call();
        Double fpbest = this.objectiveFunction.setParameters(vulture.getLbest().getPositionIndexes()).call();
        if (Validator.validateBestValue(fpbest, fgbest, isLocalMinima)) {
            this.gBest.setVector(vulture.getLbest());
        }
    }

    private Vulture randomSelectVulture(Vulture firstBestVulture, Vulture secondBestVulture) {
        int p = Commons.rouletteWheelSelection(new double[]{alpha, beta});
        return p > 0 ? firstBestVulture : secondBestVulture;
    }

    private Vulture[] findBestVultures(){


        List<Vulture> sortedVultures = Arrays.asList(vultures);
        Collections.sort(sortedVultures, new VultureComparator());

        return new Vulture[]{sortedVultures.get(0), sortedVultures.get(1)};
    }

    public double calculateF(double term){
        double t = Randoms.rand(-2, 2) * (Mathamatics.pow(Math.sin(Math.PI * term/2), omega) + Math.cos(Math.PI * term/2) -1 );
        return ((2 * Randoms.rand(0,1) )+1) * Randoms.rand(-1,1) * (1 - term) + t;
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);
        Validator.validatePosition(0,1,p1);
        Validator.validatePosition(0,1,p2);
        Validator.validatePosition(0,1,p3);

        if(populationSize < 2){
            throw new RuntimeException("At least two vultures needed");
        }

        for(int i=0; i<populationSize;i++){
            Vulture vulture = new Vulture(numberOfDimensions, minBoundary, maxBoundary);
            vulture.setFitnessValue(objectiveFunction.setParameters(vulture.getPosition().getPositionIndexes()).call());
            vultures[i] = vulture;
        }
    }

    @Override
    public double[][] getDataPoints() {
        double[][] data = new double[this.numberOfDimensions][this.populationSize*2];
        for(int i=0; i< this.populationSize; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.vultures[i].getPosition().getValue(j),2);
            }
        }
        return data;
    }
}
