package examples.si.algo.gso;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;
import java.util.List;

public class GSO extends SIAlgorithm {

    private int numberOfGlowWorms;

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
               boolean isGlobalMinima){

        this.numberOfDimensions = numberOfDimensions;
        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.gBest = new Vector(numberOfDimensions).setMaxVector();
        this.isGlobalMinima.setValue(isGlobalMinima);
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
        setFirstAgents("Worms", new ArrayList<>(numberOfGlowWorms));

    }

    @Override
    public void step() throws Exception{
            // movement phase
            for(int i=0; i< numberOfGlowWorms; i++){

                GlowWorm ithWarm = (GlowWorm) getFirstAgents().get(i);

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
            for(AbsAgent agent: getFirstAgents()){
                GlowWorm worm = (GlowWorm)agent;
                worm.updateLuciferin(ldc, lac, objectiveFunction);
                this.updateGBest(worm);
            }
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

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())){
            this.gBest.setVector(ithWarm.getPosition());
        }
    }

    private double calculateNewR(GlowWorm ithWarm, int N) {
        double d = ithWarm.getR() + (beta*(nt - N));
        return Math.min(rs, Math.max(0, d));
    }

    private List<GlowWorm> getNeighbourWarms(int i) {

        GlowWorm ithWarm = (GlowWorm) getFirstAgents().get(i);
        List<GlowWorm> w = new ArrayList<>();

        for(int j=0; j< numberOfGlowWorms; j++){
            if(i==j){
                continue;
            }
            GlowWorm jthWarm = (GlowWorm) getFirstAgents().get(j);
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
            getFirstAgents().add( g);
        }
    }

}
