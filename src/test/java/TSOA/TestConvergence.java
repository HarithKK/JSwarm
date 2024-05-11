package TSOA;

import org.junit.jupiter.api.Test;
import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.DynamicCompositeBenchmarkFunctions.Function16;
import org.usa.soc.benchmarks.DynamicCompositeBenchmarkFunctions.Function17;
import org.usa.soc.benchmarks.DynamicCompositeBenchmarkFunctions.Function18;
import org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions.Function10;
import org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions.Function8;
import org.usa.soc.benchmarks.DynamicMultiModalObjectiveFunctions.Function9;
import org.usa.soc.benchmarks.DynamicUnimodalObjectiveFunctions.Function1;
import org.usa.soc.benchmarks.DynamicUnimodalObjectiveFunctions.Function2;
import org.usa.soc.benchmarks.DynamicUnimodalObjectiveFunctions.Function3;
import org.usa.soc.benchmarks.FixMultiModalObjectiveFunctions.Function22;
import org.usa.soc.benchmarks.FixMultiModalObjectiveFunctions.Function24;
import org.usa.soc.benchmarks.FixMultiModalObjectiveFunctions.Function25;
import org.usa.soc.core.Action;
import org.usa.soc.core.Vector;
import org.usa.soc.tsoa.TSOA;
import org.usa.soc.util.Commons;
import utils.Utils;

public class TestConvergence {

    private void TestFunction(ObjectiveFunction f, int p) throws Exception {
        TSOA t = new TSOA(f, p, 1000, f.getNumberOfDimensions(), f.getMin(), f.getMax(),true, 0.7, 2);
        t.initialize();
        t.addStepAction(new Action() {
            @Override
            public void performAction(Vector best, Double bestValue, int step) {
                StringBuilder sb = new StringBuilder();
                sb.append(step);
                sb.append(",");
                sb.append(bestValue);

                for(int i=0; i< p ;i++){
                    sb.append(",");
                    sb.append(t.getDataPoints()[0][i]);
                    sb.append(",");
                    sb.append(t.getDataPoints()[1][i]);
                }
                sb.append("\n");
                Utils.writeToFile("data/convergence/"+f.getClass().getSimpleName()+".csv", sb.toString());
            }
        });
        t.runOptimizer();
    }

    @Test
    public void testF1() throws Exception {
        TestFunction(new Function1(2),30);
        TestFunction(new Function2(2),30);
        TestFunction(new Function3(2),30);

        TestFunction(new Function8(2),30);
        TestFunction(new Function9(2),30);
        TestFunction(new Function10(2),30);

        TestFunction(new Function16(2),30);
        TestFunction(new Function17(2),30);
        TestFunction(new Function18(2),30);

        TestFunction(new Function22(),30);
        TestFunction(new Function24(),30);
        TestFunction(new Function25(),30);
    }
}