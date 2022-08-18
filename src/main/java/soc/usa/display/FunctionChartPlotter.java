package soc.usa.display;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.usa.soc.Algorithm;
import org.usa.soc.core.Action;
import org.usa.soc.core.Vector;
import org.usa.soc.util.Mathamatics;

public class FunctionChartPlotter {

    private XYChart chart= null;

    private double xdata[], ydata[], xbest[], ybest[];

    private SwingWrapper<XYChart> sw;

    private Algorithm algorithm;

    private int time = 10, bestIndex;
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
        double m = Math.max(a.getFunction().getMax()[0] - a.getFunction().getMin()[0], a.getFunction().getMax()[1] - a.getFunction().getMin()[1]);
        this.xdata = new double[(int)(m)+50];
        this.ydata = new double[(int)(m)+50];
        this.xbest = new double[100];
        this.ybest = new double[100];
        this.bestIndex = 0;

        try {
            chart.removeSeries("Agents");
            chart.removeSeries("Best");
        }catch(Exception e){

        }
        XYSeries series = this.chart.addSeries("Agents", xdata, ydata);
        series.setMarker(SeriesMarkers.CIRCLE);
        XYSeries seriesb = this.chart.addSeries("Best Search Trial", xbest, ybest);
        seriesb.setMarker(SeriesMarkers.CROSS);
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
        int step =0;
        int fraction = algorithm.getStepsCount()/100;
        algorithm.addStepAction(new Action() {
            @Override
            public void performAction(Vector best, Double bestValue, int step) {

                double[][] d = algorithm.getDataPoints();
                xdata = d[0];
                ydata = d[1];

                if(bestIndex < 0 || xbest[bestIndex] != best.getValue(0) || ybest[bestIndex] != best.getValue(1)){
                    bestIndex++;
                    xbest[bestIndex] = best.getValue(0);
                    ybest[bestIndex] = best.getValue(1);
                }

                chart.updateXYSeries("Agents", xdata, ydata, null);
                if((step % fraction) == 0){
                    System.out.print("\r ["+ Mathamatics.round(bestValue, 3) +"] ["+step/fraction+"%] "  + generate(() -> "#").limit(step/fraction).collect(joining()));
                }
                sw.repaintChart();
                step = step +1;
            }
        });
        algorithm.runOptimizer(time);
        System.out.println("");
        System.out.println(algorithm.getBestDoubleValue() +" "+algorithm.getFunction().getExpectedBestValue());
        System.out.println(algorithm.getGBest().toString());
    }

    public void setTime(int time) {
        this.time = time;
    }
}
