package org.usa.soc.gso;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.cso.Cat;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
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
        this.s =s ;

        this.glowWorms = new GlowWorm[numberOfGlowWorms];
        this.gBest = isLocalMinima ? new Vector(numberOfDimensions).setMaxVector() : new Vector(numberOfDimensions).setMinVector();
    }

    @Override
    public void runOptimizer() {

        if(!this.isInitialized()){
            throw new RuntimeException("Ants Are Not Initialized");
        }

        for(int step=0; step< stepsCount; step++){

            // update luciferin
            for(GlowWorm worm: this.glowWorms){
                worm.updateLuciferin(ldc, lac, objectiveFunction);
            }

            // movement phase
            for(int i=0; i< numberOfGlowWorms; i++){

                GlowWorm ithWarm = this.glowWorms[i];

                GlowWorm []nWarms = getNeighbourWarms(i);
                double totalP = Arrays.stream(nWarms).mapToDouble(f -> Math.abs(f.getL() - ithWarm.getL())).sum();
                double minP = 0;
                GlowWorm jthWarm = null;
                for(GlowWorm w: nWarms){
                    double p = Math.abs(w.getL() - ithWarm.getL()) / totalP;
                    if(p>minP){
                        minP = p;
                        jthWarm = w;
                    }
                }

                if(jthWarm == null){
                    continue;
                }

                double eNormDistance = Mathamatics.getEuclideanNorm(ithWarm.getPosition().operate(Vector.OPERATOR.SUB, jthWarm.getPosition()));

                Vector diffVec = jthWarm.getPosition().
                        operate(Vector.OPERATOR.SUB, ithWarm.getPosition()).
                        operate(Vector.OPERATOR.DIV, eNormDistance).
                        operate(Vector.OPERATOR.MULP, s);
                ithWarm.setPosition(ithWarm.getPosition().operate(Vector.OPERATOR.ADD, diffVec));
                ithWarm.setR(calculateNewR(ithWarm, nWarms.length));

                this.updateGBest(ithWarm);
            }

        }
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
        return Math.min(rs, Math.max(0, ithWarm.getR() + d));
    }

    private GlowWorm[] getNeighbourWarms(int i) {

        GlowWorm ithWarm = glowWorms[i];
        List<GlowWorm> w = new ArrayList<>();

        for(int j=0; j< numberOfGlowWorms; j++){
            if(i==j){
                continue;
            }
            GlowWorm jthWarm = glowWorms[i];

            if(ithWarm.getL() >= jthWarm.getL()){
                continue;
            }

            if(Math.abs(ithWarm.getPosition().getValue(0) - jthWarm.getPosition().getValue(0)) >= ithWarm.getR()){
                continue;
            }

            if(ithWarm.getPosition().getDistance(jthWarm.getPosition()) >= ithWarm.getR()){
                continue;
            }
            w.add(jthWarm);
        }
        return w.toArray(GlowWorm[]::new);

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
}
