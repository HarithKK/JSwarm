package TSOA_Quatitative_Test;

import examples.si.algo.tsoa.TSOA;
import examples.si.benchmarks.DynamicMultiModalObjectiveFunctions.GriewanktFunction;
import examples.si.benchmarks.DynamicMultiModalObjectiveFunctions.Schwefel226;
import examples.si.benchmarks.DynamicUnimodalObjectiveFunctions.GeneralizedRosenbrok;
import examples.si.benchmarks.DynamicUnimodalObjectiveFunctions.Schwefel22Function;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.AckleysFunction;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.StyblinskiTangFunction;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.ZakharovFunction;
import examples.si.benchmarks.nonGeneral.classical.multimodal.separable.Alpine1Function;
import examples.si.benchmarks.nonGeneral.classical.multimodal.separable.CsendesFunction;
import examples.si.benchmarks.nonGeneral.classical.multimodal.separable.Debfunction;
import examples.si.benchmarks.nonGeneral.classical.unimodal.nonseparable.DixonPriceFunction;
import examples.si.benchmarks.nonGeneral.classical.unimodal.separable.PowellSumFunction;
import examples.si.benchmarks.nonGeneral.classical.unimodal.separable.QuarticFunction;
import examples.si.benchmarks.singleObjective.ChungReynoldsSquares;
import examples.si.benchmarks.singleObjective.RastriginFunction;
import examples.si.benchmarks.singleObjective.RosenbrockFunction;
import examples.si.benchmarks.singleObjective.SphereFunction;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.usa.soc.si.ObjectiveFunction;
import utils.ResultAggrigator;

import java.util.ArrayList;
import java.util.List;

@Execution(ExecutionMode.CONCURRENT)
public class SeedsCountAnalysis {

    double c1 = 2.0;
    double c2 =1.0;

    public void execute(ObjectiveFunction fn, String name, String aka, int minimumSeedsCount, int maximumSeedsCount, int increment, int kt) throws Exception {

        Document document = new Document();
        document.append("function_index", fn.getClass().getSimpleName());
        document.append("function_full_name", name);
        document.append("function_name", aka);

        List<List<Double>> concatData = new ArrayList<>();
        for(double j = minimumSeedsCount; j< maximumSeedsCount; j+=increment){
            List<Double> data = new ArrayList<>();
            for(int k=0; k< kt;k++){
                TSOA algo = new TSOA(
                        fn,
                        30,
                        100,
                        fn.getNumberOfDimensions(),
                        fn.getMin(),
                        fn.getMax(),
                        true,
                        10,
                        0.3,
                        1,
                        c1,
                        c2
                );
                algo.initialize();
                algo.run();
                data.add(algo.getBestDoubleValue());
                System.out.println(aka +", Seeds Count:"+j+", data: "+algo.getBestDoubleValue()+", size: "+data.size());
            }
            concatData.add(data);
        }

        document.append("data", concatData);
        ResultAggrigator.getInstance().updateDocument(document, "seeds_count_analysis");
    }

    @Test
    public void testUniModalSeperable() throws Exception {
        execute(new ChungReynoldsSquares().updateDimensions(10), "Chung Reynolds Squares", "F1", 10, 50, 10, 5);
        execute(new PowellSumFunction().updateDimensions(10), "Powell Sum", "F2", 10, 50, 10, 5);
        execute(new QuarticFunction().updateDimensions(10), "Quartic", "F3", 10, 50, 10, 5);
        execute(new SphereFunction().updateDimensions(10), "Sphere", "F8", 10, 50, 10, 5);
    }

    @Test
    public void testUniModalNonSeperable() throws Exception {
        execute(new DixonPriceFunction().updateDimensions(10), "Dixon Price", "F11", 10, 50, 10, 5);
        execute(new Schwefel22Function(10).updateDimensions(10), "Schwefel 2.22", "F14", 10, 50, 10, 5);
        execute(new ZakharovFunction().updateDimensions(10), "Zakharov", "F16", 10, 50, 10, 5);
        execute(new RosenbrockFunction().updateDimensions(10), "Rosenbrock", "F18", 10, 50, 10, 5);
    }

    @Test
    public void testMultiModalSeperable() throws Exception {
        execute(new Alpine1Function().updateDimensions(10), "Alpine1", "F20", 10, 50, 10, 5);
        execute(new CsendesFunction().updateDimensions(10), "Csendes", "F23", 10, 50, 10, 5);
        execute(new RastriginFunction().updateDimensions(10), "Rastrigin", "F27", 10, 50, 10, 5);
        execute(new Schwefel226(10).updateDimensions(10), "Schwefel226", "F28", 10, 50, 10, 5);
    }

    @Test
    public void testMultiModalMNonSeperable() throws Exception {
        execute(new AckleysFunction().updateDimensions(10), "Ackleys", "F10", 10, 50, 10, 5);
        execute(new GriewanktFunction(10).updateDimensions(10), "Griewankt", "F35", 10, 50, 10, 5);
        execute(new GeneralizedRosenbrok(10).updateDimensions(10), "Generalized Rosenbrok", "F36", 10, 50, 10, 5);
        execute(new StyblinskiTangFunction().updateDimensions(10), "Styblinski-Tang", "F37", 10, 50, 10, 5);
    }

}
