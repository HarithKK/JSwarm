package TSOA;

import org.junit.jupiter.api.Test;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.si.ObjectiveFunction;
import examples.si.benchmarks.DynamicUnimodalObjectiveFunctions.Function1;
import org.usa.soc.core.action.StepAction;
import org.usa.soc.core.ds.Vector;
import examples.si.algo.tsoa.TSOA;
import utils.AssertUtil;

public class TestTSOA {

    private static final int LIMIT = 2;
    private static final double PRECISION_VAL = 10;
    private SIAlgorithm algo;

    private SIAlgorithm getAlgorithm(ObjectiveFunction fn, int p, int s, double df) {
        return new TSOA(
                fn,
                p,
                s,
                fn.getNumberOfDimensions(),
                fn.getMin(),
                fn.getMax(),
                true,
                df,
                2
        );
    }

    public void evaluate(SIAlgorithm algo, double best, double[] variables, int D, double variance) {
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
    public void testFunction1Function() {

        ObjectiveFunction fn = new Function1(100);
        algo = getAlgorithm(fn, 30, 1000, 0.7);
        algo.addStepAction(new StepAction() {
            @Override
            public void performAction(Vector best, Double bestValue, int step) {
                System.out.println(bestValue + " | "+ step);
            }
        });
        algo.initialize();
        try {
            algo.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(algo.getBestDoubleValue() + " " + algo);
        evaluate(algo, fn.getExpectedBestValue(), fn.getExpectedParameters(), fn.getNumberOfDimensions(), PRECISION_VAL);
    }
}
