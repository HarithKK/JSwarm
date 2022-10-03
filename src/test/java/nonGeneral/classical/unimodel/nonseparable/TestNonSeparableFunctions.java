package nonGeneral.classical.unimodel.nonseparable;

import org.junit.jupiter.api.Test;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.nonGeneral.classical.multimodal.nonseparable.ColvilleFunction;
import org.usa.soc.benchmarks.nonGeneral.classical.multimodal.nonseparable.ZakharovFunction;
import org.usa.soc.benchmarks.nonGeneral.classical.unimodal.nonseparable.*;
import org.usa.soc.benchmarks.nonGeneral.classical.unimodal.nonseparable.notincluded.RotatedEllipsisFunction;
import org.usa.soc.pso.PSO;

public class TestNonSeparableFunctions {

    @Test
    public void testBealeFunction(){
        ObjectiveFunction obj = new BealeFunction();
        PSO pso = new PSO(
                obj,
                2000,
                obj.getNumberOfDimensions(),
                2000,
                1.496180,
                1.496180,
                0.729844,
                obj.getMin(),
                obj.getMax(),
                true
        );
        pso.initialize();
        pso.runOptimizer();
        System.out.println(pso.toString());
    }

    @Test
    public void testRotatedEllipsisFunction(){
        ObjectiveFunction obj = new RotatedEllipsisFunction();
        PSO pso = new PSO(
                obj,
                2000,
                obj.getNumberOfDimensions(),
                3000,
                1.496180,
                1.496180,
                0.729844,
                obj.getMin(),
                obj.getMax(),
                true
        );
        pso.initialize();
        pso.runOptimizer();
        System.out.println(pso.toString());
    }

    @Test
    public void testMatyasFunction(){
        ObjectiveFunction obj = new MatyasFunction();
        PSO pso = new PSO(
                obj,
                2000,
                obj.getNumberOfDimensions(),
                2000,
                1.496180,
                1.496180,
                0.729844,
                obj.getMin(),
                obj.getMax(),
                true
        );
        pso.initialize();
        pso.runOptimizer();
        System.out.println(pso.toString());
    }

    @Test
    public void testColvilleFunction(){
        ObjectiveFunction obj = new ColvilleFunction();
        PSO pso = new PSO(
                obj,
                2000,
                obj.getNumberOfDimensions(),
                2000,
                1.496180,
                1.496180,
                0.729844,
                obj.getMin(),
                obj.getMax(),
                true
        );
        pso.initialize();
        pso.runOptimizer();
        System.out.println(pso.toString());
    }

    @Test
    public void testZakharovFunction(){
        ObjectiveFunction obj = new ZakharovFunction();
        PSO pso = new PSO(
                obj,
                2000,
                obj.getNumberOfDimensions(),
                2000,
                1.496180,
                1.496180,
                0.729844,
                obj.getMin(),
                obj.getMax(),
                true
        );
        pso.initialize();
        pso.runOptimizer();
        System.out.println(pso.toString());
    }

    @Test
    public void testSchwefel22Function(){
        ObjectiveFunction obj = new Schwefel22Function();
        PSO pso = new PSO(
                obj,
                2000,
                obj.getNumberOfDimensions(),
                7000,
                1.496180,
                1.496180,
                0.729844,
                obj.getMin(),
                obj.getMax(),
                true
        );
        pso.initialize();
        pso.runOptimizer();
        System.out.println(pso.toString());
    }

    @Test
    public void testSchwefel12Function(){
        ObjectiveFunction obj = new Schwefel12Function();
        PSO pso = new PSO(
                obj,
                2000,
                obj.getNumberOfDimensions(),
                3000,
                1.496180,
                1.496180,
                0.729844,
                obj.getMin(),
                obj.getMax(),
                true
        );
        pso.initialize();
        pso.runOptimizer();
        System.out.println(pso.toString());
    }

    @Test
    public void testDixonPrice(){
        ObjectiveFunction obj = new DixonPriceFunction();
        PSO pso = new PSO(
                obj,
                3000,
                obj.getNumberOfDimensions(),
                10000,
                1.496180,
                1.496180,
                0.729844,
                obj.getMin(),
                obj.getMax(),
                true
        );
        pso.initialize();
        pso.runOptimizer();
        System.out.println(pso.toString());
    }

    @Test
    public void testSchafferFunction(){
        ObjectiveFunction obj = new SchafferFunction();
        PSO pso = new PSO(
                obj,
                2000,
                obj.getNumberOfDimensions(),
                3000,
                1.496180,
                1.496180,
                0.729844,
                obj.getMin(),
                obj.getMax(),
                true
        );
        pso.initialize();
        pso.runOptimizer();
        System.out.println(pso.toString());
    }


}
