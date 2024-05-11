package org.usa.soc.view.si;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;
import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.usa.soc.core.Algorithm;
import org.usa.soc.core.exceptions.KillOptimizerException;
import org.usa.soc.core.action.Action;
import org.usa.soc.core.action.EmptyAction;
import org.usa.soc.core.ds.Vector;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class FunctionChartPlotter {

    private XYChart chart= null;

    private double xdata[], ydata[], xbest[], ybest[];

    private Algorithm algorithm;
    private JFrame frame;

    private EmptyAction action;

    private int time = 10, bestIndex;
    private int interval = 0;

    private boolean isExecute = false;

    public FunctionChartPlotter(String title, int w, int h){

        this.chart = new XYChartBuilder()
                .width(w).height(h).title(title).xAxisTitle("X").yAxisTitle("Y").build();

        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
        chart.getStyler().setMarkerSize(8);
    }

    public void setChart(Algorithm a){
        this.algorithm = a;
        double m = Math.max(a.getFunction().getMax()[0] - a.getFunction().getMin()[0], a.getFunction().getMax()[1] - a.getFunction().getMin()[1]);
        this.xdata = new double[(int)(m)+50];
        this.ydata = new double[(int)(m)+50];
        this.xbest = new double[100];
        this.ybest = new double[100];
        this.bestIndex = 0;

        chart.getStyler().setXAxisMin(a.getFunction().getMin()[0]);
        chart.getStyler().setYAxisMin(a.getFunction().getMin()[1]);

        chart.getStyler().setXAxisMax(a.getFunction().getMax()[0]);
        chart.getStyler().setYAxisMax(a.getFunction().getMax()[1]);

        try {
            chart.removeSeries("Agents");
            chart.removeSeries("Best");
            chart.removeSeries("Best Search Trial");
        }catch(Exception e){

        }
        XYSeries series = this.chart.addSeries("Agents", xdata, ydata);
        series.setMarker(SeriesMarkers.CIRCLE);
        XYSeries seriesb = this.chart.addSeries("Best Search Trial", xbest, ybest);
        seriesb.setMarker(SeriesMarkers.CROSS);
        seriesb.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        double [][]best_coords = new double[2][(int)(a.getFunction().getExpectedParameters().length/2)];

        for(int x = 0; x < a.getFunction().getExpectedParameters().length; x+=2){
            best_coords[0][x/2] = a.getFunction().getExpectedParameters()[x];
            best_coords[1][x/2] = a.getFunction().getExpectedParameters()[x+1];

            System.out.println(best_coords[0][x/2] +" : "+ best_coords[1][x/2]);
        }

        XYSeries series1 = this.chart.addSeries("Best", best_coords[0], best_coords[1]);
        series1.setMarker(SeriesMarkers.DIAMOND);
        series1.setMarkerColor(Color.RED);
    }

    public XYChart getChart(){
        return this.chart;
    }

    public void setAction(EmptyAction action){
        this.action = action;
    }

    public void display(){
        frame = new JFrame("Algorithm");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setAction(new EmptyAction() {
            @Override
            public void performAction(int step, double []d) {
                frame.repaint();
            }
        });
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    XChartPanel<Chart<?, ?>> chartPanel = new XChartPanel(chart);
                    frame.add(chartPanel);
                    frame.pack();
                    frame.setVisible(true);
                }
            });
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        } catch (InvocationTargetException var4) {
            var4.printStackTrace();
        }
    }

    public void execute(){
        if(isExecute){
            return;
        }
        setExecute(true);
        algorithm.initialize();
        algorithm.setInterval(interval);
        int step =0;
        double fraction = algorithm.getStepsCount()/100;
        algorithm.addStepAction(new Action() {
            @Override
            public void performAction(Vector best, Double bestValue, int step) {

                double[][] d = algorithm.getDataPoints();
                xdata = d[0];
                ydata = d[1];

                if(bestIndex < 0 || xbest[bestIndex] != best.getValue(0) || ybest[bestIndex] != best.getValue(1)){
                    if(bestIndex < xbest.length-1){
                        bestIndex++;
                    }
                    xbest[bestIndex] = best.getValue(0);
                    ybest[bestIndex] = best.getValue(1);
                }

                chart.updateXYSeries("Agents", xdata, ydata, null);
                chart.updateXYSeries("Best Search Trial", xbest, ybest, null);

                if((step % fraction) == 0){
                    System.out.print("\r ["+ Mathamatics.round(bestValue, 3) +"] ["+step/fraction+"%] "  + generate(() -> "#").limit((long)(step/fraction)).collect(joining()));
                }
                if(action != null)
                    action.performAction(step, step/fraction, bestValue);
                step = step +1;
            }
        });
        try {
            algorithm.runOptimizer();
        } catch (Exception e) {
            if(e instanceof KillOptimizerException){
                System.out.println("Optimizer was killed forcefully");
            }
        }
        setExecute(false);
        System.out.println("");
        System.out.println("Actual: "+algorithm.getBestDoubleValue() +" , Expected: "+algorithm.getFunction().getExpectedBestValue());
        System.out.println("Actual: "+algorithm.getGBest().toString()+" , Expected: "+ Arrays.toString(algorithm.getFunction().getExpectedParameters()));
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isExecute() {
        return isExecute;
    }

    public void setExecute(boolean execute) {
        isExecute = execute;
    }

    public void pause() {
        algorithm.pauseOptimizer();
    }

    public void resume() {
        algorithm.setInterval(interval);
        algorithm.resumeOptimizer();
    }

    public boolean isPaused() {
       return algorithm.isPaused();
    }

    public void stopOptimizer() {
        algorithm.stopOptimizer();
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}