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
public class C1C2Analysis {

    public void execute(ObjectiveFunction fn, String name, String aka,  int kt, double min_c,double max_c, double ict) throws Exception {

        Document document = new Document();
        document.append("function_index", fn.getClass().getSimpleName());
        document.append("function_full_name", name);
        document.append("function_name", aka);

        List<List<List<Double>>> c1 = new ArrayList<>();
        for(double i = min_c; i< max_c; i+=ict){
            List<List<Double>> c2 = new ArrayList<>();
            for(double j = min_c; j< max_c; j+=ict){
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
                            i,
                            j
                    );
                    algo.initialize();
                    algo.run();
                    data.add(algo.getBestDoubleValue());
                    System.out.println(aka + ": C1:"+i+", C2:"+j+", data: "+algo.getBestDoubleValue()+", size: "+data.size());
                }
                c2.add(data);
            }
            c1.add(c2);
        }

        document.append("data", c1);
        ResultAggrigator.getInstance().updateDocument(document, "sensitivity_analysis");
    }

    @Test
    public void testUniModalSeperable() throws Exception {
        execute(new ChungReynoldsSquares().updateDimensions(10), "Chung Reynolds Squares", "F1", 3, 0.1, 3.0, 0.2);
        execute(new PowellSumFunction().updateDimensions(10), "Powell Sum", "F2", 3, 0.1, 3.0, 0.2);
        execute(new QuarticFunction().updateDimensions(10), "Quartic", "F3", 3, 0.1, 3.0, 0.2);
        execute(new SphereFunction().updateDimensions(10), "Sphere", "F8", 3, 0.1, 3.0, 0.2);
    }

    @Test
    public void testUniModalNonSeperable() throws Exception {
        execute(new DixonPriceFunction().updateDimensions(10), "Dixon Price", "F11", 3, 0.1, 3.0, 0.2);
        execute(new Schwefel22Function(10).updateDimensions(10), "Schwefel 2.22", "F14", 3, 0.1, 3.0, 0.2);
        execute(new ZakharovFunction().updateDimensions(10), "Zakharov", "F16", 3, 0.1, 3.0, 0.2);
        execute(new RosenbrockFunction().updateDimensions(10), "Rosenbrock", "F18", 3, 0.1, 3.0, 0.2);
    }

    @Test
    public void testMultiModalSeperable() throws Exception {
        execute(new Alpine1Function().updateDimensions(10), "Alpine1", "F20", 3, 0.1, 3.0, 0.2);
        execute(new CsendesFunction().updateDimensions(10), "Csendes", "F23", 3, 0.1, 3.0, 0.2);
        execute(new RastriginFunction().updateDimensions(10), "Rastrigin", "F27", 3, 0.1, 3.0, 0.2);
        execute(new Schwefel226(10).updateDimensions(10), "Schwefel226", "F28", 3, 0.1, 3.0, 0.2);
    }

    @Test
    public void testMultiModalMNonSeperable() throws Exception {
        execute(new AckleysFunction().updateDimensions(10), "Ackleys", "F10", 3, 0.1, 3.0, 0.2);
        execute(new GriewanktFunction(10).updateDimensions(10), "Griewankt", "F35", 3, 0.1, 3.0, 0.2);
        execute(new GeneralizedRosenbrok(10).updateDimensions(10), "Generalized Rosenbrok", "F36", 3, 0.1, 3.0, 0.2);
        execute(new StyblinskiTangFunction().updateDimensions(10), "Styblinski-Tang", "F37", 3, 0.1, 3.0, 0.2);
    }

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
                    System.out.println(": C1:"+i+", C2:"+j+", data: "+algo.getBestDoubleValue()+", size: "+data.size());
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

    @Test
    public void testTSOAMichalewicz() throws Exception {
        ObjectiveFunction fn = new RastriginFunction().updateDimensions(10);
        Document document = new Document();
        document.append("function_index", fn.getClass().getSimpleName());
        document.append("function_full_name", "Rastrigin");
        document.append("function_name", "F27");

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
