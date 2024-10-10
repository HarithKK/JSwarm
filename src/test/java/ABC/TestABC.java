package ABC;

import examples.si.benchmarks.singleObjective.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import examples.si.algo.abc.ABC;
import utils.AssertUtil;
import utils.Logger;

public class TestABC {
    private static final int LIMIT = 2;
    private SIAlgorithm algo;

    private static final double PRECISION_VAL  = 10;

    private SIAlgorithm getAlgorithm(ObjectiveFunction fn){
        return new ABC(
                fn,
                500,
                fn.getNumberOfDimensions(),
                200,
                fn.getMin(),
                fn.getMax(),
                100,
                true
        );
    }

    public void evaluate(SIAlgorithm algo, double best, double[] variables, int D, double variance){
        AssertUtil.evaluate(
                algo.getBestDoubleValue(),
                best,
                algo.getGBest(),
                variables,
                D,
                variance,
                LIMIT
        );
    }

    @Test
    public void testAckleysFunction() {

        ObjectiveFunction fn = new AckleysFunction();
        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
    public void testBoothFunction() {

        ObjectiveFunction fn = new BoothsFunction();
        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
    public void testMatyasFunction() {

        ObjectiveFunction fn = new MatyasFunction();
        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }


    @Test
    public void testRastriginFunction() {

        ObjectiveFunction fn = new RastriginFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
    public void testSphereFunction() {

        ObjectiveFunction fn = new SphereFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
    public void testRosenbrockFunction() {

        ObjectiveFunction fn = new RosenbrockFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
    public void testBealeFunction() {

        ObjectiveFunction fn = new BealeFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
    public void testBukinFunction() {

        ObjectiveFunction fn = new Bukin4Function();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AssertUtil.evaluate(algo.getBestDoubleValue(), fn.getExpectedBestValue(), PRECISION_VAL, LIMIT);
    }

    @Test
    public void testLevyFunction() {

        ObjectiveFunction fn = new LevyFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AssertUtil.evaluate(algo.getBestDoubleValue(), fn.getExpectedBestValue(), PRECISION_VAL, LIMIT);
    }

    @Test
    public void testHimmelblausFunction() {

        ObjectiveFunction fn = new HimmelblausFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AssertUtil.evaluate(algo.getBestDoubleValue(), fn.getExpectedBestValue(), 2, 2);

    }

    @Test
    public void testThreeHumpCamelFunction() {

        ObjectiveFunction fn = new ThreeHumpCamelFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
    public void testEasomFunction() {

        ObjectiveFunction fn = new EasomFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AssertUtil.evaluate(algo.getBestDoubleValue(), fn.getExpectedBestValue(), PRECISION_VAL, 1);
    }

    @Test
    public void testCrossInTrayFunction() {

        ObjectiveFunction fn = new CrossInTrayFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),1);
    }

    @Test
    public void testEggholderFunction() {

        ObjectiveFunction fn = new EggholderFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AssertUtil.evaluate(algo.getBestDoubleValue(), fn.getExpectedBestValue(), 100, 2);
    }

    @Test
    public void testHolderTableFunction() {

        ObjectiveFunction fn = new HolderTableFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
    public void testMcCormickFunction() {

        ObjectiveFunction fn = new McCormickFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AssertUtil.evaluate(algo.getBestDoubleValue(), fn.getExpectedBestValue(), PRECISION_VAL, 1);
    }

    @Test
    public void testSchafferN2Function() {

        ObjectiveFunction fn = new SchafferFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
    }

    @Test
    public void testSchafferN4Function() {

        ObjectiveFunction fn = new SchafferFunctionN4();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),20);
    }

    @Test
    public void testStyblinskiTangFunction() {

        ObjectiveFunction fn = new StyblinskiTangFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),20);
    }

    @AfterEach
    public void afterEach(){
        Logger.showFunction(algo);
    }
}
