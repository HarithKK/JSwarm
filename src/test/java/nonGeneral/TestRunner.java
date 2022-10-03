package nonGeneral;

/*
Settings
 */

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.core.Action;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;
import ui.AlgoStore;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

public class TestRunner {

    private static final int REPEATER = 1;
    private static final int AGENT_COUNT = 1000;
    private static final int STEPS_COUNT = 2000;
    private static final int ALGO_INDEX = 0;
    private static final int RIP_DIS = 10;
    private static final ObjectiveFunction OBJECTIVE_FUNCTION = new org.usa.soc.benchmarks.nonGeneral.classical.multimodal.nonseparable.AckleysFunction();

    public Algorithm getAlgorithm(){
        return new AlgoStore(ALGO_INDEX, OBJECTIVE_FUNCTION).getAlgorithm(STEPS_COUNT, AGENT_COUNT);
    }

    private static Algorithm algorithm = null;
    private static double[] meanBestValueTrial, meanMeanBestValueTrial, meanConvergence = null;
    private static double meanBestValue = 0;
    private static long meanExecutionTime = 0;

    public static void main(String[] args) {

        meanBestValueTrial = new double[STEPS_COUNT];
        meanMeanBestValueTrial = new double[STEPS_COUNT];
        meanConvergence = new double[STEPS_COUNT];
        meanBestValue =0;
        meanExecutionTime =0;
        List<String[]> dataLines = new ArrayList<>();
        double fraction = STEPS_COUNT/100;
        for(int i=0; i<REPEATER; i++){
            algorithm = new TestRunner().getAlgorithm();
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
            meanExecutionTime += algorithm.getNanoDuration();
        }

        dataLines.add(new String[]{algorithm.getClass().getSimpleName(), algorithm.getFunction().getClass().getSimpleName()});
        meanBestValue /= REPEATER;
        meanExecutionTime /= REPEATER;
        dataLines.add(new String[]{"Mean Best Value: ", String.valueOf(meanBestValue)});
        dataLines.add(new String[]{"Mean Execution Time: (ms): ", String.valueOf(meanExecutionTime)});
        dataLines.add(new String[]{"MBV", "MMBV", "MC"});
        for(int j=0;j< algorithm.getStepsCount();j++){
            meanBestValueTrial[j] /= REPEATER;
            meanMeanBestValueTrial[j] /= REPEATER;
            meanConvergence[j] /= REPEATER;
            dataLines.add(new String[]{String.valueOf(meanBestValueTrial[j]), String.valueOf(meanMeanBestValueTrial[j]), String.valueOf(meanConvergence[j])});
        }
        System.out.println();
        System.out.println("Mean Best Value: "+ meanBestValue);
        System.out.println("Mean Execution Time: (ms)"+ TimeUnit.MILLISECONDS.convert(meanExecutionTime, TimeUnit.NANOSECONDS));

        writeToCsv(dataLines);

        List<XYChart> charts = new ArrayList<XYChart>();

        XYChart chart = new XYChartBuilder().xAxisTitle("step").yAxisTitle("best value").width(2000).height(400).build();
        XYSeries series = chart.addSeries("Best Value", null, meanBestValueTrial);
        series.setMarker(SeriesMarkers.NONE);
        charts.add(chart);

        XYChart chart1 = new XYChartBuilder().xAxisTitle("step").yAxisTitle("mean best value").width(600).height(400).build();
        XYSeries series1 = chart1.addSeries("Mean Best Value", null, meanMeanBestValueTrial);
        series1.setMarker(SeriesMarkers.NONE);
        charts.add(chart1);

        XYChart chart2 = new XYChartBuilder().xAxisTitle("step").yAxisTitle("convergence value").width(600).height(400).build();
        XYSeries series2 = chart2.addSeries("Convergence Value", null, meanConvergence);
        series2.setMarker(SeriesMarkers.NONE);
        charts.add(chart2);

        new SwingWrapper<XYChart>(charts).displayChartMatrix();

    }

    private static void writeToCsv(List<String[]> dataLines) {
        File f = new File("data");
        if(!f.exists())
            f.mkdir();
        File csvOutputFile = new File("data/"+System.currentTimeMillis() + ".csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(dataLine -> Stream.of(dataLine)
                            .map(data ->{
                                String escapedData = data.replaceAll("\\R", " ");
                                if (data.contains(",") || data.contains("\"") || data.contains("'")) {
                                    data = data.replace("\"", "\"\"");
                                    escapedData = "\"" + data + "\"";
                                }
                                return escapedData;
                            })
                            .collect(Collectors.joining(",")))
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
