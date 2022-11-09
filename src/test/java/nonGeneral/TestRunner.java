package nonGeneral;

/*
Settings
 */

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.nonGeneral.classical.multimodal.nonseparable.*;
import org.usa.soc.benchmarks.nonGeneral.classical.multimodal.separable.*;
import org.usa.soc.benchmarks.nonGeneral.classical.unimodal.nonseparable.*;
import org.usa.soc.benchmarks.nonGeneral.classical.unimodal.separable.*;
import org.usa.soc.core.Action;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import ui.AlgoStore;

import java.awt.*;
import java.net.URI;
import java.nio.file.Files;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

public class TestRunner {

    private static final int REPEATER = 5;
    private static final int AGENT_COUNT = 1000;
    private static final int STEPS_COUNT = 1000;
    private static final int ALGO_INDEX = 2;

    private static final int START = 0, END =8;
    private static final ObjectiveFunction OBJECTIVE_FUNCTION = new org.usa.soc.benchmarks.nonGeneral.classical.unimodal.nonseparable.DixonPriceFunction();
    private static final String RESULT_FILE = "data_1000/";

    public Algorithm getAlgorithm(){
        return new AlgoStore(ALGO_INDEX, OBJECTIVE_FUNCTION).getAlgorithm(STEPS_COUNT, AGENT_COUNT);
    }


    private String RunTest(int index, ObjectiveFunction fn, String extra){

        Algorithm algorithm = getAlgorithm(index, fn);

        if(algorithm == null){
            return "";
        }

        double[] meanBestValueTrial = new double[STEPS_COUNT];
        double[] meanMeanBestValueTrial = new double[STEPS_COUNT];
        double[] meanConvergence = new double[STEPS_COUNT];
        double meanBestValue =0;
        long meanExecutionTime =0;
        double std = 0;
        double fraction = STEPS_COUNT/100;
        double[] bestValuesArray = new double[REPEATER];
        String filename = RESULT_FILE+"logs/"+System.currentTimeMillis() + ".csv";
        Path p = createFile(filename);
        appendToFile(p, algorithm.getClass().getSimpleName() + ","+ algorithm.getFunction().getClass().getSimpleName(), true);

        for(int i=0; i<REPEATER; i++){
            Algorithm finalAlgorithm = getAlgorithm(index,fn);
            System.out.println();
            int finalI = i;
            finalAlgorithm.addStepAction(new Action() {
                @Override
                public void performAction(Vector best, Double bestValue, int step) {

                    if((step % fraction) == 0){
                        System.out.print("\r [("+ finalI +") "+ finalAlgorithm.getClass().getSimpleName()+" - "+ finalAlgorithm.getFunction().getClass().getSimpleName()+"]:["+ Mathamatics.round(bestValue, 3) +"] ["+step/fraction+"%] "  + generate(() -> "#").limit((long)(step/fraction)).collect(joining()));
                    }
                    if(step >1) {
                        meanBestValueTrial[step-2] += bestValue;
                        meanConvergence[step - 2] += finalAlgorithm.getConvergenceValue();
                        meanMeanBestValueTrial[step - 2] += finalAlgorithm.getMeanBestValue();
                    }
                }
            });
            finalAlgorithm.initialize();
            finalAlgorithm.runOptimizer();
            meanBestValue += finalAlgorithm.getBestDoubleValue();
            bestValuesArray[i] = finalAlgorithm.getBestDoubleValue();
            meanExecutionTime += finalAlgorithm.getNanoDuration();
        }

        meanBestValue /= REPEATER;
        meanExecutionTime /= REPEATER;
        std = new StandardDeviation().evaluate(bestValuesArray, meanBestValue);
        appendToFile(p, "\nMean Best Value: ," + meanBestValue, false);
        appendToFile(p, "Mean Execution Time: (ms): ,"+ TimeUnit.MILLISECONDS.convert(meanExecutionTime, TimeUnit.NANOSECONDS), false);
        appendToFile(p, "MBV, MMBV, MC\n", false);

        for(int j=0;j< algorithm.getStepsCount();j++){
            meanBestValueTrial[j] /= REPEATER;
            meanMeanBestValueTrial[j] /= REPEATER;
            meanConvergence[j] /= REPEATER;
            appendToFile(p, "\n"+meanBestValueTrial[j] +","+meanMeanBestValueTrial[j] +","+meanConvergence[j], false);
        }

        // find effective time
        int effectiveStep = algorithm.getStepsCount();

        for(int j=algorithm.getStepsCount()-1;j>0;j--){
            if(meanBestValueTrial[j-1] - algorithm.getFunction().getExpectedBestValue() <= 0.1){
                effectiveStep = j;
                break;
            }
        }

        System.out.println();

        StringBuffer sb = new StringBuffer();
        sb.append(new Date().toString()).append(',');
        sb.append(extra).append(',');
        sb.append(algorithm.getClass().getSimpleName()).append(',');
        sb.append(algorithm.getFunction().getClass().getSimpleName()).append(',');
        sb.append(algorithm.getFunction().getNumberOfDimensions()).append(',');
        sb.append(AGENT_COUNT).append(',');
        sb.append(algorithm.getStepsCount()).append(',');
        sb.append(algorithm.getFunction().getExpectedBestValue()).append(',');
        sb.append(meanBestValue).append(',');
        sb.append(std).append(',');
        sb.append(effectiveStep).append(',');
        sb.append(TimeUnit.MILLISECONDS.convert(meanExecutionTime, TimeUnit.NANOSECONDS)).append(',');
        sb.append(Arrays.toString(bestValuesArray));
        sb.append(filename).append(',');

        sb.append('\n');

        Path r = createResultFile(algorithm.getClass().getSimpleName());
        appendToFile(r, sb.toString(), true);

        return sb.toString();
    }

