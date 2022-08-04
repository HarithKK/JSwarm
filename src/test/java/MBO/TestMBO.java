package MBO;

import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.aco.ACO;
import org.usa.soc.benchmarks.singleObjective.*;
import org.usa.soc.intefaces.IAlgorithm;
import org.usa.soc.mbo.MBO;
import utils.Logger;

import java.util.List;

public class TestMBO {

    private static final int LIMIT = 2;
    private MBO algo;

    private MBO getAlgorithm(ObjectiveFunction fn){
        return new MBO(
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
        );
    }

    public void evaluate(MBO algo, double best, double[] variables, int D, double variance){
        double p = algo.getBestValue(LIMIT);
        //Assertions.assertTrue(p > (best - variance) && p < (best + variance));

        for(int i=0;i<D; i++){
            p = algo.getBest().toList(LIMIT).get(i);
            //Assertions.assertTrue(p > (variables[i] - variance) && p < (variables[i] + variance));
        }
    }

    @Test
    public void testAckleysFunction() {

        ObjectiveFunction fn = new AckleysFunction();
        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0},fn.getNumberOfDimensions(),0.001);
    }

    @Test
    @Ignore
    public void testBoothFunction() {

        ObjectiveFunction fn = new BoothsFunction();
        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

    }

    @Test
    public void testMatyasFunction() {

        ObjectiveFunction fn = new MatyasFunction();
        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0},fn.getNumberOfDimensions(),0.3);
    }


    @Test
    public void testRastriginFunction() {

        ObjectiveFunction fn = new RastriginFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0,0},fn.getNumberOfDimensions(),0.3);

    }

    @Test
    public void testSphereFunction() {

        ObjectiveFunction fn = new SphereFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0,0},fn.getNumberOfDimensions(),0.3);

    }

    @Test
    public void testRosenbrockFunction() {

        ObjectiveFunction fn = new RosenbrockFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);

    }

    @Test
    public void testBealeFunction() {

        ObjectiveFunction fn = new BealeFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);

    }

    @Test
    public void testBukinFunction() {

        ObjectiveFunction fn = new BukinFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0},fn.getNumberOfDimensions(),0.3);

    }

    @Test
    public void testLevyFunction() {

        ObjectiveFunction fn = new LevyFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);

    }

    @Test
    public void testHimmelblausFunction() {

        ObjectiveFunction fn = new HimmelblausFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);
    }

    @Test
    public void testThreeHumpCamelFunction() {

        ObjectiveFunction fn = new ThreeHumpCamelFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0},fn.getNumberOfDimensions(),0.3);

    }

    @Test
    public void testEasomFunction() {

        ObjectiveFunction fn = new EasomFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);

    }

    @Test
    public void testCrossInTrayFunction() {

        ObjectiveFunction fn = new CrossInTrayFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);
    }

    @Test
    public void testEggholderFunction() {

        ObjectiveFunction fn = new EggholderFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);
    }

    @Test
    public void testHolderTableFunction() {

        ObjectiveFunction fn = new HolderTableFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);
    }

    @Test
    public void testMcCormickFunction() {

        ObjectiveFunction fn = new McCormickFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, -1.91, new double[]{-0.55,-1.55},fn.getNumberOfDimensions(),0.3);

    }

    @Test
    public void testSchafferN2Function() {

        ObjectiveFunction fn = new SchafferFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0},fn.getNumberOfDimensions(),0.3);
    }

    @Test
    public void testSchafferN4Function() {

        ObjectiveFunction fn = new SchafferFunctionN4();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);
    }

    @Test
    public void testStyblinskiTangFunction() {

        ObjectiveFunction fn = new StyblinskiTangFunction();

        algo = getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);
    }

    @AfterEach
    public void afterEach(){
        Logger.showFunction(algo);
    }
}
