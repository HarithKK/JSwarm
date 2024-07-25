package examples.si.algo.ms;

import org.apache.commons.math3.analysis.function.Abs;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.si.Agent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MS extends SIAlgorithm {
    private final int numberOfMonkeys;
    private final int maxHeightOfTheTree;

    private final double c1;

    public MS(
            ObjectiveFunction fn,
            int numberOfIterations,
            int numberOfMonkeys,
            int numberOfDimensions,
            int maxHeightOfTheTree,
            double[] minBoundary,
            double[] maxBoundary,
            double c1,
            boolean isGlobalMinima) {
        this.objectiveFunction = fn;
        this.stepsCount = numberOfIterations;
        this.numberOfMonkeys = numberOfMonkeys;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.maxHeightOfTheTree = maxHeightOfTheTree;
        this.c1 = c1;

        setFirstAgents("Monkeys", new ArrayList<>(this.numberOfMonkeys));
        this.gBest = Randoms.getRandomVector(this.numberOfDimensions, this.minBoundary, this.maxBoundary);
    }

    @Override
    public void step() throws Exception{
            for (AbsAgent agent : getAgents("Monkeys").getAgents()) {
                Monky m = (Monky)agent;
                m.climbTree(this.c1,this.isGlobalMinima.isSet(), this.gBest);
                updateGBest(m);
            }
    }

    @Override
    public long getNanoDuration() {
        return this.nanoDuration;
    }

    @Override
    public void initialize() {
        this.setInitialized(true);

        for (int i = 0; i < this.numberOfMonkeys; i++) {
            Monky m = new Monky(
                    this.maxBoundary,
                    this.minBoundary,
                    this.numberOfDimensions,
                    this.maxHeightOfTheTree,
                    this.objectiveFunction
            );
            getAgents("Monkeys").getAgents().add(m);
            this.updateGBest(m);
        }
    }

    private void updateGBest(Monky m) {

        Double fpbest = this.objectiveFunction.setParameters(m.getBestRoot().getClonedVector().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getClonedVector().getPositionIndexes()).call();
        if (Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())) {
            this.gBest.setVector(m.getBestRoot(), this.minBoundary, this.maxBoundary);
        }
    }

    @Override
    public SIAlgorithm clone() throws CloneNotSupportedException {
        return new MS(objectiveFunction,
                getStepsCount(),
                numberOfMonkeys,
                numberOfDimensions,
                maxHeightOfTheTree,
                minBoundary,
                maxBoundary,
                c1,
                isGlobalMinima.isSet()
        );
    }
}
