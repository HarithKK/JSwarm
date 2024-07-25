package examples.si.algo.cso;

import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class CSO extends SIAlgorithm {

    private int numberOfCats;

    private double seekersToTracersRatio;

    private int smp;
    private double cdc, srd, c, w;
    private boolean spc;

    public CSO(
            ObjectiveFunction<Double> objectiveFunction,
            int numberOfDimensions,
            int stepsCount,
            int numberOfCats,
            double seekersToTracersRatio,
            double[] minBoundary,
            double[] maxBoundary,
            int smp,
            double cdc,
            double srd,
            boolean spc,
            double c,
            double w,
            boolean isGlobalMinima) {

        this.numberOfDimensions = numberOfDimensions;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.numberOfCats = numberOfCats;
        this.seekersToTracersRatio = seekersToTracersRatio;
        this.smp = smp;
        this.cdc = cdc;
        this.srd = srd;
        this.spc = spc;
        this.c = c;
        this.w =w;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        setFirstAgents("Cats", new ArrayList<>(numberOfCats));
    }

    @Override
    public void step() throws Exception{

        if(!this.isInitialized()){
            throw new RuntimeException("Cats Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();

        for(int i = 0; i< this.getStepsCount(); i++){

            for (Cat cat: (Cat[]) getFirstAgents().toArray()) {
                if(cat.isSeeker()){
                    cat.seek(objectiveFunction, isMinima());
                }else{
                    cat.trace(c, w, gBest);
                }
                cat.updateMode();
                updateBestCat(cat);
            }
            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), i);
            stepCompleted(i);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    @Override
    public void initialize() {

        this.setInitialized(true);

        if(!Validator.validateRangeInOneAndZero(this.seekersToTracersRatio)){
            throw new RuntimeException("Seekers to Tracers should be in between 0 and 1");
        }

        if(!Validator.validateRangeInOneAndZero(this.cdc)){
            throw new RuntimeException("CDC should be in between 0 and 1");
        }

        int seekersCount = (int)(this.seekersToTracersRatio * this.numberOfCats);

        int i = 0;
        while(i< seekersCount){
            getFirstAgents().set(i, new Cat(i+1,this.minBoundary, this.maxBoundary, this.numberOfDimensions, Mode.SEEKER, this.smp, this.cdc, this.srd, this.spc));
            this.updateBestCat((Cat) getFirstAgents().get(i));
            i++;
        }
        while(i< this.numberOfCats){
            getFirstAgents().set(i, new Cat(i+1,this.minBoundary, this.maxBoundary, this.numberOfDimensions, Mode.TRACER, this.smp, this.cdc, this.srd, this.spc));
            this.updateBestCat((Cat) getFirstAgents().get(i));
            i++;
        }
    }

    private void updateBestCat(Cat cat) {

        Double fpbest = this.objectiveFunction.setParameters(cat.getPosition().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())){
            this.gBest.setVector(cat.getPosition().getClonedVector(), this.minBoundary, this.maxBoundary);
        }
    }
}
