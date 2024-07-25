package examples.si.algo.jso;

import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class JSO extends SIAlgorithm {

    private int populationSize;

    private double beta, gamma;

    public JSO(
            ObjectiveFunction objectiveFunction,
            int populationSize,
            int numberOfIterations,
            int numberOfDimensions,
            double[] minBoundary,
            double[] maxBoundary,
            boolean isGlobalMinima,
            double beta,
            double gamma
    ) {

        this.objectiveFunction = objectiveFunction;
        this.populationSize = populationSize;
        this.stepsCount = numberOfIterations;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.beta = beta;
        this.gamma = gamma;

        setFirstAgents("Jellyfish", new ArrayList<>(populationSize));
    }

    @Override
    public void step() throws Exception{
        if(!this.isInitialized()){
            throw new RuntimeException("Jellyfishes Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        try{
            for(int step = 0; step< getStepsCount(); step++){

                for(Jellyfish jellyfish: (Jellyfish[]) getFirstAgents().toArray()){

                    double ct = Math.abs((1 - ((step+1)/stepsCount)) * (2*Randoms.rand(0,1)-1));
                    Vector mu = getMeanLocation();
                    Vector newX;
                    if(ct >= 0.5){
                        Vector v1 = mu
                                .operate(Vector.OPERATOR.MULP, beta)
                                .operate(Vector.OPERATOR.MULP, Randoms.rand(0,1));
                        Vector trend = this.gBest.getClonedVector()
                                .operate(Vector.OPERATOR.SUB, v1);
                        newX = jellyfish.getPosition()
                                .operate(Vector.OPERATOR.ADD, trend.operate(Vector.OPERATOR.MULP, Randoms.rand(0,1)));
                    }else if(Randoms.rand(0,1) >(1-ct)){
                        newX = getMotionVector()
                               .operate(Vector.OPERATOR.MULP,Randoms.rand(0,1))
                               .operate(Vector.OPERATOR.MULP, gamma)
                               .operate(Vector.OPERATOR.ADD, jellyfish.getPosition());
                    }else{
                        Jellyfish jthJellyfish = getRandomJellyfish(jellyfish);
                        Vector direction = jthJellyfish.getFitnessValue() > jellyfish.getFitnessValue()
                                ?jellyfish.getPosition().operate(Vector.OPERATOR.SUB, jthJellyfish.getPosition())
                                :jthJellyfish.getPosition().operate(Vector.OPERATOR.SUB, jellyfish.getPosition());
                         newX = direction.operate(Vector.OPERATOR.MULP, Randoms.rand(0,1))
                                        .operate(Vector.OPERATOR.ADD, jellyfish.getPosition());
                    }
                    jellyfish.setPosition(newX.fixVector(minBoundary, maxBoundary));
                    jellyfish.setFitnessValue(objectiveFunction.setParameters(jellyfish.getPosition().getPositionIndexes()).call());
                    updateGbest(jellyfish);
                }

                if(this.stepAction != null)
                    this.stepAction.performAction(this.gBest.getClonedVector(), this.getBestDoubleValue(), step);
                stepCompleted(step);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private Jellyfish getRandomJellyfish(Jellyfish jellyfish) {
        Jellyfish f = (Jellyfish) getFirstAgents().get(Randoms.rand(populationSize-1));
        if(f == jellyfish){
            return (Jellyfish) getFirstAgents().get(Randoms.rand(populationSize-1));
        }else{
            return f;
        }
    }

    private Vector getMotionVector() {
        Vector vector = new Vector(numberOfDimensions).resetAllValues(0.0);
        for(int i=0; i< numberOfDimensions;i++)
            vector.setValue(maxBoundary[i]-minBoundary[i],i);
        return vector;
    }

    private Vector getMeanLocation() {
        Vector sum = new Vector(numberOfDimensions).resetAllValues(0.0);
        for(Jellyfish jellyfish: (Jellyfish[]) getFirstAgents().toArray()){
            sum.operate(Vector.OPERATOR.ADD, jellyfish.getPosition());
        }
        return sum.operate(Vector.OPERATOR.DIV, (double)populationSize);
    }

    @Override
    public void initialize() {

        this.setInitialized(true);

        for(int i=0;i <populationSize; i++){
            Jellyfish jellyfish = new Jellyfish(numberOfDimensions, minBoundary, maxBoundary);
            jellyfish.setFitnessValue(objectiveFunction.setParameters(jellyfish.getPosition().getPositionIndexes()).call());

            getFirstAgents().set(i,jellyfish);
        }

        for(Jellyfish jellyfish: (Jellyfish[]) getFirstAgents().toArray()){
            updateGbest(jellyfish);
        }
    }

    private void updateGbest(Jellyfish jellyfish){
        double fgbest = objectiveFunction.setParameters(gBest.getClonedVector().getPositionIndexes()).call();
        if(Validator.validateBestValue(jellyfish.getFitnessValue(), fgbest, isGlobalMinima.isSet())){
            this.gBest.setVector(jellyfish.getPosition());
        }
    }
}
