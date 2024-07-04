package org.usa.soc.multiagent.view;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProgressiveChart {

    private XYChart chart;

    private List<Double> yData;
    private XYSeries series;

    private DecimalFormat decimalFormat;

    private XChartPanel panel;

    private double initValue;

    private int maxLength = -1;

    public ProgressiveChart(int width, int height, String title, String seriesName, String xAxisName, double initValue){
        this.chart = new XYChartBuilder()
                .width(width)
                .height(height)
                .title(title)
                .xAxisTitle(xAxisName)
                .yAxisTitle(seriesName).build();
        getChart().getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        getChart().getStyler().setChartTitleVisible(false);
        getChart().getStyler().setLegendVisible(false);
        getChart().getStyler().setMarkerSize(3);

        decimalFormat = new DecimalFormat("#.##");
        initValue = initValue;

        yData = new ArrayList<>();

        this.clearChart();

        series = getChart().addSeries(seriesName, null, yData);
    }

    public void setMaxLength(int length){
        this.maxLength = length;
    }

    public void addData(int step, double data){
        if(this.yData.size() == 1 && this.yData.get(0) == initValue){
            this.yData.remove(0);
            this.yData.add(Double.valueOf(decimalFormat.format(data)));
            return;
        }
        this.yData.add(Double.valueOf(decimalFormat.format(data)));
        if(maxLength > 0 && this.yData.size() == maxLength){
            this.yData.remove(0);
        }
        this.getChart().updateXYSeries(series.getName(), null, yData, null);
    }

    public void clearChart(){
        this.yData.clear();
        this.yData.add(initValue);
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