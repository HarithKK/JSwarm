package nonGeneral;

import org.junit.jupiter.api.Test;
import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.nonGeneral.classical.unimodal.separable.SumSquares;

public class TempTest {

    @Test
    public void tempTest(){
        Algorithm algorithm = new AlgorithmStore().getWSO(
                new SumSquares(), 5000, 1000
        );

        algorithm.initialize();
        algorithm.runOptimizer();

        System.out.println(algorithm.toString());
    }
}