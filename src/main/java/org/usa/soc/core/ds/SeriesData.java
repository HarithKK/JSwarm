package org.usa.soc.core.ds;

import org.knowm.xchart.style.markers.*;
import org.knowm.xchart.style.markers.Rectangle;

import java.awt.*;

public class SeriesData {

    public  interface Markers {
        Marker NONE = new None();
        Marker CIRCLE = new Circle();
        Marker DIAMOND = new Diamond();
        Marker SQUARE = new Square();
        Marker TRIANGLE_DOWN = new TriangleDown();
        Marker TRIANGLE_UP = new TriangleUp();
        Marker CROSS = new Cross();
        Marker PLUS = new Plus();
        Marker TRAPEZOID = new Trapezoid();
        Marker OVAL = new Oval();
        Marker RECTANGLE = new Rectangle();
    }
    public String name;
    public double x[], y[];
    public Marker marker;
    public Color markerColor;

    public SeriesData(String name){
        this.name = name;
        this.marker = Markers.CIRCLE;
        this.markerColor = Color.blue;
    }


}
