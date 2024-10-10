package org.usa.soc.core.ds;

import org.knowm.xchart.style.markers.*;

import java.awt.*;

public class SeriesData {
    public String name;
    private Marker marker;
    private Color markerColor;

    private Color lineColor;

    public SeriesData(String name){
        this.name = name;
        this.setMarker(Markers.CIRCLE);
        this.setMarkerColor(Color.blue);
        this.setLineColor(Color.DARK_GRAY);
    }


    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Color getMarkerColor() {
        return markerColor;
    }

    public void setMarkerColor(Color markerColor) {
        this.markerColor = markerColor;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
}
