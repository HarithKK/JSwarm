package MBO;

import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.singleObjective.*;
import org.usa.soc.mbo.MBO;
import org.usa.soc.multiRunner.MultiRunner;
import org.usa.soc.util.Mathamatics;
import utils.AssertUtil;
import utils.Logger;

@Ignore("Class not ready for tests")
public class TestMBO {

    private static final int LIMIT = 15;
    private MultiRunner algo;

    private static final double PRECISION_VAL  = 50;

    private MultiRunner getAlgorithm(ObjectiveFunction fn){
        return new MultiRunner(new MBO(
                fn,
                500,
                100,
                30,
                1000,
                fn.getNumberOfDimensions(),
                fn.getMin(),
                fn.getMax(),
                true,
                0.9,
                0,
                10,
                0.5,
                0.5,
                10
        ),5);
    }

    private MultiRunner getAlgorithm(ObjectiveFunction fn, int i){
        return new MultiRunner(new MBO(
                fn,
                500,
                100,
                30,
                i,
                fn.getNumberOfDimensions(),
                fn.getMin(),
                fn.getMax(),
                true,
                0.4,
                0,
                10,
                0.8,
                0.5,
                1
        ),5);
    }

    public void evaluate(MultiRunner algo, double best, double[] variables, int D, double variance){
        AssertUtil.evaluate(
                algo.getBestDValue(),
                best,
                algo.getBestVector(),
                variables,
                D,
                variance,
                LIMIT
        );
    }

    @Test
@Disabled
    public void testAckleysFunction() {

        ObjectiveFunction fn = new AckleysFunction();
        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testBoothFunction() {

        ObjectiveFunction fn = new BoothsFunction();
        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testMatyasFunction() {

        ObjectiveFunction fn = new MatyasFunction();
        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }


    @Test
@Disabled
    public void testRastriginFunction() {

        ObjectiveFunction fn = new RastriginFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testSphereFunction() {

        ObjectiveFunction fn = new SphereFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testRosenbrockFunction() {

        ObjectiveFunction fn = new RosenbrockFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testBealeFunction() {

        ObjectiveFunction fn = new BealeFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testBukinFunction() {

        ObjectiveFunction fn = new BukinFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),3);
    }

    @Test
@Disabled
    public void testLevyFunction() {

        ObjectiveFunction fn = new LevyFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testHimmelblausFunction() {

        ObjectiveFunction fn = new HimmelblausFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        Assertions.assertEquals(Mathamatics.round(algo.getBestDValue(), LIMIT), 0);

    }

    @Test
@Disabled
    public void testThreeHumpCamelFunction() {

        ObjectiveFunction fn = new ThreeHumpCamelFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testEasomFunction() {

        ObjectiveFunction fn = new EasomFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testCrossInTrayFunction() {

        ObjectiveFunction fn = new CrossInTrayFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testEggholderFunction() {

        ObjectiveFunction fn = new EggholderFunction();

        algo = getAlgorithm(fn, 2000);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testHolderTableFunction() {

        ObjectiveFunction fn = new HolderTableFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testMcCormickFunction() {

        ObjectiveFunction fn = new McCormickFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testSchafferN2Function() {

        ObjectiveFunction fn = new SchafferFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
@Disabled
    public void testSchafferN4Function() {

        ObjectiveFunction fn = new SchafferFunctionN4();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),1.25);
    }

    @Test
@Disabled
    public void testStyblinskiTangFunction() {

        ObjectiveFunction fn = new StyblinskiTangFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        double p = Mathamatics.round(algo.getBestDValue(),LIMIT);
        Assertions.assertEquals(-117.5,p);

        double []d = algo.getBestVector().toDoubleArray(LIMIT);
        Assertions.assertArrayEquals(new double[]{-2.9, -2.9, -2.9}, d);
    }

    @AfterEach
    public void afterEach(){
        Logger.showFunction(algo);
    }
}
