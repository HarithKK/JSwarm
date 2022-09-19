package soc.usa.display;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class IterationChartPlotter {

    private XYChart chart;

    private List<Double> yData;
    private XYSeries series;

    private DecimalFormat decimalFormat;

    private double initValue;

    public IterationChartPlotter(int width, int height, String title, String seriesName, double initValue){
        this.chart = new XYChartBuilder()
                    .width(width)
                    .height(height)
                    .title(title)
                    .xAxisTitle("Iteration")
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

    public void addData(int step, double data){
        if(this.yData.size() == 1 && this.yData.get(0) == initValue){
            this.yData.remove(0);
            this.yData.add(Double.valueOf(decimalFormat.format(data)));
            return;
        }
        this.yData.add(Double.valueOf(decimalFormat.format(data)));
        this.getChart().updateXYSeries(series.getName(), null, yData, null);
    }

    public void clearChart(){
        this.yData.clear();
        this.yData.add(initValue);
    }

    public XYChart getChart() {
        return chart;
    }
}
