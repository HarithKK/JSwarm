package nonGeneral;

import org.junit.jupiter.api.Test;
import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.nonGeneral.classical.multimodal.nonseparable.GoldsteinPrice;
import org.usa.soc.benchmarks.nonGeneral.classical.unimodal.separable.SumSquares;

public class TempTest {

    @Test
    public void tempTest(){
        Algorithm algorithm = new AlgorithmStore().getPSO(
                new GoldsteinPrice(), 1000, 1000
        );

        algorithm.initialize();
        algorithm.runOptimizer();

        System.out.println(algorithm.toString());
    }
}
