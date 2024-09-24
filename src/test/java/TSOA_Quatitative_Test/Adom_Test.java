package TSOA_Quatitative_Test;

import examples.si.AlgorithmFactory;
import examples.si.benchmarks.DynamicUnimodalObjectiveFunctions.Function1;
import org.junit.jupiter.api.Test;
import org.usa.soc.si.SIAlgorithm;
import utils.Utils;

import java.util.Date;

public class Adom_Test {
    int n = 30;
    int p = 30;
    int steps = 1000;

    @Test
    public void testMFA() {
        try {
            SIAlgorithm algo = new AlgorithmFactory(13, new Function1(n)).getAlgorithm(steps, p);
            algo.initialize();
            algo.run();
            System.out.println(algo.getBestDoubleValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
