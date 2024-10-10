package TSOA_Quatitative_Test;

import examples.si.AlgorithmFactory;
import examples.si.algo.tsoa.TSOA;
import examples.si.benchmarks.DynamicMultiModalObjectiveFunctions.GriewanktFunction;
import examples.si.benchmarks.cec2005.ShiftedSphereFunction;
import examples.si.benchmarks.cec2014.RotatedHighConditionedEllipticFunction;
import examples.si.benchmarks.cec2017.HybridFunction1;
import examples.si.benchmarks.cec2022.HybridFunction2;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.ZakharovFunction;
import examples.si.benchmarks.singleObjective.RastriginFunction;
import examples.si.benchmarks.singleObjective.SchafferFunction;
import examples.si.benchmarks.singleObjective.SphereFunction;
import org.junit.jupiter.api.Test;
import org.usa.soc.si.ObjectiveFunction;
import utils.ResultAggrigator;

public class SensitivityAnalysis {

    int[] agents = new int[]{10, 30, 50, 100, 200};

    @Test
    public void testTSOASpheareF8() {
        ObjectiveFunction fn = new SphereFunction().updateDimensions(30);
        for(int x : agents){
            try {
                TSOA algo = (TSOA) new AlgorithmFactory(29, fn)
                        .getAlgorithm(ResultAggrigator.STEPS, x);
                algo.initialize();
                algo.run();
                ResultAggrigator.getInstance().updateTestResuly(algo, fn.getClass().getSimpleName(), x);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ResultAggrigator.clear();
    }

    @Test
    public void testTSOAZackrovF16() {
        ObjectiveFunction fn = new ZakharovFunction().updateDimensions(30);
        for(int x : agents){
            try {
                TSOA algo = (TSOA) new AlgorithmFactory(29, fn)
                        .getAlgorithm(ResultAggrigator.STEPS, x);
                algo.initialize();
                algo.run();
                ResultAggrigator.getInstance().updateTestResuly(algo, fn.getClass().getSimpleName(), x);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ResultAggrigator.clear();
    }

    @Test
    public void testTSOARastrigiF27() {
        ObjectiveFunction fn = new RastriginFunction().updateDimensions(30);
        for(int x : agents){
            try {
                TSOA algo = (TSOA) new AlgorithmFactory(29, fn)
                        .getAlgorithm(ResultAggrigator.STEPS, x);
                algo.initialize();
                algo.run();
                ResultAggrigator.getInstance().updateTestResuly(algo, fn.getClass().getSimpleName(), x);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ResultAggrigator.clear();
    }

    @Test
    public void testTSOAGriewanktFunctionF35() {
        ObjectiveFunction fn = new GriewanktFunction(30);
        for(int x : agents){
            try {
                TSOA algo = (TSOA) new AlgorithmFactory(29, fn)
                        .getAlgorithm(ResultAggrigator.STEPS, x);
                algo.initialize();
                algo.run();
                ResultAggrigator.getInstance().updateTestResuly(algo, fn.getClass().getSimpleName(), x);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ResultAggrigator.clear();
    }

    @Test
    public void testTSOAShiftedSphereFunctionF38() {
        ObjectiveFunction fn = new ShiftedSphereFunction().updateDimensions(30);
        for(int x : agents){
            try {
                TSOA algo = (TSOA) new AlgorithmFactory(29, fn)
                        .getAlgorithm(ResultAggrigator.STEPS, x);
                algo.initialize();
                algo.run();
                ResultAggrigator.getInstance().updateTestResuly(algo, fn.getClass().getSimpleName(), x);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ResultAggrigator.clear();
    }

    @Test
    public void testTSOARotatedHighConditionedEllipticFunctionF44() {
        ObjectiveFunction fn = new RotatedHighConditionedEllipticFunction().updateDimensions(20);
        for(int x : agents){
            try {
                TSOA algo = (TSOA) new AlgorithmFactory(29, fn)
                        .getAlgorithm(ResultAggrigator.STEPS, x);
                algo.initialize();
                algo.run();
                ResultAggrigator.getInstance().updateTestResuly(algo, fn.getClass().getSimpleName(), x);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ResultAggrigator.clear();
    }

    @Test
    public void testTSOAHybridFunction1F54() {
        ObjectiveFunction fn = new HybridFunction1().updateDimensions(30);
        for(int x : agents){
            try {
                TSOA algo = (TSOA) new AlgorithmFactory(29, fn)
                        .getAlgorithm(ResultAggrigator.STEPS, x);
                algo.initialize();
                algo.run();
                ResultAggrigator.getInstance().updateTestResuly(algo, fn.getClass().getSimpleName(), x);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ResultAggrigator.clear();
    }

    @Test
    public void testTSOAHybridFunction2F69() {
        ObjectiveFunction fn = new HybridFunction2().updateDimensions(30);
        for(int x : agents){
            try {
                TSOA algo = (TSOA) new AlgorithmFactory(29, fn)
                        .getAlgorithm(ResultAggrigator.STEPS, x);
                algo.initialize();
                algo.run();
                ResultAggrigator.getInstance().updateTestResuly(algo, fn.getClass().getSimpleName(), x);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ResultAggrigator.clear();
    }
}
