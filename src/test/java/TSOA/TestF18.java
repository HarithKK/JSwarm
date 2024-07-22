package TSOA;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.usa.soc.si.Algorithm;
import org.usa.soc.si.ObjectiveFunction;
import examples.si.benchmarks.DynamicCompositeBenchmarkFunctions.Function18;
import examples.si.AlgorithmFactory;
import utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static utils.Utils.calcStd;

/**
 * Targeted algorithms
 *  MFA
 *  PSO
 *  ALSO
 *  ABC
 *  CSO
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestF18 {
    int n = 100;
    int p = 30;
    int steps = 1000;

    String filepath;

    String testName;

    public ObjectiveFunction getFunction(){
        return new Function18(n);
    }

    List<Double> tsoa = new ArrayList<>();
    List<Double> ssa = new ArrayList<>();
    List<Double> mfa = new ArrayList<>();
    List<Double> cso = new ArrayList<>();
    List<Double> pso = new ArrayList<>();
    List<Double> also = new ArrayList<>();
    List<Double> ba = new ArrayList<>();
    List<Double> avoa = new ArrayList<>();
    List<Double> tsa = new ArrayList<>();
    List<Double> gwo = new ArrayList<>();

    @AfterAll
    public void printToFile(){

        Utils.writeToFile("data/result_mean.csv",
                testName + ","
                        + tsoa.stream().mapToDouble(d -> (Double)d).average().getAsDouble() + ","
                        + ssa.stream().mapToDouble(d -> (Double)d).average().getAsDouble() + ","
                        + mfa.stream().mapToDouble(d -> (Double)d).average().getAsDouble() + ","
                        + cso.stream().mapToDouble(d -> (Double)d).average().getAsDouble() + ","
                        + pso.stream().mapToDouble(d -> (Double)d).average().getAsDouble() + ","
                        + also.stream().mapToDouble(d -> (Double)d).average().getAsDouble() + ","
                        + ba.stream().mapToDouble(d -> (Double)d).average().getAsDouble() + ","
                        + avoa.stream().mapToDouble(d -> (Double)d).average().getAsDouble() + ","
                        + tsa.stream().mapToDouble(d -> (Double)d).average().getAsDouble() + ","
                        + gwo.stream().mapToDouble(d -> (Double)d).average().getAsDouble() + "\n"
        );
        Utils.writeToFile("data/result_std.csv",
                testName + ","
                        + calcStd(tsoa) + ","
                        + calcStd(ssa) + ","
                        + calcStd(mfa) + ","
                        + calcStd(cso) + ","
                        + calcStd(pso) + ","
                        + calcStd(also) + ","
                        + calcStd(ba) + ","
                        + calcStd(avoa) + ","
                        + calcStd(tsa) + ","
                        + calcStd(gwo) + "\n"
        );
        Utils.writeToFile("data/result_min.csv",
                testName + ","
                        + tsoa.stream().mapToDouble(d -> (Double)d).min().getAsDouble() + ","
                        + ssa.stream().mapToDouble(d -> (Double)d).min().getAsDouble() + ","
                        + mfa.stream().mapToDouble(d -> (Double)d).min().getAsDouble() + ","
                        + cso.stream().mapToDouble(d -> (Double)d).min().getAsDouble() + ","
                        + pso.stream().mapToDouble(d -> (Double)d).min().getAsDouble() + ","
                        + also.stream().mapToDouble(d -> (Double)d).min().getAsDouble() + ","
                        + ba.stream().mapToDouble(d -> (Double)d).min().getAsDouble() + ","
                        + avoa.stream().mapToDouble(d -> (Double)d).min().getAsDouble() + ","
                        + tsa.stream().mapToDouble(d -> (Double)d).min().getAsDouble() + ","
                        + gwo.stream().mapToDouble(d -> (Double)d).min().getAsDouble() + "\n"
        );
        Utils.writeToFile("data/result_max.csv",
                testName + ","
                        + tsoa.stream().mapToDouble(d -> (Double)d).max().getAsDouble() + ","
                        + ssa.stream().mapToDouble(d -> (Double)d).max().getAsDouble() + ","
                        + mfa.stream().mapToDouble(d -> (Double)d).max().getAsDouble() + ","
                        + cso.stream().mapToDouble(d -> (Double)d).max().getAsDouble() + ","
                        + pso.stream().mapToDouble(d -> (Double)d).max().getAsDouble() + ","
                        + also.stream().mapToDouble(d -> (Double)d).max().getAsDouble() + ","
                        + ba.stream().mapToDouble(d -> (Double)d).max().getAsDouble() + ","
                        + avoa.stream().mapToDouble(d -> (Double)d).max().getAsDouble() + ","
                        + tsa.stream().mapToDouble(d -> (Double)d).max().getAsDouble() + ","
                        + gwo.stream().mapToDouble(d -> (Double)d).max().getAsDouble() + "\n"
        );
    }

    @BeforeAll
    public void setPath(){
        this.filepath = "data/result_"+ this.getClass().getSimpleName()+".csv";
        this.testName = this.getClass().getSimpleName();
    }

    @RepeatedTest(10)
    public void testTSOA() {
        try {
            Algorithm algo = new AlgorithmFactory(29, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            tsoa.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testSSA() {
        try {
            Algorithm algo = new AlgorithmFactory(19, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            ssa.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," +testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testMFA() {
        try {
            Algorithm algo = new AlgorithmFactory(13, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            mfa.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testCSO() {
        try {
            Algorithm algo = new AlgorithmFactory(2, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            cso.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testPSO() {
        try {
            Algorithm algo = new AlgorithmFactory(0, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            pso.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testALSO() {
        try {
            Algorithm algo = new AlgorithmFactory(15, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            also.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testBA() {
        try {
            Algorithm algo = new AlgorithmFactory(10, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            ba.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @RepeatedTest(10)
    public void testAVOA() {
        try {
            Algorithm algo = new AlgorithmFactory(17, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            avoa.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testTSA() {
        try {
            Algorithm algo = new AlgorithmFactory(18, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            tsa.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testGWO() {
        try {
            Algorithm algo = new AlgorithmFactory(12, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            gwo.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+ algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
