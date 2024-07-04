package org.usa.soc.multiagent.view;

import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.Marker;
import org.usa.soc.core.ds.ChartType;
import org.usa.soc.core.ds.Markers;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChartSeries {
    List<Double> yData;
    List<Integer> xData;
    String seriesName;

    XYSeries series;

    double initValue = 0.0;

    Color markerColor = Color.BLUE;

    Marker marker = Markers.CIRCLE;

    XYSeries.XYSeriesRenderStyle style = ChartType.Line;

    public ChartSeries(String seriesName, double initValue){
        this.yData = new ArrayList<>();
        this.xData = new ArrayList<>();
        this.seriesName = seriesName;
        this.initValue = initValue;
    }

    public void initialize() {
        this.series.setXYSeriesRenderStyle(style);
        this.series.setMarker(marker);
        this.series.setMarkerColor(markerColor);
    }

    public ChartSeries setStyle(XYSeries.XYSeriesRenderStyle type){
        this.style = type;
        return this;
    }

    public ChartSeries setColor(Color color){
        this.markerColor = color;
        return this;
    }

    public ChartSeries setMarker(Marker marker){
        this.marker = marker;
        return this;
    }
}
