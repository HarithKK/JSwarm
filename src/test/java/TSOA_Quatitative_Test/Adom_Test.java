package TSOA_Quatitative_Test;

import examples.si.AlgorithmFactory;
import examples.si.benchmarks.DynamicUnimodalObjectiveFunctions.Function1;
import examples.si.benchmarks.singleObjective.SphereFunction;
import org.junit.Ignore;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.util.Randoms;
import utils.Utils;

import java.util.Date;

public class Adom_Test {
    int n = 30;
    int p = 30;
    int steps = 1000;

    ObjectiveFunction fn = new SphereFunction().updateDimensions(n);




    @RepeatedTest(1)
    public void testAVOA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(17, fn).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            System.out.println(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    // -----------------------------------------------------

    @RepeatedTest(1)
    public void testGOA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(14, fn).getAlgorithm(steps, p);
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
            SIAlgorithm algo = new AlgorithmFactory(15, fn).getAlgorithm(steps, p);
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

    @RepeatedTest(10)
    public void testTSOA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(29, fn).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            System.out.println(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
