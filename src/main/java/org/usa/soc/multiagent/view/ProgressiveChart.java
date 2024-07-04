package org.usa.soc.multiagent.view;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ProgressiveChart {

    private XYChart chart;

    private DecimalFormat decimalFormat;

    private XChartPanel panel;

    private int maxLength = -1;

    private int step;

    Map<String, ChartSeries> chartSeriesMap = new HashMap<>();

    public ProgressiveChart(int width, int height, String chartName, String yAxisName, String xAxisName){
        this.chart = new XYChartBuilder()
                .width(width)
                .height(height)
                .title(chartName)
                .xAxisTitle(xAxisName)
                .yAxisTitle(yAxisName).build();
        getChart().getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        getChart().getStyler().setChartTitleVisible(false);
        getChart().getStyler().setLegendVisible(false);
        getChart().getStyler().setMarkerSize(2);

        decimalFormat = new DecimalFormat("#.##");

        this.clearChart();
    }

    public ProgressiveChart setLegend(boolean isVisible){
        getChart().getStyler().setLegendVisible(isVisible);
        return this;
    }

    public ProgressiveChart subscribe(ChartSeries chartSeries){
        chartSeries.yData.add(chartSeries.initValue);
        chartSeries.xData.add(0);
        chartSeries.series = getChart().addSeries(chartSeries.seriesName, null, chartSeries.yData);
        chartSeries.initialize();
        chartSeriesMap.put(chartSeries.seriesName, chartSeries);
        return this;
    }

    public ProgressiveChart setMaxLength(int length){
        this.maxLength = length;
        return this;
    }

    public void addData(String seriesName, double data){
        if(!chartSeriesMap.containsKey(seriesName))
            return;
        ChartSeries ch = chartSeriesMap.get(seriesName);
        if(ch.yData.size() == 1 && ch.yData.get(0) == ch.initValue){
            ch.yData.remove(0);
            ch.xData.remove(0);
            ch.xData.add(step);
            ch.yData.add(Double.valueOf(decimalFormat.format(data)));
            return;
        }
        ch.xData.add(step++);
        ch.yData.add(Double.valueOf(decimalFormat.format(data)));
        if(maxLength > 0 && ch.yData.size() == maxLength){
            ch.yData.remove(0);
            ch.xData.remove(0);
        }
        this.getChart().updateXYSeries(ch.seriesName, ch.xData, ch.yData, null);
    }

    public void clearChart(){
        for(ChartSeries ch: chartSeriesMap.values()){
            ch.yData.clear();
            ch.yData.add(ch.initValue);
        }
    }

    public XYChart getChart() {
        return chart;
    }

    public void updateUI(){
        panel.updateUI();
    }

    public XChartPanel getPanel() {
        panel = new XChartPanel(chart);
        return panel;
    }
}