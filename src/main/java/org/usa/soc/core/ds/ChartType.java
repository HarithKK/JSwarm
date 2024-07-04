package org.usa.soc.core.ds;

import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.*;

public interface ChartType {
    XYSeries.XYSeriesRenderStyle Line = XYSeries.XYSeriesRenderStyle.Line;
    XYSeries.XYSeriesRenderStyle Scatter = XYSeries.XYSeriesRenderStyle.Scatter;
    XYSeries.XYSeriesRenderStyle Area = XYSeries.XYSeriesRenderStyle.Area;
    XYSeries.XYSeriesRenderStyle PolygonArea = XYSeries.XYSeriesRenderStyle.PolygonArea;
    XYSeries.XYSeriesRenderStyle StepArea = XYSeries.XYSeriesRenderStyle.StepArea;
    XYSeries.XYSeriesRenderStyle Step = XYSeries.XYSeriesRenderStyle.Step;
}