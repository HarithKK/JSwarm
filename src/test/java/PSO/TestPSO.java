package PSO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.singleObjective.*;
import org.usa.soc.pso.PSO;

import java.util.List;

public class TestPSO {

    private static final int LIMIT = 2;

    @Test
    public void testPSOFunctionA() {

        ObjectiveFunction fn = new FunctionA();
        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(1.5, p.getGBest().toAbsList(LIMIT).get(0));
        Assertions.assertEquals(1.69,p.getGBestAbsValue(LIMIT));
    }

    @Test
    public void testAckleysFunction() {

        ObjectiveFunction fn = new AckleysFunction();
        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(0,arr.get(0));
        Assertions.assertEquals(0,arr.get(1));
        Assertions.assertEquals(0,p.getGBestAbsValue(LIMIT));
    }

    @Test
    public void testBoothFunction() {

        ObjectiveFunction fn = new BoothsFunction();
        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(1, arr.get(0));
        Assertions.assertEquals(3, arr.get(1));
        Assertions.assertEquals(0, p.getGBestAbsValue(LIMIT));
    }

    @Test
    public void testMatyasFunction() {

        ObjectiveFunction fn = new MatyasFunction();
        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(0,arr.get(0));
        Assertions.assertEquals(0,arr.get(1));
        Assertions.assertEquals(0,p.getGBestAbsValue(LIMIT));
    }


    @Test
    public void testRastriginFunction() {

        ObjectiveFunction fn = new RastriginFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                3000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(0, p.getGBestAbsValue(LIMIT));
        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(0,arr.get(0));
        Assertions.assertEquals(0,arr.get(1));
        Assertions.assertEquals(0,arr.get(2));

    }

    @Test
    public void testSphereFunction() {

        ObjectiveFunction fn = new SphereFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(p.getGBestAbsValue(LIMIT), 0);
        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(0,arr.get(0));
        Assertions.assertEquals(0,arr.get(1));
        Assertions.assertEquals(0,arr.get(2));

    }

    @Test
    public void testRosenbrockFunction() {

        ObjectiveFunction fn = new RosenbrockFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(p.getGBestAbsValue(LIMIT), 0);
        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(1,arr.get(0));
        Assertions.assertEquals(1,arr.get(1));
        Assertions.assertEquals(1,arr.get(2));

    }

    @Test
    public void testBealeFunction() {

        ObjectiveFunction fn = new BealeFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(0, p.getGBestAbsValue(LIMIT));
        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(3,arr.get(0));
        Assertions.assertEquals(0.5,arr.get(1));

    }

    @Test
    public void testBukinFunction() {

        ObjectiveFunction fn = new BukinFunction();

        PSO p = new PSO(
                fn,
                100,
                fn.getNumberOfDimensions(),
                3000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(0, p.getGBestAbsValue(LIMIT-2));

    }

    @Test
    public void testLevyFunction() {

        ObjectiveFunction fn = new LevyFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(0, p.getGBestAbsValue(LIMIT));
        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(1,arr.get(0));
        Assertions.assertEquals(1,arr.get(1));

    }

    @Test
    public void testHimmelblausFunction() {

        ObjectiveFunction fn = new HimmelblausFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(0, p.getGBestAbsValue(LIMIT));
        List<Double> arr = p.getGBest().toList(LIMIT);
        Assertions.assertTrue(arr.get(0) == 3.0 || arr.get(0) == -2.81 || arr.get(0) == -3.78 || arr.get(0) == 3.58);
        Assertions.assertTrue(arr.get(1) == 2.0 || arr.get(1) == 3.13 || arr.get(1) == -3.28 || arr.get(1) == -1.85);
    }

    @Test
    public void testThreeHumpCamelFunction() {

        ObjectiveFunction fn = new ThreeHumpCamelFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(0, p.getGBestAbsValue(LIMIT));
        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(0,arr.get(0));
        Assertions.assertEquals(0,arr.get(1));

    }

    @Test
    public void testEasomFunction() {

        ObjectiveFunction fn = new EasomFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(-1, p.getGBestValue(LIMIT));
        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(3.14,arr.get(0));
        Assertions.assertEquals(3.14,arr.get(1));

    }

    @Test
    public void testCrossInTrayFunction() {

        ObjectiveFunction fn = new CrossInTrayFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(-2.06, p.getGBestValue(LIMIT));
        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(1.35,arr.get(0));
        Assertions.assertEquals(1.35,arr.get(1));
    }

    @Test
    public void testEggholderFunction() {

        ObjectiveFunction fn = new EggholderFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                2000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(-959.64, p.getGBestValue(LIMIT));
        List<Double> arr = p.getGBest().toList(LIMIT);
        Assertions.assertEquals(512,arr.get(0));
        Assertions.assertEquals(404.23,arr.get(1));
    }

    @Test
    public void testHolderTableFunction() {

        ObjectiveFunction fn = new HolderTableFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(-19.21, p.getGBestValue(LIMIT));
        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(8.06,arr.get(0));
        Assertions.assertEquals(9.66,arr.get(1));
    }

    @Test
    public void testMcCormickFunction() {

        ObjectiveFunction fn = new McCormickFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(-1.91, p.getGBestValue(LIMIT));
        List<Double> arr = p.getGBest().toList(LIMIT);
        Assertions.assertEquals(-0.55,arr.get(0));
        Assertions.assertEquals(-1.55,arr.get(1));
    }

    @Test
    public void testSchafferN2Function() {

        ObjectiveFunction fn = new SchafferFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(0, p.getGBestAbsValue(LIMIT));
        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(0,arr.get(0));
        Assertions.assertEquals(0,arr.get(1));
    }

    @Test
    public void testSchafferN4Function() {

        ObjectiveFunction fn = new SchafferFunctionN4();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(0.29, p.getGBestAbsValue(LIMIT));
        List<Double> arr = p.getGBest().toList(LIMIT);
        Assertions.assertTrue(arr.get(0) == 0 || arr.get(0) == 0 || arr.get(0) == 1.25 || arr.get(0) == -1.25);
        Assertions.assertTrue(arr.get(1) == 1.25 || arr.get(1) == -1.25 || arr.get(1) == 0 || arr.get(1) == 0);
    }

    @Test
    public void testStyblinskiTangFunction() {

        ObjectiveFunction fn = new StyblinskiTangFunction();

        PSO p = new PSO(
                fn,
                1000,
                fn.getNumberOfDimensions(),
                1000,
                1.496180,
                1.496180,
                0.729844,
                fn.getMin(),
                fn.getMax(),
                true);

        p.runOptimizer();

        Assertions.assertEquals(117.5, p.getGBestAbsValue(LIMIT));
        List<Double> arr = p.getGBest().toList(LIMIT);

        Assertions.assertEquals(-2.9,arr.get(0));
        Assertions.assertEquals(-2.9,arr.get(1));
        Assertions.assertEquals(-2.9,arr.get(2));
    }
}
