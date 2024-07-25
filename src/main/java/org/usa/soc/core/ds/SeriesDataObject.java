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

    public void addXY(int i, Vector v){
        this.x[i] = v.getValue(0);
        double d = 0.0;
        for(int j=1; j< v.getNumberOfDimensions() ; j++){
            d += v.getValue(j);
        }
        this.y[i] = d / (v.getNumberOfDimensions()-1);
    }

    public double[] getX(){ return x; }
    public double[] getY(){ return y; }
}
