package org.usa.soc.core.ds;

public class SeriesDataObject {
    double x[], y[];

    public SeriesDataObject(int count){
        x = new double[count];
        y = new double[count];
    }

    public void addXY(int i, double x, double y){
        this.x[i] = x;
        this.y[i] = y;
    }

    public double[] getX(){ return x; }
    public double[] getY(){ return y; }
}
