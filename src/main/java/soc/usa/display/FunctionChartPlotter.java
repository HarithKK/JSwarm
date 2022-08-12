package soc.usa.display;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.usa.soc.Algorithm;
import org.usa.soc.core.Action;
import org.usa.soc.core.Vector;

import javax.swing.*;
import java.awt.*;

public class FunctionChartPlotter {

    private XYChart chart= null;
    private double xdata[], ydata[];

    private SwingWrapper<XYChart> sw;

    private Algorithm algorithm;
    public FunctionChartPlotter(String title, int w, int h){

        this.chart = new XYChartBuilder()
                .width(w).height(h).title(title).xAxisTitle("X").yAxisTitle("Y").build();

        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
        chart.getStyler().setMarkerSize(5);
    }

    public void setChart(Algorithm a){
        this.algorithm = a;
        this.xdata = new double[(int)(a.getFunction().getMax()[0] - a.getFunction().getMin()[0])+50];
        this.ydata = new double[(int)(a.getFunction().getMax()[1] - a.getFunction().getMin()[1])+50];

        try {
            chart.removeSeries("Agents");
            chart.removeSeries("Best");
        }catch(Exception e){

        }
        XYSeries series = this.chart.addSeries("Agents", xdata, ydata);
        series.setMarker(SeriesMarkers.CIRCLE);
        XYSeries series1 = this.chart.addSeries("Best",
                new double[]{a.getFunction().getExpectedParameters()[0]},
                new double[]{a.getFunction().getExpectedParameters()[1]});
        series1.setMarker(SeriesMarkers.DIAMOND);
    }

    public void display(){
        sw = new SwingWrapper(chart);
        sw.displayChart();
    }

    public void execute(){
        algorithm.initialize();

        algorithm.addStepAction(new Action() {
            @Override
            public void performAction(Vector best, Double bestValue) {
                double[][] d = algorithm.getDataPoints();
                xdata = d[0];
                ydata = d[1];

                chart.updateXYSeries("Agents", xdata, ydata, null);
                sw.repaintChart();
            }
        });
        algorithm.runOptimizer(10);
        System.out.println(algorithm.getBestDoubleValue() +" "+algorithm.getFunction().getExpectedBestValue());
        System.out.println(algorithm.getGBest().toString());
    }
}
