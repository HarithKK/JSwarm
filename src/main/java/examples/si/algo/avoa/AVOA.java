package examples.si.algo.avoa;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AVOA extends SIAlgorithm {

    private int populationSize;

    private double alpha, beta, omega, p1, p2, p3;

    public AVOA(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
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
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.omega = omega;
        this.alpha = alpha;
        this.beta = beta;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);

        setFirstAgents("vultures", new ArrayList<>(populationSize));
    }

    @Override
    public void step() throws Exception{

        try{
                Vulture[] firstBestVultures = findBestVultures();
                double F = calculateF(currentStep+1);

                for(AbsAgent agent: getFirstAgents()){
                    Vulture vulture = (Vulture)agent;
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
                        if (Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())) {
                            //  vulture.setLbest(vulture.getPosition());
                        }
                    }
                }

                for(AbsAgent agent: getFirstAgents()){
                    updateGBest((Vulture)agent);
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateGBest(Vulture vulture) {
        Double fgbest = this.objectiveFunction.setParameters(gBest.getPositionIndexes()).call();
        Double fpbest = this.objectiveFunction.setParameters(vulture.getLbest().getPositionIndexes()).call();
        if (Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())) {
            this.gBest.setVector(vulture.getLbest());
        }
    }

    private Vulture randomSelectVulture(Vulture firstBestVulture, Vulture secondBestVulture) {
        int p = Commons.rouletteWheelSelection(new double[]{alpha, beta});
        return p > 0 ? firstBestVulture : secondBestVulture;
    }

    private Vulture[] findBestVultures(){

        sort();

        return new Vulture[]{(Vulture) getFirstAgents().get(0), (Vulture) getFirstAgents().get(1)};
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
            getFirstAgents().add(vulture);
        }
    }
}
