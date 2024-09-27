package examples.si.algo.wso;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;

public class WSO extends SIAlgorithm {
    private int numberOfWasps;

    private double c1, c2;

    public WSO(ObjectiveFunction fn,
               int numberOfIterations,
               int numberOfWasps,
               int numberOfDimensions,
               double[] minBoundary,
               double[] maxBoundary,
               double c1,
               double c2,
               boolean isGlobalMinima) {
        this.objectiveFunction = fn;
        this.stepsCount = numberOfIterations;
        this.numberOfWasps = numberOfWasps;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.c1 = c1;
        this.c2 = c2;

        setFirstAgents("wasps", new ArrayList<>(numberOfWasps));
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
    }

    @Override
    public void step() throws Exception{

            // tournament
            double totalForce = Arrays.stream(getFirstAgents().toArray()).mapToDouble(f -> ((Wasp)f).getForce()).sum();
            double minForce = Arrays.stream(getFirstAgents().toArray()).mapToDouble(f -> ((Wasp)f).getForce()).min().getAsDouble();

            for(AbsAgent agent: getFirstAgents()){
                Wasp w = (Wasp) agent;
                double p0 = Randoms.randAny(minForce, totalForce);
                double p = w.getForce() / totalForce;
                if(p <= p0){
                    this.updateBest(w);
                }
                w.setSolution(Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary));
                w.updateDiversity(getObjectiveFunction(), isGlobalMinima.isSet());
                w.updateForce(this.c1, this.c2, this.getObjectiveFunction());
            }
    }

    @Override
    public long getNanoDuration() {
        return this.nanoDuration;
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        for(int i=0; i< this.numberOfWasps;i++){
            Wasp wasp = createNewRandomWasp();
            getFirstAgents().add(wasp);
        }
    }

    private Wasp createNewRandomWasp(){
        Wasp wasp = new Wasp(
                this.minBoundary,
                this.maxBoundary,
                this.numberOfDimensions);
        wasp.setSolution(Randoms.getRandomVector(this.numberOfDimensions, this.minBoundary, this.maxBoundary));
        wasp.updateDiversity(getObjectiveFunction(), isGlobalMinima.isSet());
        wasp.updateForce(this.c1, this.c2, this.getObjectiveFunction());
        this.updateBest(wasp);
        return wasp;
    }

    private void updateBest(Wasp w) {
        Double fgbest = getObjectiveFunction().setParameters(this.getGBest().getClonedVector().getPositionIndexes()).call();
        Double fpbest = getObjectiveFunction().setParameters(w.getBestSolution().getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())){
            this.gBest.setVector(w.getBestSolution().getClonedVector(), minBoundary, maxBoundary);
        }
    }

    @Override
    public SIAlgorithm clone() throws CloneNotSupportedException {
        return new WSO(getObjectiveFunction(),
                getStepsCount(),
                numberOfWasps,
                numberOfDimensions,
                minBoundary,
                maxBoundary,
                c1,
                c2,
                isGlobalMinima.isSet());
    }

}
