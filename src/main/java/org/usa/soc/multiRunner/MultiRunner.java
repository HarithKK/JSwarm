package org.usa.soc.multiRunner;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.IAlgorithm;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Validator;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiRunner implements IAlgorithm {

    private IAlgorithm algorithm;
    private int numberOfRunners;

    private double minimumBestValue;
    private double maximumBestValue;

    private Vector gbest;
    private Double gbestValue;

    private boolean isInitialized = false;

    private ExecutorService executorService;

    private Queue<IAlgorithm> completedAlgos;

    private CountDownLatch countDownLatch;

    public MultiRunner(IAlgorithm algorithm, int numberOfRunners) {
        this.algorithm = algorithm;
        this.numberOfRunners = numberOfRunners;
    }

    @Override
    public void runOptimizer() {
        if(!this.isInitialized){
            throw new RuntimeException("Particles Are Not Initialized");
        }
        try {
            for(int i = 0; i < this.numberOfRunners; i++){
                IAlgorithm algo = this.algorithm.clone();
                algo.initialize();
                algo.runOptimizer();
                this.executorService.submit(()->{
                    algo.initialize();
                    algo.runOptimizer();
                    this.completedAlgos.add(algo);
                    this.countDownLatch.countDown();
                });
            }
            countDownLatch.await();
            finalizeAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void finalizeAll() {

        while(!this.completedAlgos.isEmpty()){
            IAlgorithm a = this.completedAlgos.poll();
            if(Validator.validateBestValue(a.getBestDValue(), this.gbestValue, this.isMinima())){
                this.gbestValue = a.getBestDValue();
                this.gbest = a.getBestVector();
                this.setMinimumBestValue(Math.min(a.getBestDValue(), this.getMinimumBestValue()));
                this.setMaximumBestValue(Math.max(a.getBestDValue(), this.getMaximumBestValue()));
                this.algorithm = a;
            }
        }
    }

    @Override
    public long getNanoDuration() {
        return 0;
    }

    @Override
    public void initialize() {
        this.isInitialized = true;
        this.executorService = Executors.newFixedThreadPool(this.numberOfRunners);
        this.setMinimumBestValue(Double.MAX_VALUE);
        this.setMaximumBestValue(Double.MIN_VALUE);
        this.gbest = new Vector(this.algorithm.getFunction().getNumberOfDimensions());
        this.gbestValue = this.algorithm.isMinima() ? Double.MAX_VALUE : Double.MAX_VALUE;
        this.completedAlgos = new LinkedList<>();
        this.countDownLatch = new CountDownLatch(this.numberOfRunners);
    }

    @Override
    public String getBestValue() {
        return String.valueOf(this.gbestValue);
    }

    @Override
    public Double getBestDValue() {
        return this.gbestValue;
    }

    @Override
    public Vector getBestVector() {
        return this.gbest;
    }

    @Override
    public ObjectiveFunction getFunction() {
        return this.algorithm.getFunction();
    }

    @Override
    public String getBestVariables() {
        return this.algorithm.getBestVariables();
    }

    @Override
    public IAlgorithm clone() throws CloneNotSupportedException {
        return (IAlgorithm) super.clone();
    }

    @Override
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
}
