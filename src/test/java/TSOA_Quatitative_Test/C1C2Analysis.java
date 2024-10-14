package TSOA_Quatitative_Test;

import examples.si.AlgorithmFactory;
import examples.si.algo.tsoa.TSOA;
import examples.si.benchmarks.DynamicMultiModalObjectiveFunctions.GriewanktFunction;
import examples.si.benchmarks.cec2005.ShiftedSphereFunction;
import examples.si.benchmarks.cec2014.RotatedHighConditionedEllipticFunction;
import examples.si.benchmarks.cec2017.HybridFunction1;
import examples.si.benchmarks.cec2022.HybridFunction2;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.ZakharovFunction;
import examples.si.benchmarks.nonGeneral.classical.multimodal.separable.Debfunction;
import examples.si.benchmarks.nonGeneral.classical.unimodal.nonseparable.DixonPriceFunction;
import examples.si.benchmarks.nonGeneral.classical.unimodal.separable.QuarticFunction;
import examples.si.benchmarks.singleObjective.RastriginFunction;
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
public class C1C2Analysis {


    @Test
    public void testTSOAQuartic() throws Exception {
        ObjectiveFunction fn = new QuarticFunction().updateDimensions(10);
        Document document = new Document();
        document.append("function_index", fn.getClass().getSimpleName());
        document.append("function_full_name", "Quartic");
        document.append("function_name", "F3");

        List<List<List<Double>>> c1 = new ArrayList<>();
        for(double i = 0.1; i< 3.0; i+=0.2){
            List<List<Double>> c2 = new ArrayList<>();
            for(double j = 0.1; j< 3.0; j+=0.2){
                List<Double> data = new ArrayList<>();
                for(int k=0; k< 5;k++){
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
                            i,
                            j
                    );
                    algo.initialize();
                    algo.run();
                    data.add(algo.getBestDoubleValue());
                    System.out.println("C1:"+i+",C2:"+j+",data: "+algo.getBestDoubleValue()+",size: "+data.size());
                }
                c2.add(data);
            }
            c1.add(c2);
        }
        document.append("data", c1);
        ResultAggrigator.getInstance().updateDocument(document);
    }

    @Test
    public void testTSOADixenPrice() throws Exception {
        ObjectiveFunction fn = new DixonPriceFunction().updateDimensions(10);
        Document document = new Document();
        document.append("function_index", fn.getClass().getSimpleName());
        document.append("function_full_name", "DixonPrice");
        document.append("function_name", "F11");

        List<List<List<Double>>> c1 = new ArrayList<>();
        for(double i = 0.1; i< 3.0; i+=0.2){
            List<List<Double>> c2 = new ArrayList<>();
            for(double j = 0.1; j< 3.0; j+=0.2){
                List<Double> data = new ArrayList<>();
                for(int k=0; k< 5;k++){
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
                            i,
                            j
                    );
                    algo.initialize();
                    algo.run();
                    data.add(algo.getBestDoubleValue());
                    System.out.println("C1:"+i+",C2:"+j+",data: "+algo.getBestDoubleValue()+",size: "+data.size());
                }
                c2.add(data);
            }
            c1.add(c2);
        }
        document.append("data", c1);
        ResultAggrigator.getInstance().updateDocument(document);
    }

    @Test
    public void testTSOADeb() throws Exception {
        ObjectiveFunction fn = new Debfunction().updateDimensions(10);
        Document document = new Document();
        document.append("function_index", fn.getClass().getSimpleName());
        document.append("function_full_name", "Deb");
        document.append("function_name", "F24");

        List<List<List<Double>>> c1 = new ArrayList<>();
        for(double i = 0.1; i< 3.0; i+=0.2){
            List<List<Double>> c2 = new ArrayList<>();
            for(double j = 0.1; j< 3.0; j+=0.2){
                List<Double> data = new ArrayList<>();
                for(int k=0; k< 5;k++){
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
                            i,
                            j
                    );
                    algo.initialize();
                    algo.run();
                    data.add(algo.getBestDoubleValue());
                    System.out.println("C1:"+i+",C2:"+j+",data: "+algo.getBestDoubleValue()+",size: "+data.size());
                }
                c2.add(data);
            }
            c1.add(c2);
        }
        document.append("data", c1);
        ResultAggrigator.getInstance().updateDocument(document);
    }

    @Test
    public void testTSOAGriewankt() throws Exception {
        ObjectiveFunction fn = new GriewanktFunction(10).updateDimensions(10);
        Document document = new Document();
        document.append("function_index", fn.getClass().getSimpleName());
        document.append("function_full_name", "Griewankt");
        document.append("function_name", "F8");

        List<List<List<Double>>> c1 = new ArrayList<>();
        for(double i = 0.1; i< 3.0; i+=0.2){
            List<List<Double>> c2 = new ArrayList<>();
            for(double j = 0.1; j< 3.0; j+=0.2){
                List<Double> data = new ArrayList<>();
                for(int k=0; k< 5;k++){
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
                            i,
                            j
                    );
                    algo.initialize();
                    algo.run();
                    data.add(algo.getBestDoubleValue());
                    System.out.println("C1:"+i+",C2:"+j+",data: "+algo.getBestDoubleValue()+",size: "+data.size());
                }
                c2.add(data);
            }
            c1.add(c2);
        }
        document.append("data", c1);
        ResultAggrigator.getInstance().updateDocument(document);
    }

}
