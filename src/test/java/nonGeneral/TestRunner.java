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

    private static final int RIP_DIS = 10;
    private static final ObjectiveFunction OBJECTIVE_FUNCTION = new org.usa.soc.benchmarks.nonGeneral.classical.unimodal.nonseparable.DixonPriceFunction();

    public Algorithm getAlgorithm(){
        return new AlgoStore(ALGO_INDEX, OBJECTIVE_FUNCTION).getAlgorithm(STEPS_COUNT, AGENT_COUNT);
    }

    private  static  Algorithm algorithm = null;

    private String RunTest(int index, ObjectiveFunction fn, String extra){
        double[] meanBestValueTrial = new double[STEPS_COUNT];
        double[] meanMeanBestValueTrial = new double[STEPS_COUNT];
        double[] meanConvergence = new double[STEPS_COUNT];
        double meanBestValue =0;
        long meanExecutionTime =0;
        double std = 0;
        List<String[]> dataLines = new ArrayList<>();
        double fraction = STEPS_COUNT/100;
        double[] bestValuesArray = new double[REPEATER];
        String filename = "data/"+System.currentTimeMillis() + ".csv";
        Path p = createFile(filename);
        algorithm = getAlgorithm(index, fn);
        appendToFile(p, algorithm.getClass().getSimpleName() + ","+ algorithm.getFunction().getClass().getSimpleName());

        for(int i=0; i<REPEATER; i++){
            algorithm = getAlgorithm(index,fn);
            algorithm.initialize();
            System.out.println();
            algorithm.addStepAction(new Action() {
                @Override
                public void performAction(Vector best, Double bestValue, int step) {

                    if((step % fraction) == 0){
                        System.out.print("\r ["+ Mathamatics.round(bestValue, 3) +"] ["+step/fraction+"%] "  + generate(() -> "#").limit((long)(step/fraction)).collect(joining()));
                    }
                    if(step >1) {
                        meanBestValueTrial[step-2] += bestValue;
                        meanConvergence[step - 2] += algorithm.getConvergenceValue();
                        meanMeanBestValueTrial[step - 2] += algorithm.getMeanBestValue();
                    }
                }
            });
            algorithm.runOptimizer();
            meanBestValue += algorithm.getBestDoubleValue();
            bestValuesArray[i] = algorithm.getBestDoubleValue();
            meanExecutionTime += algorithm.getNanoDuration();
        }

        meanBestValue /= REPEATER;
        meanExecutionTime /= REPEATER;
        std = new StandardDeviation().evaluate(bestValuesArray, meanBestValue);
        appendToFile(p, "Mean Best Value: ," + meanBestValue);
        appendToFile(p, "Mean Execution Time: (ms): ,"+ TimeUnit.MILLISECONDS.convert(meanExecutionTime, TimeUnit.NANOSECONDS));
        appendToFile(p, "MBV, MMBV, MC");

        for(int j=0;j< algorithm.getStepsCount();j++){
            meanBestValueTrial[j] /= REPEATER;
            meanMeanBestValueTrial[j] /= REPEATER;
            meanConvergence[j] /= REPEATER;
            appendToFile(p, meanBestValueTrial[j] +","+meanMeanBestValueTrial[j] +","+meanConvergence[j]);
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
        sb.append(TimeUnit.MILLISECONDS.convert(meanExecutionTime, TimeUnit.NANOSECONDS)).append(',');
        sb.append(filename).append(',');

        sb.append('\n');

        return sb.toString();

//        List<XYChart> charts = new ArrayList<XYChart>();
//
//        XYChart chart = new XYChartBuilder().xAxisTitle("step").yAxisTitle("best value").width(2000).height(400).build();
//        XYSeries series = chart.addSeries("Best Value", null, meanBestValueTrial);
//        series.setMarker(SeriesMarkers.NONE);
//        charts.add(chart);
//
//        XYChart chart1 = new XYChartBuilder().xAxisTitle("step").yAxisTitle("mean best value").width(600).height(400).build();
//        XYSeries series1 = chart1.addSeries("Mean Best Value", null, meanMeanBestValueTrial);
//        series1.setMarker(SeriesMarkers.NONE);
//        charts.add(chart1);
//
//        XYChart chart2 = new XYChartBuilder().xAxisTitle("step").yAxisTitle("convergence value").width(600).height(400).build();
//        XYSeries series2 = chart2.addSeries("Convergence Value", null, meanConvergence);
//        series2.setMarker(SeriesMarkers.NONE);
//        charts.add(chart2);
//
//        new SwingWrapper<XYChart>(charts).displayChartMatrix();
    }

    public static void main(String[] args) {

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
        Path p = createResultFile();

        for(int i=0;i<12;i++){
            for (ObjectiveFunction fn: multimodalNonSeparableFunctionList) {
                String s = new TestRunner().RunTest(i,fn,"Multi Modal - Non Separable");
                appendToFile(p, s);
            }

            for (ObjectiveFunction fn: multimodalSeparableFunctionList) {
                String s = new TestRunner().RunTest(i,fn,"Multi Modal - Separable");
                appendToFile(p, s);
            }

            for (ObjectiveFunction fn: unimodalNonSeparableFunctionList) {
                String s = new TestRunner().RunTest(i,fn,"Uni Modal - Non Separable");
                appendToFile(p, s);
            }

            for (ObjectiveFunction fn: unimodalSeparableFunctionList) {
                String s = new TestRunner().RunTest(i,fn,"Uni Modal - Separable");
                appendToFile(p, s);
            }
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
        }
        return null;
    }

    private static void appendToFile(Path path, String data){
        try {
            System.out.println(data);
            Files.write(path, data.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path createResultFile(){
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
        sb.append("Execution time").append(',');
        sb.append('\n');

        Path path = Paths.get("data/result.csv");
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
        try {
            if(Files.exists(path)){
                return path;
            }
            Files.write(path, (new Date().toString()+"\n").getBytes(), StandardOpenOption.CREATE);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
