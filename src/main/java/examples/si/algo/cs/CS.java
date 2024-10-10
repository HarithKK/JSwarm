package examples.si.algo.cs;

import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class CS extends SIAlgorithm {

    private int numberOfNests;

    private double alpha, pa;

    public CS(
            ObjectiveFunction<Double> objectiveFunction,
            int stepsCount,
            int numberOfDimensions,
            int numberOfNests,
            double[] minBoundary,
            double[] maxBoundary,
            double alpha,
            double pa,
            boolean isGlobalMinima
    ){
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.numberOfNests = numberOfNests;
        this.alpha = alpha;
        this.pa = pa;

        setFirstAgents("Nests", new ArrayList<>(numberOfNests));
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
    }

    @Override
    public void step() throws Exception{

            for (int i=0; i< numberOfNests; i++) {
                // get cuckoo
                Vector cuckooPosition = Mathamatics.getLevyVector((int) currentStep, numberOfDimensions, 1, 3)
                        .operate(Vector.OPERATOR.MULP, this.alpha)
                        .operate(Vector.OPERATOR.ADD, getFirstAgents().get(i).getPosition())
                        .fixVector(minBoundary, maxBoundary);

                // select the nest randomly
                int j = Math.min( (int) Math.abs(Randoms.rand(0, 1) * numberOfNests + 1) -1, numberOfNests -1);

                Double fj = this.getObjectiveFunction().setParameters(getFirstAgents().get(j).getPosition().getPositionIndexes()).call();
                Double fi = this.getObjectiveFunction().setParameters(getFirstAgents().get(i).getPosition().getPositionIndexes()).call();

                if(Validator.validateBestValue(fi, fj, isGlobalMinima.isSet())){
                    getFirstAgents().get(j).setPosition(cuckooPosition);
                }

                if (Randoms.rand(0, 1) < pa) {
                    ReInitiateWorstNest();
                }

                updateGBest((Nest) getFirstAgents().get(i));
            }
    }

    private void updateGBest(Nest nest) {
        Double fpbest = this.getObjectiveFunction().setParameters(nest.getPosition().getPositionIndexes()).call();
        Double fgbest = this.getObjectiveFunction().setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())){
            this.gBest.setVector(nest.getPosition());
        }
    }

    private void ReInitiateWorstNest() {
        //find worst
        int i0 = 0;
        for(int i=1; i< numberOfNests; i++){
            Double fi = this.getObjectiveFunction().setParameters(getFirstAgents().get(i0).getPosition().getPositionIndexes()).call();
            Double fj = this.getObjectiveFunction().setParameters(getFirstAgents().get(i).getPosition().getPositionIndexes()).call();

            if(Validator.validateBestValue(fi, fj, isGlobalMinima.isSet())){
                i0 = i;
            }
        }
        getFirstAgents().get(i0).setPosition(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1));
    }

    @Override
    public void initialize() {
        this.setInitialized(true);

        if(!Validator.validateRangeInOneAndZero(pa)){
            throw new RuntimeException("Pa should be between 0 and 1");
        }

        for(int i=0; i< this.numberOfNests; i++){
            getFirstAgents().add(new Nest(minBoundary, maxBoundary, numberOfDimensions));
        }
    }
}
