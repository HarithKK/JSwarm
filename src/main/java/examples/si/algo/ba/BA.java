package examples.si.algo.ba;

import org.usa.soc.core.action.Method;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.Random;

public class BA extends SIAlgorithm {

    private int numberOfBats;

    private double fMin, fMax, alpha, A0, r0, gamma, Aavg;

    public BA(ObjectiveFunction<Double> objectiveFunction,
              int stepsCount,
              int numberOfDimensions,
              double[] minBoundary,
              double[] maxBoundary,
              int numberOfBats,
              double fMin,
              double fMax,
              double alpha,
              double gamma,
              double A0,
              double r0,
              boolean isGlobalMinima){
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.numberOfBats = numberOfBats;
        this.fMin = fMin;
        this.fMax = fMax;
        this.alpha = alpha;
        this.gamma = gamma;
        this.A0 = A0;
        this.r0 = r0; // pulse rate

        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        setFirstAgents("Bats", new ArrayList<>(numberOfBats));
    }
    @Override
    public void step() throws Exception {

            this.A0 *= this.alpha;
            this.r0 = this.r0 * (1 - Math.exp(-this.gamma * currentStep));

            for(int i=0; i< numberOfBats; i++){
                Bat b = (Bat)getFirstAgents().get(i);
                Vector frequency = Randoms.getRandomVector(numberOfDimensions, fMin, fMax);
                Vector vel = b.getPosition().getClonedVector()
                        .operate(Vector.OPERATOR.SUB, getGBest().getClonedVector())
                        .operate(Vector.OPERATOR.MULP, frequency);
                b.getVelocity().updateVector(vel);

                Vector solution;
                if(Randoms.rand(0,1) > r0){
                    solution = new Vector(numberOfDimensions);
                    solution.setValues(new Method() {
                        @Override
                        public double execute() {
                            return new Random().nextGaussian();
                        }
                    });

                    solution = solution.operate(Vector.OPERATOR.MULP, A0 * 0.1)
                            .operate(Vector.OPERATOR.ADD, gBest.getClonedVector());
                }else{
                    solution = b.getPosition().getClonedVector().operate(Vector.OPERATOR.ADD, b.getVelocity().getClonedVector());
                }

                if(objectiveFunction.setParameters(solution.getPositionIndexes()).call() < b.fitnessValue){
                    getFirstAgents().get(i).setPosition(solution.getClonedVector());
                    ((Bat)getFirstAgents().get(i)).calcFitnessValue(getObjectiveFunction());
                }

                if(b.getFitnessValue() < getBestDoubleValue()){
                    this.gBest.setVector(solution.fixVector(minBoundary, maxBoundary));
                }
            }
    }

    @Override
    public void initialize() {
        this.setInitialized(true);
        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);

        double at =1;
        for(int i=0; i< numberOfBats; i++){
            Bat b = new Bat(
                    minBoundary,
                    maxBoundary,
                    numberOfDimensions,
                    alpha,
                    A0,
                    gamma,
                    r0
            );
            b.calcFitnessValue(getObjectiveFunction());
            getFirstAgents().add(b);
        }
        this.gBest = getFirstAgents().get(0).getPosition().getClonedVector();
    }
}