    private void runC(List<ObjectiveFunction> fns, int i, String e, Path p){
        for (ObjectiveFunction fn: fns) {
            try{
                new TestRunner().RunTest(i,fn,e);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    static  int ls =0;

    public void runT(
            int index,
            List<ObjectiveFunction> a,
            List<ObjectiveFunction> b,
            List<ObjectiveFunction> c,
            List<ObjectiveFunction> d,
            CountDownLatch latch

    ) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println( " ["+index+"] Started");
                runC(a, index, "Multi Modal - Non Separable", null);
                runC(b, index, "Multi Modal - Separable", null);
                runC(c, index, "Uni Modal - Non Separable", null);
                runC(d, index, "Uni Modal - Separable", null);
                latch.countDown();
            }
        });
        Thread.sleep(1000);
        t.start();
    }

    public static void main(String[] args) throws InterruptedException {

        // list of Objective Functions
        List<ObjectiveFunction> multimodalNonSeparableFunctionList = Arrays.asList(
                new AckleysFunction(),
                new ColvilleFunction(),
                new CrossInTrayFunction(),
                new GoldsteinPrice(),
                new McCormickFunction(),
                new ThreeHumpCamelFunction(),
                new ZakharovFunction()
        );

        List<ObjectiveFunction> multimodalSeparableFunctionList = Arrays.asList(
                new Alpine1Function(),
                new BohachevskFunction(),
                new Bukin4Function(),
                new CsendesFunction(),
                new Debfunction(),
                new EasomFunction(),
                new SphereFunction()
        );

        List<ObjectiveFunction> unimodalNonSeparableFunctionList = Arrays.asList(
                new BealeFunction(),
                new BoothsFunction(),
                new DixonPriceFunction(),
                new MatyasFunction(),
                new SchafferFunction(),
                new Schwefel12Function(),
                new Schwefel22Function()
        );

        List<ObjectiveFunction> unimodalSeparableFunctionList = Arrays.asList(
                new ChungReynoldsSquares(),
                new PowellSumFunction(),
                new QuarticFunction(),
                new SchumerSteiglitzFunction(),
                new StepFunction(),
                new StepintFunction(),
                new SumSquares()
        );

        // start log file writer

        CountDownLatch latch = new CountDownLatch(3);

        new TestRunner().runT(0, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch);
        new TestRunner().runT(1, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch);
        new TestRunner().runT(2, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch);
        latch.await();

        CountDownLatch latch1 = new CountDownLatch(3);

        new TestRunner().runT(3, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch1);
        new TestRunner().runT(4, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch1);
        new TestRunner().runT(5, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch1);

        latch.await();

        CountDownLatch latch2 = new CountDownLatch(3);

        new TestRunner().runT(6, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch2);
        new TestRunner().runT(7, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch2);
        new TestRunner().runT(8, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch2);

        latch.await();

        CountDownLatch latch3 = new CountDownLatch(3);

        new TestRunner().runT(9, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch3);
        new TestRunner().runT(10, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch3);
        new TestRunner().runT(11, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch3);

        latch.await();

        CountDownLatch latch4 = new CountDownLatch(2);

        new TestRunner().runT(12, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch4);
        new TestRunner().runT(13, multimodalNonSeparableFunctionList, multimodalSeparableFunctionList, unimodalNonSeparableFunctionList, unimodalSeparableFunctionList, latch4);

        latch.await();

        for(ls=5;ls<6;ls++){

            //new TestRunner().runC(multimodalNonSeparableFunctionList, ls, "Multi Modal - Non Separable", null);
            //new TestRunner().runC(multimodalSeparableFunctionList, ls, "Multi Modal - Separable", null);
            //new TestRunner().runC(unimodalNonSeparableFunctionList, ls, "Uni Modal - Non Separable", null);
            //new TestRunner().runC(unimodalSeparableFunctionList, ls, "Uni Modal - Separable", null);
        }
    }

    private static Algorithm getAlgorithm(int index,ObjectiveFunction fn) {
        AlgorithmStore algorithmStore = new AlgorithmStore();

        switch (index){
            case 0: return algorithmStore.getPSO(fn, AGENT_COUNT, STEPS_COUNT);
            case 1: return algorithmStore.getCSO(fn, AGENT_COUNT, STEPS_COUNT);
            case 2: return algorithmStore.getGSO(fn, AGENT_COUNT, STEPS_COUNT);
            case 3: return algorithmStore.getWSO(fn, AGENT_COUNT, STEPS_COUNT);
            case 4: return algorithmStore.getCS(fn, AGENT_COUNT, STEPS_COUNT);
            case 5: return algorithmStore.getFA(fn, AGENT_COUNT, STEPS_COUNT);
            case 6: return algorithmStore.getABC(fn, AGENT_COUNT, STEPS_COUNT);
            case 7: return algorithmStore.getBA(fn, AGENT_COUNT, STEPS_COUNT);
            case 8: return algorithmStore.getTCO(fn, AGENT_COUNT, STEPS_COUNT);
            case 9: return algorithmStore.getGWO(fn, AGENT_COUNT, STEPS_COUNT);
            case 10: return algorithmStore.getMFA(fn, AGENT_COUNT, STEPS_COUNT);
            case 11: return algorithmStore.getALO(fn, AGENT_COUNT, STEPS_COUNT);
            case 12: return algorithmStore.getGEO(fn, AGENT_COUNT, STEPS_COUNT);
            case 13: return algorithmStore.getALSO(fn, AGENT_COUNT, STEPS_COUNT);
        }
        return null;
    }

    private static void appendToFile(Path path, String data, boolean f){
        if(data.isEmpty()){
            return;
        }
        try {
            if(f)
                System.out.println(data);
            Files.write(path, data.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path createResultFile(){
        return createResultFile(null);
    }

    private static Path createResultFile(String l){
        StringBuffer sb = new StringBuffer();
        sb.append("Date").append(',');
        sb.append("Type").append(',');
        sb.append("Algorithm").append(',');
        sb.append("Function").append(',');
        sb.append("Number of Dimensions").append(',');
        sb.append("Agents Count").append(',');
        sb.append("Steps Count").append(',');
        sb.append("Expected Best Value").append(',');
        sb.append("Actual Mean Best Value").append(',');
        sb.append("STD value").append(',');
        sb.append("Effective Step").append(',');
        sb.append("Execution time").append(',');
        sb.append('\n');

        Path path = Paths.get(l == null ? (RESULT_FILE + "result.csv") : (RESULT_FILE + "result_"+l+".csv"));
        try {
            if(Files.exists(path)){
                return path;
            }
            Files.write(path, sb.toString().getBytes(), StandardOpenOption.CREATE);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    private static Path createFile(String filename){
        Path path = Paths.get(filename);
        File f = new File(RESULT_FILE+"/logs");
        try {
            if(!f.exists()){
                f.mkdirs();
            }
            if(Files.exists(path)){
                return path;
            }
            Files.createFile(path);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
