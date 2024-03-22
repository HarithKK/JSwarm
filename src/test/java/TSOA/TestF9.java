package TSOA;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions.Function8;
import org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions.Function9;
import ui.AlgoStore;
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
public class TestF9 {

    int n = 100;
    //
    int p = 30;
    int steps = 1000;

    String filepath = "data/result_f9.csv";

    String testName = "F9";

    public ObjectiveFunction getFunction(){
        return new Function9(n);
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
    }

    @RepeatedTest(10)
    public void testTSOA() {
        try {
            Algorithm algo = new AlgoStore(29, getFunction()).getAlgorithm(steps, p);
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
            Algorithm algo = new AlgoStore(19, getFunction()).getAlgorithm(steps, p);
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
            Algorithm algo = new AlgoStore(13, getFunction()).getAlgorithm(steps, p);
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
            Algorithm algo = new AlgoStore(2, getFunction()).getAlgorithm(steps, p);
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
            Algorithm algo = new AlgoStore(0, getFunction()).getAlgorithm(steps, p);
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
            Algorithm algo = new AlgoStore(15, getFunction()).getAlgorithm(steps, p);
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
            Algorithm algo = new AlgoStore(10, getFunction()).getAlgorithm(steps, p);
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
            Algorithm algo = new AlgoStore(17, getFunction()).getAlgorithm(steps, p);
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
            Algorithm algo = new AlgoStore(18, getFunction()).getAlgorithm(steps, p);
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
            Algorithm algo = new AlgoStore(12, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            gwo.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+ algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
