package utils;

import examples.si.AlgorithmFactory;
import examples.si.algo.tsoa.TSOA;
import examples.si.algo.tsoa.TSOA_V1;
import examples.si.benchmarks.DynamicMultiModalObjectiveFunctions.Function9;
import examples.si.benchmarks.DynamicUnimodalObjectiveFunctions.*;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.ColvilleFunction;
import examples.si.benchmarks.nonGeneral.classical.unimodal.separable.QuarticFunction;
import examples.si.benchmarks.singleObjective.SphereFunction;
import org.apache.commons.math3.stat.inference.TTest;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.usa.soc.core.action.Method;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;
import utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Adom_Test {



    int n = 30;
    int p = 30;
    int steps = 1000;

    ObjectiveFunction fn = new SphereFunction().updateDimensions(n);

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

    @RepeatedTest(10)
    public void testAVOA() {
        try {
            algo = new AlgorithmFactory(17, fn).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            System.out.println(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(1)
    public void testMFA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(13, fn).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            System.out.println(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(1)
    public void testSSA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(19, fn).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            System.out.println(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(1)
    public void testALSO() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(15, new QuarticFunction().updateDimensions(30)).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            System.out.println(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(1)
    public void testCSO() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(2, fn).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            System.out.println(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(1)
    public void testBA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(10, fn).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            System.out.println(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(1)
    public void testPSO() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(0, fn).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            System.out.println(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(1)
    public void testTSA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(18, fn).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            System.out.println(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RepeatedTest(1)
    public void testGWO() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(12, fn).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            System.out.println(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    List<Double> d = new ArrayList<>();
    TSOA algorithm;
    @AfterEach
    public void logC(){
        d.add(algorithm.getBestDoubleValue());
        for(double i : algorithm.z_history){
            System.out.print(i+",");
        }
        System.out.println("");
        for(double i : algorithm.getHistory()){
            System.out.print(i+",");
        }
        System.out.println("");
    }

    @AfterAll
    public void aall(){
        System.out.println("P Value "+Commons.calculatePValue(
                Commons.fill(algorithm.getObjectiveFunction().getExpectedBestValue(), d.size()),
                d.stream().mapToDouble(t->t).toArray()
        ));

    }

    @RepeatedTest(1)
    public void testTSOA() {
        try {
            algorithm = (TSOA)new AlgorithmFactory(29, new ColvilleFunction()).getAlgorithm(500, 30);
            algorithm.initialize();
            algorithm.run();

            System.out.println(algorithm.getBestDoubleValue());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPSO12() throws Exception {
        SIAlgorithm al = new AlgorithmFactory(0, new ColvilleFunction()).getAlgorithm(500, 30);
        al.initialize();
        al.run();

        System.out.println(al.getBestValue());
    }

    @Test
    public void a(){

        System.out.println(Math.sin(Math.PI/2));
    }
}
