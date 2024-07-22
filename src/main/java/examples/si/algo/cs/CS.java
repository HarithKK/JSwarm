package examples.si.algo.cs;

import org.usa.soc.si.Algorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class CS extends Algorithm {

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

        agents = new ArrayList<>(numberOfNests);
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
    }

    @Override
    public void runOptimizer() throws Exception{
        if(!this.isInitialized()){
            throw new RuntimeException("Nests Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();
        for(int step = 0; step< getStepsCount(); step++){

            for (int i=0; i< numberOfNests; i++) {
                // get cuckoo
                Vector cuckooPosition = Mathamatics.getLevyVector(step, numberOfDimensions, 1, 3)
                        .operate(Vector.OPERATOR.MULP, this.alpha)
                        .operate(Vector.OPERATOR.ADD, agents.get(i).getPosition())
                        .fixVector(minBoundary, maxBoundary);

                // select the nest randomly
                int j = Math.min( (int) Math.abs(Randoms.rand(0, 1) * numberOfNests + 1) -1, numberOfNests -1);

                Double fj = this.objectiveFunction.setParameters(this.agents.get(j).getPosition().getPositionIndexes()).call();
                Double fi = this.objectiveFunction.setParameters(this.agents.get(i).getPosition().getPositionIndexes()).call();

                if(Validator.validateBestValue(fi, fj, isGlobalMinima.isSet())){
                    this.agents.get(j).setPosition(cuckooPosition);
                }

                if (Randoms.rand(0, 1) < pa) {
                    ReInitiateWorstNest();
                }

                updateGBest((Nest) agents.get(i));
            }
                if(this.stepAction != null)
                    this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), step);
                stepCompleted(step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private void updateGBest(Nest nest) {
        Double fpbest = this.objectiveFunction.setParameters(nest.getPosition().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())){
            this.gBest.setVector(nest.getPosition());
        }
    }

    private void ReInitiateWorstNest() {
        //find worst
        int i0 = 0;
        for(int i=1; i< numberOfNests; i++){
            Double fi = this.objectiveFunction.setParameters(agents.get(i0).getPosition().getPositionIndexes()).call();
            Double fj = this.objectiveFunction.setParameters(agents.get(i).getPosition().getPositionIndexes()).call();

            if(Validator.validateBestValue(fi, fj, isGlobalMinima.isSet())){
                i0 = i;
            }
        }
        agents.get(i0).setPosition(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary, 0, 1));
    }

    @Override
    public void initialize() {
        this.setInitialized(true);

        if(!Validator.validateRangeInOneAndZero(pa)){
            throw new RuntimeException("Pa should be between 0 and 1");
        }

        for(int i=0; i< this.numberOfNests; i++){
            agents.set(i, new Nest(minBoundary, maxBoundary, numberOfDimensions));
        }
    }
}
