package org.usa.soc.si.view;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;
import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.usa.soc.core.ds.SeriesDataObject;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.multiagent.StepCompleted;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.core.exceptions.KillOptimizerException;
import org.usa.soc.core.action.StepAction;
import org.usa.soc.core.action.EmptyAction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Mathamatics;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

public class FunctionChartPlotter extends JFrame {

    private XYChart chart= null;

    private double xbest[], ybest[];

    private SIAlgorithm siAlgorithm;

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

        this.setTitle(title);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setAction(new EmptyAction() {
            @Override
            public void performAction(int step, double... values) {
                repaint();
            }
        });

        add(new XChartPanel(chart));
        pack();
    }

    public void setChart(SIAlgorithm a){
        this.siAlgorithm = a;
        double m = Math.max(a.getFunction().getMax()[0] - a.getFunction().getMin()[0], a.getFunction().getMax()[1] - a.getFunction().getMin()[1]);
        this.xbest = new double[100];
        this.ybest = new double[100];
        this.bestIndex = 0;

        chart.getStyler().setXAxisMin(a.getFunction().getMin()[0]);
        chart.getStyler().setYAxisMin(a.getFunction().getMin()[1]);

        chart.getStyler().setXAxisMax(a.getFunction().getMax()[0]);
        chart.getStyler().setYAxisMax(a.getFunction().getMax()[1]);

        var keys = chart.getSeriesMap().keySet().toArray();
        for(Object cs : keys){
            chart.removeSeries(String.valueOf(cs));
        }
        for(AgentGroup ag: a.getAgentsMap().values()){
            SeriesDataObject obj = ag.getLocations();
            if(ag.getAgentsCount() > 0) {
                XYSeries series = this.chart.addSeries(ag.name, obj.getX(), obj.getY());
                series.setMarker(ag.getMarker());
                series.setMarkerColor(ag.getMarkerColor());
            }else{
                XYSeries series = this.chart.addSeries(ag.name, new double[100], new double[100]);
                series.setMarker(ag.getMarker());
                series.setMarkerColor(ag.getMarkerColor());
            }
        }
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
        series1.setMarkerColor(Color.CYAN);
    }

    public XYChart getChart(){
        return this.chart;
    }

    public void setAction(EmptyAction action){
        this.action = action;
    }

    public void display(){
        setVisible(true);
    }

    public void execute(){
        if(isExecute){
            return;
        }
        setExecute(true);
        siAlgorithm.initialize();
        siAlgorithm.setInterval(interval);
        int step =0;
        double fraction = siAlgorithm.getStepsCount()/100;
        siAlgorithm.setStepCompleted(new StepCompleted() {
            @Override
            public void performAction(long step) {
                if(bestIndex < 0 || xbest[bestIndex] != siAlgorithm.getGBest().getValue(0) || ybest[bestIndex] != siAlgorithm.getGBest().getValue(1)){
                    if(bestIndex < xbest.length-1){
                        bestIndex++;
                    }
                    xbest[bestIndex] = siAlgorithm.getGBest().getValue(0);
                    ybest[bestIndex] = siAlgorithm.getGBest().getValue(1);
                }

                for(AgentGroup ag: siAlgorithm.getAgentsMap().values()){
                    SeriesDataObject obj = ag.getLocations();
                    if(ag.getAgentsCount() > 0) {
                        chart.updateXYSeries(ag.name, obj.getX(), obj.getY(), null);
                    }
                }
                chart.updateXYSeries("Best Search Trial", xbest, ybest, null);

                if((step % fraction) == 0){
                    System.out.print("\r ["+ Mathamatics.round(siAlgorithm.getBestDoubleValue(), 3) +"] ["+step/fraction+"%] "  + generate(() -> "#").limit((long)(step/fraction)).collect(joining()));
                }
                if(action != null)
                    action.performAction((int)step, step/fraction, siAlgorithm.getBestValue());
                step = step +1;
            }
        });
        try {
            siAlgorithm.run(siAlgorithm.getStepsCount());
        } catch (Exception e) {
            if(e instanceof KillOptimizerException){
                System.out.println("Optimizer was killed forcefully");
            }else{
                e.printStackTrace();
            }
        }
        setExecute(false);
        System.out.println("");
        System.out.println("Actual: "+ siAlgorithm.getBestDoubleValue() +" , Expected: "+ siAlgorithm.getFunction().getExpectedBestValue());
        System.out.println("Actual: "+ siAlgorithm.getGBest().toString()+" , Expected: "+ Arrays.toString(siAlgorithm.getFunction().getExpectedParameters()));
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
        siAlgorithm.pauseOptimizer();
    }

    public void resume() {
        siAlgorithm.setInterval(interval);
        siAlgorithm.resumeOptimizer();
    }

    public boolean isPaused() {
       return siAlgorithm.isPaused();
    }

    public void stopOptimizer() {
        siAlgorithm.stopOptimizer();
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
