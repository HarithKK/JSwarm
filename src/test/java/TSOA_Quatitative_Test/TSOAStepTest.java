package TSOA_Quatitative_Test;

import examples.si.algo.also.ALSO;
import examples.si.algo.tsoa.TSOA;
import examples.si.algo.tsoa.Tree;
import examples.si.benchmarks.DynamicUnimodalObjectiveFunctions.Function1;
import org.junit.Ignore;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.action.StepAction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.ObjectiveFunction;
import utils.DB;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Ignore
public class TSOAStepTest {

    int nd = 30;
    int populationSize = 30;
    int stepCount = 500;
    ObjectiveFunction function = new Function1(nd);
    int itr = 10;

    public void TestTSOA() throws Exception {
        DB db = new DB();
        db.connect();
        TSOA algo = new TSOA(
                function,
                populationSize,
                stepCount,
                function.getNumberOfDimensions(),
                function.getMin(),
                function.getMax(),
                true,
                10,
                0.7,
                0.5,
                2.05,
                0.1
        );
    }
    public void testALSO() throws Exception {
        ALSO algo = new ALSO(
                function,
                populationSize,
                stepCount,
                function.getNumberOfDimensions(),
                function.getMin(),
                function.getMax(),
                true,
                10, 210,
                2.5,
                1.0,
                10,
                10
        );

        algo.initialize();
        algo.run();

        System.out.println(algo.getBestDoubleValue());
    }
}
