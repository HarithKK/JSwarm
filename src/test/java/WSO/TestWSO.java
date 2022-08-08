package WSO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.singleObjective.*;
import org.usa.soc.multiRunner.MultiRunner;
import org.usa.soc.wso.WSO;
import utils.AssertUtil;
import utils.Logger;

public class TestWSO {

        private static final int LIMIT = 2;

        private static final double PRECISION_VAL  = 20;
        private MultiRunner algo;

        private MultiRunner getAlgorithm(ObjectiveFunction fn){
            return new MultiRunner(new WSO(
                    fn,
                    1000,
                    1000,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
                    0.4,
                    0.4,
                    true
            ),1);
        }

        private MultiRunner getAlgorithm(ObjectiveFunction fn, int i){
            return new MultiRunner(new WSO(
                    fn,
                    i,
                    1000,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
                    0.4,
                    0.4,
                    true
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
        public void testAckleysFunction() {

            ObjectiveFunction fn = new AckleysFunction();
            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testBoothFunction() {

            ObjectiveFunction fn = new BoothsFunction();
            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testMatyasFunction() {

            ObjectiveFunction fn = new MatyasFunction();
            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }


        @Test
        public void testRastriginFunction() {

            ObjectiveFunction fn = new RastriginFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testSphereFunction() {

            ObjectiveFunction fn = new SphereFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testRosenbrockFunction() {

            ObjectiveFunction fn = new RosenbrockFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testBealeFunction() {

            ObjectiveFunction fn = new BealeFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testBukinFunction() {

            ObjectiveFunction fn = new BukinFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            AssertUtil.evaluate(algo.getBestDValue(), fn.getExpectedBestValue(), PRECISION_VAL, LIMIT);
        }

        @Test
        public void testLevyFunction() {

            ObjectiveFunction fn = new LevyFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testHimmelblausFunction() {

            ObjectiveFunction fn = new HimmelblausFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            AssertUtil.evaluate(algo.getBestDValue(), fn.getExpectedBestValue(),3, 1);

        }

        @Test
        public void testThreeHumpCamelFunction() {

            ObjectiveFunction fn = new ThreeHumpCamelFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testEasomFunction() {

            ObjectiveFunction fn = new EasomFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testCrossInTrayFunction() {

            ObjectiveFunction fn = new CrossInTrayFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testEggholderFunction() {

            ObjectiveFunction fn = new EggholderFunction();

            algo = getAlgorithm(fn, 2000);
            algo.initialize();
            algo.runOptimizer();

            AssertUtil.evaluate(algo.getBestDValue(), fn.getExpectedBestValue(), 50, LIMIT);
        }

        @Test
        public void testHolderTableFunction() {

            ObjectiveFunction fn = new HolderTableFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testMcCormickFunction() {

            ObjectiveFunction fn = new McCormickFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testSchafferN2Function() {

            ObjectiveFunction fn = new SchafferFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),PRECISION_VAL);
        }

        @Test
        public void testSchafferN4Function() {

            ObjectiveFunction fn = new SchafferFunctionN4();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(),fn.getNumberOfDimensions(),10);
        }

        @Test
        public void testStyblinskiTangFunction() {

            ObjectiveFunction fn = new StyblinskiTangFunction();

            algo = getAlgorithm(fn);
            algo.initialize();
            algo.runOptimizer();

            AssertUtil.evaluate(algo.getBestDValue(), 117.5, 20, LIMIT);

        }


        @AfterEach
            public void afterEach(){
                Logger.showFunction(algo);
            }
        }
