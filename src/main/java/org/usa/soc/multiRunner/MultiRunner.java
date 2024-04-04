package org.usa.soc.multiRunner;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Validator;

import java.util.LinkedList;
import java.util.Queue;

public class MultiRunner {

    private Algorithm algorithm;
    private int numberOfRunners;

    private double minimumBestValue;
    private double maximumBestValue;

    private Vector gbest;
    private Double gbestValue;

    private boolean isInitialized = false;

    private Queue<Algorithm> completedAlgos;

    private long nanoDuration;

    public MultiRunner(Algorithm algorithm, int numberOfRunners) {
        this.algorithm = algorithm;
        this.numberOfRunners = numberOfRunners;
    }

    public void runOptimizer() {
        if(!this.isInitialized){
            throw new RuntimeException("Multi runner Are Not Initialized");
        }
        this.nanoDuration = System.nanoTime();
        try {
            for(int i = 0; i < this.numberOfRunners; i++){
                Algorithm algo = this.algorithm.clone();
                algo.initialize();
                try {
            algo.runOptimizer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
                this.completedAlgos.add(algo);
            }
            finalizeAll();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.nanoDuration = System.nanoTime() - this.nanoDuration;
    }

    private void finalizeAll() {

        while(!this.completedAlgos.isEmpty()){
            Algorithm a = this.completedAlgos.poll();
            if(Validator.validateBestValue(a.getBestDoubleValue(), this.gbestValue, this.isMinima())){
                this.gbestValue = a.getBestDoubleValue();
                this.gbest = a.getGBest();
                this.setMinimumBestValue(Math.min(a.getBestDoubleValue(), this.getMinimumBestValue()));
                this.setMaximumBestValue(Math.max(a.getBestDoubleValue(), this.getMaximumBestValue()));
                this.algorithm = a;
            }
        }
    }

    public void initialize() {
        this.isInitialized = true;
        this.setMinimumBestValue(Double.MAX_VALUE);
        this.setMaximumBestValue(Double.MIN_VALUE);
        this.gbestValue = this.algorithm.isMinima() ? Double.MAX_VALUE : Double.MIN_VALUE;
        this.completedAlgos = new LinkedList<>();
    }

    public String getBestStringValue() {
        return String.valueOf(this.gbestValue);
    }

    public Double getBestDoubleValue() {
        return this.gbestValue;
    }

    public Vector getGBest() {
        return this.gbest;
    }

    public ObjectiveFunction getFunction() {
        return this.algorithm.getFunction();
    }

    public String getBestVariables() {
        return this.algorithm.getBestVariables();
    }

    public boolean isMinima() {
        return this.algorithm.isMinima();
    }

    public double getMinimumBestValue() {
        return minimumBestValue;
    }

    public void setMinimumBestValue(double minimumBestValue) {
        this.minimumBestValue = minimumBestValue;
    }

    public double getMaximumBestValue() {
        return maximumBestValue;
    }

    public void setMaximumBestValue(double maximumBestValue) {
        this.maximumBestValue = maximumBestValue;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public long getNanoDuration() {
        return nanoDuration;
    }
}
