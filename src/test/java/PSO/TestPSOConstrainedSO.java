package PSO;

import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.usa.soc.core.Algorithm;
import org.usa.soc.core.ObjectiveFunction;
import org.usa.soc.benchmarks.singleObjectiveConstrained.RosenbrockFunction;
import org.usa.soc.si.pso.PSO;
import org.usa.soc.util.Mathamatics;

import java.util.List;

@Ignore("Not Ready")
public class TestPSOConstrainedSO {

    private static final int LIMIT = 2;
    private PSO p;

    private Algorithm getAlgorithm(ObjectiveFunction fn){
        return new PSO(
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
    }

    @Test
    @Disabled
    public void testRosenbrockFunction() {

        ObjectiveFunction fn = new RosenbrockFunction();

        p = (PSO)getAlgorithm(fn);
        p.initialize();
        try {
            p.runOptimizer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(1.0,arr.get(0));
        Assertions.assertEquals(1.0,arr.get(1));
        Assertions.assertEquals(0, Mathamatics.absRound(p.getBestDoubleValue(),LIMIT));

    }

    @AfterEach
    public void afterEach(TestInfo in){
        //Logger.showFunction(p);
    }
}
