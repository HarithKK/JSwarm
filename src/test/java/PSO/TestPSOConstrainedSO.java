package PSO;

import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.singleObjectiveConstrained.RosenbrockFunction;
import org.usa.soc.IAlgorithm;
import org.usa.soc.pso.PSO;
import utils.Logger;

import java.util.List;

@Ignore("Not Ready")
public class TestPSOConstrainedSO {

    private static final int LIMIT = 2;
    private PSO p;

    private IAlgorithm getAlgorithm(ObjectiveFunction fn){
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
        p.runOptimizer();

        List<Double> arr = p.getGBest().toAbsList(LIMIT);
        Assertions.assertEquals(1.0,arr.get(0));
        Assertions.assertEquals(1.0,arr.get(1));
        Assertions.assertEquals(0,p.getGBestAbsValue(LIMIT));

    }

    @AfterEach
    public void afterEach(TestInfo in){
        //Logger.showFunction(p);
    }
}
