package org.usa.soc.multiagent.view;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.usa.soc.core.ds.SeriesDataObject;
import org.usa.soc.multiagent.Agent;
import org.usa.soc.multiagent.AgentGroup;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.core.Flag;
import org.usa.soc.core.action.Action;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.core.ds.SeriesData;
import org.usa.soc.multiagent.StepCompleted;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.joining;

public class PlainView2D {

    private XYChart chart= null;
    private Algorithm algo = null;

    private int interval = 0;
    private Flag isExecuting = new Flag();

    private Action action;

    public PlainView2D(String title, int w, int h){
        this.initComponents(title, w, h);
    }

    public PlainView2D(String title, int w, int h, Algorithm algorithm){
        this.initComponents(title, w, h);
        this.initAlgorithm(algorithm, algorithm.getMargins());
    }

    public PlainView2D(String title, int w, int h, Algorithm algorithm, Margins m){
        this.initComponents(title, w, h);
        this.initAlgorithm(algorithm, m);
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

    private void initAlgorithm(Algorithm algorithm, Margins m){
        this.setAlgo(algorithm);

        if(m != null){
            getChart().getStyler().setXAxisMin(m.xMin);
            getChart().getStyler().setYAxisMin(m.yMin);

            getChart().getStyler().setXAxisMax(m.xMax);
            getChart().getStyler().setYAxisMax(m.yMax);
        }
    }

    public void execute() throws Exception{

        if(this.isExecuting.isSet()){
            return;
        }

        this.getAlgo().runInitializer();
        this.getAlgo().setInterval(this.interval);

        this.getAlgo().setStepCompleted(new StepCompleted() {
                    @Override
                    public void performAction(long step) {
                        if(!isExecuting.isSet()){
                            initChartWithSeries();
                        }else{
                            updateChartWithSeries();
                        }

                        if(action != null){
                            action.performAction(step);
                        }
                        isExecuting.set();
                    }
        });

        this.getAlgo().runOptimizer();
    }

    private void updateChartWithSeries() {
        Map<String, AgentGroup> data = this.getAlgo().getSeriesData();
        if(data == null){
            throw new RuntimeException("This Algorithm has not defined get function");
        }

        for(String key: data.keySet()){
            AgentGroup agentGroup = data.get(key);
            SeriesDataObject obj = agentGroup.getLocations();
            chart.updateXYSeries(agentGroup.name, obj.getX(), obj.getY(), null);
        }
    }

    private void initChartWithSeries() {
        Map<String, AgentGroup> data = this.getAlgo().getSeriesData();
        if(data == null){
            throw new RuntimeException("This Algorithm has not defined get function");
        }

        Set<String> keys = this.getChart().getSeriesMap().keySet();
        for(String s: keys){
            this.chart.removeSeries(s);
        }

        for(String key: data.keySet()){
            AgentGroup agentGroup = data.get(key);
            SeriesDataObject obj = agentGroup.getLocations();
            XYSeries series = this.chart.addSeries(agentGroup.name, obj.getX(), obj.getY());
            series.setMarker(agentGroup.getMarker());
            series.setMarkerColor(agentGroup.getMarkerColor());
        }
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
        this.getAlgo().setInterval(this.interval);
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

    public void stopExecution() {
        algo.stopOptimizer();
        this.isExecuting.unset();
    }

    public void pauseExecution() {
        algo.pauseOptimizer();
    }
}