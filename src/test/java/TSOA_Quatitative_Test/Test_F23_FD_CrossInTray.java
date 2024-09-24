package TSOA_Quatitative_Test;


import examples.si.AlgorithmFactory;
import examples.si.benchmarks.FixMultiModalObjectiveFunctions.Function22;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.si.SIAlgorithm;
import utils.Utils;

import java.sql.SQLException;
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
public class Test_F23_FD_CrossInTray {
    int n = 30;
    int p = 60;
    int steps = 1000;

    String filepath;

    String testName;

    public ObjectiveFunction getFunction(){
        return new Function22();
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

        try {
            DB db = new DB();
            db.connect();
            db.addFinal("TSOA", testName,
                    tsoa.stream().mapToDouble(d -> (Double)d).average().getAsDouble(),
                    tsoa.stream().mapToDouble(d -> (Double)d).max().getAsDouble(),
                    tsoa.stream().mapToDouble(d -> (Double)d).min().getAsDouble(),
                    calcStd(tsoa), 10);
            db.addFinal("SSA", testName,
                    ssa.stream().mapToDouble(d -> (Double)d).average().getAsDouble(),
                    ssa.stream().mapToDouble(d -> (Double)d).max().getAsDouble(),
                    ssa.stream().mapToDouble(d -> (Double)d).min().getAsDouble(),
                    calcStd(ssa), 10);
            db.addFinal("MFA", testName,
                    mfa.stream().mapToDouble(d -> (Double)d).average().getAsDouble(),
                    mfa.stream().mapToDouble(d -> (Double)d).max().getAsDouble(),
                    mfa.stream().mapToDouble(d -> (Double)d).min().getAsDouble(),
                    calcStd(mfa), 10);
            db.addFinal("CSO", testName,
                    cso.stream().mapToDouble(d -> (Double)d).average().getAsDouble(),
                    cso.stream().mapToDouble(d -> (Double)d).max().getAsDouble(),
                    cso.stream().mapToDouble(d -> (Double)d).min().getAsDouble(),
                    calcStd(cso), 10);
            db.addFinal("PSO", testName,
                    pso.stream().mapToDouble(d -> (Double)d).average().getAsDouble(),
                    pso.stream().mapToDouble(d -> (Double)d).max().getAsDouble(),
                    pso.stream().mapToDouble(d -> (Double)d).min().getAsDouble(),
                    calcStd(pso), 10);
            db.addFinal("ALSO", testName,
                    also.stream().mapToDouble(d -> (Double)d).average().getAsDouble(),
                    also.stream().mapToDouble(d -> (Double)d).max().getAsDouble(),
                    also.stream().mapToDouble(d -> (Double)d).min().getAsDouble(),
                    calcStd(also), 10);
            db.addFinal("BA", testName,
                    ba.stream().mapToDouble(d -> (Double)d).average().getAsDouble(),
                    ba.stream().mapToDouble(d -> (Double)d).max().getAsDouble(),
                    ba.stream().mapToDouble(d -> (Double)d).min().getAsDouble(),
                    calcStd(ba), 10);
            db.addFinal("AVOA", testName,
                    avoa.stream().mapToDouble(d -> (Double)d).average().getAsDouble(),
                    avoa.stream().mapToDouble(d -> (Double)d).max().getAsDouble(),
                    avoa.stream().mapToDouble(d -> (Double)d).min().getAsDouble(),
                    calcStd(avoa), 10);
            db.addFinal("TSA", testName,
                    tsa.stream().mapToDouble(d -> (Double)d).average().getAsDouble(),
                    tsa.stream().mapToDouble(d -> (Double)d).max().getAsDouble(),
                    tsa.stream().mapToDouble(d -> (Double)d).min().getAsDouble(),
                    calcStd(tsa), 10);
            db.addFinal("GWO", testName,
                    gwo.stream().mapToDouble(d -> (Double)d).average().getAsDouble(),
                    gwo.stream().mapToDouble(d -> (Double)d).max().getAsDouble(),
                    gwo.stream().mapToDouble(d -> (Double)d).min().getAsDouble(),
                    calcStd(gwo), 10);
            db.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public void setPath(){
        this.filepath = "data/result_"+ this.getClass().getSimpleName()+".csv";
        this.testName = this.getClass().getSimpleName();
    }
    @RepeatedTest(10)
    public void testTSOA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(29, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            tsoa.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testSSA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(19, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            ssa.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," +testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testMFA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(13, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            mfa.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testCSO() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(2, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            cso.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testPSO() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(0, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            pso.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testALSO() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(15, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            also.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testBA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(10, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            ba.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @RepeatedTest(10)
    public void testAVOA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(17, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            avoa.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testTSA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(18, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            tsa.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(10)
    public void testGWO() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(12, getFunction()).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            gwo.add(algo.getBestDoubleValue());
            Utils.writeToFile(filepath, new Date().toString() +","+ algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
