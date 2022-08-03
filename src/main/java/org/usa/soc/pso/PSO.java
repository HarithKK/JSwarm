package org.usa.soc.pso;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.intefaces.IAlgorithm;
import org.usa.soc.util.Logger;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class PSO implements IAlgorithm {

    private int particleCount;
    private int numberOfDimensions;
    private int stepsCount;
    private Double c1, c2, wMax, wMin;

    private boolean isLocalMinima;

    private double[] minBoundary, maxBoundary;
    private ObjectiveFunction<Double> objectiveFunction;

    private Particle[] particles;

    private Vector gBest;

    private long nanoDuration;

    private boolean isInitialized = false;

    public PSO(
            ObjectiveFunction<Double> objectiveFunction,
            int particleCount,
            int numberOfDimensions,
            int stepsCount,
            double c1,
            double c2,
            double w,
            double []minBoundary,
            double []maxBoundary,
            boolean isLocalMinima) {

        this.particleCount = particleCount;
        this.numberOfDimensions = numberOfDimensions;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.c1 = c1;
        this.c2 = c2;
        this.wMax = this.wMin = w;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.particles = new Particle[particleCount];
        this.gBest = new Vector(numberOfDimensions);
        this.isLocalMinima = isLocalMinima;
        this.getGBest().resetAllValues(isLocalMinima ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY);
    }

    public PSO(
            ObjectiveFunction<Double> objectiveFunction,
            int particleCount,
            int numberOfDimensions,
            int stepsCount,
            double c1,
            double c2,
            double wMax,
            double wMin,
            double []minBoundary,
            double []maxBoundary,
            boolean isLocalMinima) {

        this.particleCount = particleCount;
        this.numberOfDimensions = numberOfDimensions;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.c1 = c1;
        this.c2 = c2;
        this.wMax = wMax;
        this.wMin = wMin;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.particles = new Particle[particleCount];
        this.gBest = new Vector(numberOfDimensions);
        this.isLocalMinima = isLocalMinima;
        this.getGBest().resetAllValues(isLocalMinima ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY);
    }

    public void runOptimizer(){

        if(!this.isInitialized()){
            throw new RuntimeException("Particles Are Not Initialized");
        }

        this.nanoDuration = System.nanoTime();
        // run the steps
        double currentBestValue = objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();

        for(int step = 1; step <= this.stepsCount; step++){
            Double stepBestValue = objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();

            if(Validator.validateBestValue(stepBestValue, currentBestValue, isLocalMinima)){
                currentBestValue = stepBestValue;
            }

            // update positions
            for (Particle p: this.particles) {
                p.updatePbest(this.objectiveFunction, this.isLocalMinima);
                this.updateGBest(p.getPBest(), this.getGBest());
            }

            // update velocity factor
            for (Particle p: this.particles) {
                p.updateVelocityAndPosition(this.getGBest(), this.c1, this.c2, this.calculateW(wMax, wMin, step));
            }

        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private Double calculateW(Double wMax, Double wMin, int step) {
        if(wMax == wMin)
            return wMin;
        else{
            return wMax - step*((wMax - wMin)/this.stepsCount);
        }
    }

    private boolean isInitialized(){
        if(!this.isInitialized){
            this.isInitialized = true;
            return false;
        }
        return true;
    }

    public void initialize(){

        this.isInitialized();

        Validator.checkBoundaries(this.minBoundary, this.maxBoundary, this.numberOfDimensions);
        Validator.checkMinMax(wMax, wMin);

        // initialize particles
        for(int i=0;i<this.particleCount; i++){
            Particle p = new Particle(this.minBoundary, this.maxBoundary, this.numberOfDimensions);
            Vector vc = getRandomPosition(Randoms.getRandomVector(numberOfDimensions,this.minBoundary, this.maxBoundary));
            p.setPosition(vc);
            p.setPBest(vc);
            this.particles[i] = p;
            this.updateGBest(this.particles[i].getPBest(), this.getGBest());
        }
    }

    private Vector getRandomPosition(Vector v) {

        Double[] center = Mathamatics.getCenterPoint(this.numberOfDimensions, this.minBoundary, this.maxBoundary);

        if(objectiveFunction.setParameters(v.getPositionIndexes()).validateRange()){
            return v;
        }

        boolean[] isPlus = new boolean[numberOfDimensions];
        for(int i=0;i<numberOfDimensions;i++){
            isPlus[i] = v.getValue(i) < center[i];
        }

        while(!isTerminated(isPlus, v.getPositionIndexes(), center, numberOfDimensions)){

            for(int i=0;i<numberOfDimensions;i++){
                Double dt = v.getValue(i) + (isPlus[i] ? 0.1 : -0.1);
                v.setValue( dt , i);
                if(objectiveFunction.setParameters(v.getPositionIndexes()).validateRange()){
                    return v;
                }
            }
        }

        return v;
    }

    private boolean isTerminated(boolean[] isPlus, Double[] positionIndexes, Double[] center, int D) {
        boolean shouldRun = true;
        for(int i=0;i< D; i++){
            if(isPlus[i]){
                shouldRun = shouldRun && positionIndexes[i] < center[i];
            }else{
                shouldRun = shouldRun && positionIndexes[i] > center[i];
            }
        }
        return !shouldRun;
    }

    @Override
    public void setBoundaries(double[] minBoundary, double[] maxBoundary) {
        Validator.checkBoundaries(minBoundary, maxBoundary, this.numberOfDimensions);

        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;

        for(int i=0;i<this.particleCount; i++){
            this.particles[i].setBoundaries(minBoundary, maxBoundary);
        }
    }

    @Override
    public String getBestValue() {
        return String.valueOf(this.getGBestValue());
    }

    @Override
    public ObjectiveFunction getFunction() {
        return this.objectiveFunction;
    }

    @Override
    public String getBestVariables() {
        return this.gBest.toString();
    }

    @Override
    public String getErrorPercentage() {
        Double r = Math.abs(this.getGBestValue() - this.objectiveFunction.getExpectedBestValue());
        r /= this.objectiveFunction.getExpectedBestValue() + + 0.00000000001;
        r *= 100;
        return Math.abs(r) + " %";
    }

    private void updateGBest(Vector pBestPosition, Vector gBestPosition) {

        ObjectiveFunction tfn = this.objectiveFunction.setParameters(pBestPosition.getPositionIndexes());
        Double fpbest = tfn.call();
        Double fgbest = this.objectiveFunction.setParameters(gBestPosition.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.getGBest().setVector(getRandomPosition(pBestPosition));
        }
    }

    public Vector getGBest() {
        return gBest;
    }

    public Double getGBestValue() {
        return this.objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();
    }
    public Double getGBestValue(int round) {
        Double d = this.objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();
        return Mathamatics.round(d, round);
    }
    public Double getGBestAbsValue(int round) {
        Double d = this.objectiveFunction.setParameters(this.getGBest().getPositionIndexes()).call();
        return Mathamatics.absRound(d, round);
    }


    public long getNanoDuration() {
        return nanoDuration;
    }
}
