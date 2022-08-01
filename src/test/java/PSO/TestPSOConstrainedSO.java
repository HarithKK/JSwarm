package PSO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.singleObjectiveConstrained.MishraBirdFunction;
import org.usa.soc.benchmarks.singleObjectiveConstrained.RosenbrockFunction;
import org.usa.soc.benchmarks.singleObjectiveConstrained.RosenbrockFunctionDish;
import org.usa.soc.intefaces.IAlgorithm;
import org.usa.soc.pso.PSO;
import org.usa.soc.util.Constraint;
import utils.Logger;

import java.util.List;

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
        Logger.showFunction(p);
    }
}
