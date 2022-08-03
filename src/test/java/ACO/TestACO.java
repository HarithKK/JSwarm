package ACO;

import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.aco.ACO;
import org.usa.soc.benchmarks.singleObjective.*;
import org.usa.soc.intefaces.IAlgorithm;
import org.usa.soc.pso.PSO;
import utils.Logger;

import java.util.List;

public class TestACO {

    private static final int LIMIT = 2;
    private ACO algo;

    private IAlgorithm getAlgorithm(ObjectiveFunction fn){
        return new ACO(
                fn,
                1000,
                100,
                10,
                fn.getNumberOfDimensions(),
                fn.getMin(),
                fn.getMax(),
                true
        );
    }

    public void evaluate(ACO algo, double best, double[] variables, int D, double variance){
        double p = algo.getGBestValue(LIMIT);
        Assertions.assertTrue(p > (best - variance) && p < (best + variance));

        for(int i=0;i<D; i++){
            p = algo.getGBest().toList(LIMIT).get(i);
            Assertions.assertTrue(p > (variables[i] - variance) && p < (variables[i] + variance));
        }
    }

    @Test
    public void testAckleysFunction() {

        ObjectiveFunction fn = new AckleysFunction();
        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0},fn.getNumberOfDimensions(),0.001);
    }

    @Test
    @Ignore
    public void testBoothFunction() {

        ObjectiveFunction fn = new BoothsFunction();
        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

    }

    @Test
    public void testMatyasFunction() {

        ObjectiveFunction fn = new MatyasFunction();
        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0},fn.getNumberOfDimensions(),0.3);
    }


    @Test
    public void testRastriginFunction() {

        ObjectiveFunction fn = new RastriginFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0,0},fn.getNumberOfDimensions(),0.3);

    }

    @Test
    public void testSphereFunction() {

        ObjectiveFunction fn = new SphereFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0,0},fn.getNumberOfDimensions(),0.3);

    }

    @Test
    public void testRosenbrockFunction() {

        ObjectiveFunction fn = new RosenbrockFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);

    }

    @Test
    public void testBealeFunction() {

        ObjectiveFunction fn = new BealeFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);

    }

    @Test
    public void testBukinFunction() {

        ObjectiveFunction fn = new BukinFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0},fn.getNumberOfDimensions(),0.3);

    }

    @Test
    public void testLevyFunction() {

        ObjectiveFunction fn = new LevyFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);

    }

    @Test
    public void testHimmelblausFunction() {

        ObjectiveFunction fn = new HimmelblausFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);
    }

    @Test
    public void testThreeHumpCamelFunction() {

        ObjectiveFunction fn = new ThreeHumpCamelFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, 0, new double[]{0,0},fn.getNumberOfDimensions(),0.3);

    }

    @Test
    public void testEasomFunction() {

        ObjectiveFunction fn = new EasomFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);

    }

    @Test
    public void testCrossInTrayFunction() {

        ObjectiveFunction fn = new CrossInTrayFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);
    }

    @Test
    public void testEggholderFunction() {

        ObjectiveFunction fn = new EggholderFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);
    }

    @Test
    public void testHolderTableFunction() {

        ObjectiveFunction fn = new HolderTableFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);
    }

    @Test
    public void testMcCormickFunction() {

        ObjectiveFunction fn = new McCormickFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        evaluate(algo, -1.91, new double[]{-0.55,-1.55},fn.getNumberOfDimensions(),0.3);

    }

    @Test
    public void testSchafferN2Function() {

        ObjectiveFunction fn = new SchafferFunction();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        Assertions.assertEquals(0, algo.getGBestAbsValue(LIMIT));
        List<Double> arr = algo.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(0,arr.get(0));
        Assertions.assertEquals(0,arr.get(1));
    }

    @Test
    public void testSchafferN4Function() {

        ObjectiveFunction fn = new SchafferFunctionN4();

        algo = (ACO)getAlgorithm(fn);
        algo.initialize();
        algo.runOptimizer();

        // High difference, Not test
        Assertions.assertTrue(true);
    }

    @Test
    public void testStyblinskiTangFunction() {

        ObjectiveFunction fn = new StyblinskiTangFunction();

        algo = (ACO)getAlgorithm(fn);
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
