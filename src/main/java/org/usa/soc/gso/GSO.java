package org.usa.soc.gso;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.List;

public class GSO extends Algorithm {

    private int numberOfGlowWorms;

    private GlowWorm[] glowWorms;

    private double l0, r0, ldc, lac, nt, beta, rs, s;

    public GSO(
               ObjectiveFunction<Double> objectiveFunction,
               int numberOfDimensions,
               int stepsCount,
               int numberOfGlowWorms,
               double[] minBoundary,
               double[] maxBoundary,
               double initiallLuciferinContent,
               double initialSensingRadius,
               double luciferinDecayConstant,
               double luciferinEnhanceConstant,
               double nt,
               double rs,
               double beta,
               double s,
               boolean isLocalMinima){

        this.numberOfDimensions = numberOfDimensions;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.gBest = new Vector(numberOfDimensions).setMaxVector();
        this.isLocalMinima = isLocalMinima;
        this.numberOfGlowWorms = numberOfGlowWorms;
        this.l0 = initiallLuciferinContent;
        this.ldc = luciferinDecayConstant;
        this.lac = luciferinEnhanceConstant;
        this.r0 = initialSensingRadius;
        this.rs= rs;
        this.beta = beta;
        this.nt = nt;
        this.s = s;
        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.glowWorms = new GlowWorm[numberOfGlowWorms];

    }

    @Override
    public void runOptimizer(int time) throws Exception{

        if(!this.isInitialized()){
            throw new RuntimeException("Ants Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();
        for(float step = 0; step< getStepsCount(); step+=1){

            // movement phase
            for(int i=0; i< numberOfGlowWorms; i++){

                GlowWorm ithWarm = this.glowWorms[i];

                List<GlowWorm> nWarms = getNeighbourWarms(i);
                GlowWorm jthWarm = getJthGlowWorm(nWarms,ithWarm);
                if(jthWarm == null){
                    continue;
                }

                Vector diffVec = ithWarm.getPosition().operate(Vector.OPERATOR.SUB, jthWarm.getPosition());
                double eNormDistance = Mathamatics.getEuclideanNorm(diffVec);

                Vector positionDiff = diffVec.operate(Vector.OPERATOR.DIV, eNormDistance).operate(Vector.OPERATOR.MULP, s);
                Vector newVR = ithWarm.getPosition().operate(Vector.OPERATOR.ADD, positionDiff);
                ithWarm.setPosition(newVR);
                ithWarm.setR(calculateNewR(ithWarm, nWarms.size()));
            }
            // update luciferin
            for(GlowWorm worm: this.glowWorms){
                worm.updateLuciferin(ldc, lac, objectiveFunction);
                this.updateGBest(worm);
            }
            if(this.stepAction != null)
                this.stepAction.performAction(this.gBest, this.getBestDoubleValue(), (int)step);
            stepCompleted(time, (int)step);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private GlowWorm getJthGlowWorm(List<GlowWorm> nWarms, GlowWorm ithWarm) {

        if(nWarms.size() ==0)
            return null;

        double totalFs = nWarms.stream().mapToDouble(f -> f.getL() - ithWarm.getL()).sum();
        double minFs = nWarms.stream().mapToDouble(f -> f.getL() - ithWarm.getL()).min().getAsDouble();
        double relFitness = minFs;
        double randP = Randoms.randAny(minFs, totalFs);

        for(GlowWorm w: nWarms){
            relFitness += (w.getL() - ithWarm.getL()) / totalFs;
            if(randP<relFitness){
                return w;
            }
        }
        return  null;
    }

    private void updateGBest(GlowWorm ithWarm) {
        Double fpbest = this.objectiveFunction.setParameters(ithWarm.getPosition().getPositionIndexes()).call();
        Double fgbest = this.objectiveFunction.setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isLocalMinima)){
            this.gBest.setVector(ithWarm.getPosition());
        }
    }

    private double calculateNewR(GlowWorm ithWarm, int N) {
        double d = ithWarm.getR() + (beta*(nt - N));
        return Math.min(rs, Math.max(0, d));
    }

    private List<GlowWorm> getNeighbourWarms(int i) {

        GlowWorm ithWarm = glowWorms[i];
        List<GlowWorm> w = new ArrayList<>();

        for(int j=0; j< numberOfGlowWorms; j++){
            if(i==j){
                continue;
            }
            GlowWorm jthWarm = glowWorms[j];
            double distance = ithWarm.getPosition().getDistance(jthWarm.getPosition());
            if(ithWarm.getL() < jthWarm.getL() && distance <  ithWarm.getR()){
                w.add(jthWarm);
            }
        }
        return w;

    }

    @Override
    public void initialize() {

        this.setInitialized(true);

        for(int i=0; i< numberOfGlowWorms; i++){
            GlowWorm g = new GlowWorm(this.l0, this.r0, this.numberOfDimensions, this.minBoundary, this.maxBoundary);
            this.updateGBest(g);
            this.glowWorms[i] = g;
        }
    }

    @Override
    public double[][] getDataPoints(){
        double[][] data = new double[this.numberOfDimensions][this.numberOfGlowWorms];
        for(int i=0; i< this.numberOfGlowWorms; i++){
            for(int j=0; j< numberOfDimensions; j++){
                data[j][i] = Mathamatics.round(this.glowWorms[i].getPosition().getValue(j),2);
            }
        }
        return data;
    };
}
