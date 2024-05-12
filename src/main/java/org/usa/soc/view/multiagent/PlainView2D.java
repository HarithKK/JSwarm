package org.usa.soc.view.multiagent;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler;
import org.usa.soc.core.Algorithm;
import org.usa.soc.core.Flag;
import org.usa.soc.core.action.Action;
import org.usa.soc.core.action.StepAction;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.SeriesData;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Logger;
import org.usa.soc.util.Mathamatics;

import javax.swing.*;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class PlainView2D {

    private XYChart chart= null;
    private Algorithm algo = null;

    private int interval = 0;
    private Flag isExecuting;

    private Action action;

    public PlainView2D(String title, int w, int h){
        this.initComponents(title, w, h);
    }

    public PlainView2D(String title, int w, int h, Algorithm algorithm){
        this.initComponents(title, w, h);
        this.initAlgorithm(algorithm, algorithm.getFunction().getMin()[0], algorithm.getFunction().getMin()[1], algorithm.getFunction().getMax()[0], algorithm.getFunction().getMax()[1]);
    }

    public PlainView2D(String title, int w, int h, Algorithm algorithm, Margins m){
        this.initComponents(title, w, h);
        this.initAlgorithm(algorithm, m.xMin, m.yMin, m.xMax, m.yMax);
    }

    private void initComponents(String title, int w, int h){

        this.isExecuting.unset();
        this.chart = new XYChartBuilder()
                .width(w).height(h).title(title).xAxisTitle("X").yAxisTitle("Y").build();

        this.getChart().getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        this.getChart().getStyler().setChartTitleVisible(true);
        this.getChart().getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
        this.getChart().getStyler().setMarkerSize(8);
    }

    private void initAlgorithm(Algorithm algorithm, Double xMin, Double yMin, Double xMax, Double yMax){
        this.setAlgo(algorithm);

        getChart().getStyler().setXAxisMin(xMin);
        getChart().getStyler().setYAxisMin(yMin);

        getChart().getStyler().setXAxisMax(xMax);
        getChart().getStyler().setYAxisMax(yMax);
    }

    public void execute(){

        if(this.isExecuting.isSet()){
            return;
        }

        this.getAlgo().initialize();
        this.getAlgo().setInterval(this.interval);

        double fraction = this.getAlgo().getStepsCount()/100;

        this.getAlgo().addStepAction(new StepAction() {
            @Override
            public void performAction(Vector best, Double bestValue, int step) {

                if(!isExecuting.isSet()){
                    initChartWithSeries();
                }else{
                    updateChartWithSeries();
                }

                double percentage = step/fraction;
                if((step % fraction) == 0){
                    Logger.getInstance().printTrail(Mathamatics.round(bestValue, 3), percentage);
                }
                if(action != null){
                    action.performAction(percentage);
                }
            }
        });
    }

    private void updateChartWithSeries() {
        List<SeriesData> data = this.getAlgo().getSeriesData();
        if(data == null){
            throw new RuntimeException("This Algorithm has not defined get function");
        }

        SeriesData s = null;
        for(int i=0; i<data.size(); i++){
            s = data.get(i);
            getChart().updateXYSeries(s.name, s.x, s.y, null);
        }
    }

    private void initChartWithSeries() {
        List<SeriesData> data = this.getAlgo().getSeriesData();
        if(data == null){
            throw new RuntimeException("This Algorithm has not defined get function");
        }

        for(String s: this.getChart().getSeriesMap().keySet()){
            this.getChart().removeSeries(s);
        }

        for(SeriesData d : data){
            XYSeries series = this.getChart().addSeries(d.name, d.x, d.y);
            series.setMarker(d.marker);
            series.setMarkerColor(d.markerColor);
        }
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public XYChart getChart() {
        return chart;
    }

    public Algorithm getAlgo() {
        return algo;
    }

    public void setAlgo(Algorithm algo) {
        this.algo = algo;
    }
}