package TSOA;


import org.junit.jupiter.api.Test;
import org.usa.soc.Algorithm;
import org.usa.soc.benchmarks.DynamicUnimodalObjectiveFunctions.Function1;
import org.usa.soc.core.Action;
import org.usa.soc.core.Vector;
import ui.AlgoStore;

/**
 * Targeted algorithms
 *  MFA
 *  PSO
 *  ALSO
 *  ABC
 *  CSO
 */

public class TestF1 {

    int n = 100;
    int p = 500;
    int steps = 1000;

    @Test
    public void testTSOA() {
        try {
            Algorithm algo = new AlgoStore(29, new Function1(n)).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            System.out.println(algo.getName() + "  : " +algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSSA() {
        try {
            Algorithm algo = new AlgoStore(19, new Function1(n)).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            System.out.println(algo.getName() + "  : " +algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMFA() {
        try {
            Algorithm algo = new AlgoStore(13, new Function1(n)).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            System.out.println(algo.getName() + "  : " +algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCSO() {
        try {
            Algorithm algo = new AlgoStore(2, new Function1(n)).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            System.out.println(algo.getName() + "  : " +algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPSO() {
        try {
            Algorithm algo = new AlgoStore(0, new Function1(n)).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            System.out.println(algo.getName() + "  : " +algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testALSO() {
        try {
            Algorithm algo = new AlgoStore(15, new Function1(n)).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            System.out.println(algo.getName() + "  : " +algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testBA() {
        try {
            Algorithm algo = new AlgoStore(10, new Function1(n)).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            System.out.println(algo.getName() + "  : " +algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testWSO() {
        try {
            Algorithm algo = new AlgoStore(6, new Function1(n)).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            System.out.println(algo.getName() + "  : " +algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAVOA() {
        try {
            Algorithm algo = new AlgoStore(17, new Function1(n)).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            System.out.println(algo.getName() + "  : " +algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTSA() {
        try {
            Algorithm algo = new AlgoStore(18, new Function1(n)).getAlgorithm(steps, p);
            algo.initialize();
            algo.runOptimizer();
            System.out.println(algo.getName() + "  : " +algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGWO() {
        try {
            Algorithm algo = new AlgoStore(12, new Function1(n)).getAlgorithm(steps, p);

            algo.addStepAction(new Action() {
                @Override
                public void performAction(Vector best, Double bestValue, int step) {
                    System.out.println(bestValue);
                }
            });
            algo.initialize();
            algo.runOptimizer();
            System.out.println(algo.getName() + "  : " +algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
