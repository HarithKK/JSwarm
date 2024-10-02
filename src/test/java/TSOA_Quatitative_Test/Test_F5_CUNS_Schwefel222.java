package TSOA_Quatitative_Test;


import examples.si.AlgorithmFactory;
import examples.si.benchmarks.DynamicUnimodalObjectiveFunctions.Function2;
import org.junit.jupiter.api.*;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.si.SIAlgorithm;
import utils.ResultAggrigator;
import utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Targeted algorithms
 *  MFA
 *  PSO
 *  ALSO
 *  ABC
 *  CSO
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Test_F5_CUNS_Schwefel222 {
    int n = 30;
    int p = 30;
    int steps = 1000;
    String filepath;
    String testName;
    SIAlgorithm algo;
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

    public ObjectiveFunction getFunction() {
        return new Function2(n);
    }

    @BeforeAll
    public void setPath() {
        this.filepath = "data/result_" + this.getClass().getSimpleName() + ".csv";
        this.testName = this.getClass().getSimpleName();
    }

    @AfterEach
    public void writeToFile() {
        Utils.writeToFile(filepath, new Date().toString() + "," + algo.getName() + "," + testName + "," + algo.getBestDoubleValue() + "," + algo.getNanoDuration() + "\n");
    }

    @AfterEach
    public void writeToMongo(TestInfo ir, RepetitionInfo i) {
        ResultAggrigator.getInstance().updateTestResuly(algo, testName, i.getCurrentRepetition());
    }

    @AfterAll
    public void resultUpdate() {
        ResultAggrigator.getInstance().aggrigateInfo(testName,
                new String[]{"tsoa", "ssa", "mfa", "cso", "pso", "also", "ba", "avoa", "tsa", "gwo"},
                new List[]{
                        tsoa, ssa, mfa, cso, pso, also, ba, avoa, tsa, gwo
                }, algo);
    }

    @RepeatedTest(value = ResultAggrigator.REP_COUNT)
    public void testTSOA() {
        try {
            algo = new AlgorithmFactory(29, getFunction()).getAlgorithm(ResultAggrigator.STEPS, ResultAggrigator.POPULATION);
            algo.initialize();
            algo.run();
            tsoa.add(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(value = ResultAggrigator.REP_COUNT)
    public void testSSA() {
        try {
            algo = new AlgorithmFactory(19, getFunction()).getAlgorithm(ResultAggrigator.STEPS, ResultAggrigator.POPULATION);
            algo.initialize();
            algo.run();
            ssa.add(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(value = ResultAggrigator.REP_COUNT)
    public void testMFA() {
        try {
            algo = new AlgorithmFactory(13, getFunction()).getAlgorithm(ResultAggrigator.STEPS, ResultAggrigator.POPULATION);
            algo.initialize();
            algo.run();
            mfa.add(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(value = ResultAggrigator.REP_COUNT)
    public void testCSO() {
        try {
            algo = new AlgorithmFactory(2, getFunction()).getAlgorithm(ResultAggrigator.STEPS, ResultAggrigator.POPULATION);
            algo.initialize();
            algo.run();
            cso.add(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(value = ResultAggrigator.REP_COUNT)
    public void testPSO() {
        try {
            algo = new AlgorithmFactory(0, getFunction()).getAlgorithm(ResultAggrigator.STEPS, ResultAggrigator.POPULATION);
            algo.initialize();
            algo.run();
            pso.add(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(value = ResultAggrigator.REP_COUNT)
    public void testALSO() {
        try {
            algo = new AlgorithmFactory(15, getFunction()).getAlgorithm(ResultAggrigator.STEPS, ResultAggrigator.POPULATION);
            algo.initialize();
            algo.run();
            also.add(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(value = ResultAggrigator.REP_COUNT)
    public void testBA() {
        try {
            algo = new AlgorithmFactory(10, getFunction()).getAlgorithm(ResultAggrigator.STEPS, ResultAggrigator.POPULATION);
            algo.initialize();
            algo.run();
            ba.add(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @RepeatedTest(value = ResultAggrigator.REP_COUNT)
    public void testAVOA() {
        try {
            algo = new AlgorithmFactory(17, getFunction()).getAlgorithm(ResultAggrigator.STEPS, ResultAggrigator.POPULATION);
            algo.initialize();
            algo.run();
            avoa.add(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(value = ResultAggrigator.REP_COUNT)
    public void testTSA() {
        try {
            algo = new AlgorithmFactory(18, getFunction()).getAlgorithm(ResultAggrigator.STEPS, ResultAggrigator.POPULATION);
            algo.initialize();
            algo.run();
            tsa.add(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(value = ResultAggrigator.REP_COUNT)
    public void testGWO() {
        try {
            algo = new AlgorithmFactory(12, getFunction()).getAlgorithm(ResultAggrigator.STEPS, ResultAggrigator.POPULATION);
            algo.initialize();
            algo.run();
            gwo.add(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
