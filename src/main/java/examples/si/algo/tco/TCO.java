package examples.si.algo.tco;

import org.usa.soc.core.AbsAgent;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

import java.util.ArrayList;

public class TCO extends SIAlgorithm {

    private int numberOfTermites;

    private double p0, eRate, r, omega;

    Vector tau ,tauDecrements;

    public TCO(ObjectiveFunction<Double> objectiveFunction,
               int stepsCount,
               int numberOfDimensions,
               int numberOfTermites,
               double[] minBoundary,
               double[] maxBoundary,
               double p0,
               double evaporationRate,
               double r,
               double omega,
               boolean isGlobalMinima){

        this.objectiveFunction = objectiveFunction;
        this.stepsCount = stepsCount;
        this.numberOfDimensions = numberOfDimensions;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.isGlobalMinima.setValue(isGlobalMinima);
        this.numberOfTermites = numberOfTermites;
        this.p0 = p0;
        this.eRate = evaporationRate;
        this.r = r;
        this.omega = omega;

        this.gBest = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        setFirstAgents("termites", new ArrayList<>(numberOfTermites));

    }

    @Override
    public void step() throws Exception{

            for (AbsAgent agent: getFirstAgents()) {
                ((Termite)agent).updatePheramoneValue(eRate, getObjectiveFunction());
            }

            for(int i=0 ;i< numberOfTermites; i++){
                Termite ti = (Termite) getFirstAgents().get(i);
                Termite tb = getClosestTermite(i,ti.getPosition(), tau.getMagnitude());

                if(tb == null){
                    ti.updatePositionByRandomWalk(tau);
                }
                else{
                    if(ti.getpValue() < tb.getpValue()){
                        updateGBest(tb);
                        ti.updatePosition(omega, tb.getPosition());
                    }else{
                        // Our Work
                        ti.updatePositionByRandomWalk(tau);
                    }
                }
            }
    }

    private void updateGBest(Termite ti) {
        Double fpbest = this.getObjectiveFunction().setParameters(ti.getPosition().getPositionIndexes()).call();
        Double fgbest = this.getObjectiveFunction().setParameters(this.gBest.getPositionIndexes()).call();

        if(Validator.validateBestValue(fpbest, fgbest, isGlobalMinima.isSet())){
            this.gBest.setVector(ti.getPosition());
        }
    }

    private Termite getClosestTermite(int j, Vector position, double c) {
        double diff = Double.MAX_VALUE;
        Termite t = null;
        for(int i=0 ;i< numberOfTermites; i++){
            if(i==j)
                continue;
            Termite tj = (Termite) getFirstAgents().get(i);
            double d = position.getDistance(tj.getPosition());
            if(d < c && d < diff){
                diff = d;
                t = tj;
            }
        }
        return t;
    }

    @Override
    public void initialize() {
        setInitialized(true);

        for(int i=0 ;i< numberOfTermites; i++){
            getFirstAgents().add(new Termite(minBoundary, maxBoundary, numberOfDimensions, p0));
        }

        tau = new Vector(numberOfDimensions);
        tauDecrements = new Vector(numberOfDimensions);

        for(int i=0;i<numberOfDimensions;i++){
            double diff = Math.abs(getObjectiveFunction().getMax()[i] - getObjectiveFunction().getMin()[i]);
            double max = diff/2;
            double min = diff/8;

            tau.setValue(max, i);
            tauDecrements.setValue((max - min)/stepsCount, i);
        }
    }
}
