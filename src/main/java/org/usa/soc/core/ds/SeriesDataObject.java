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
        int n = v.getNumberOfDimensions()-1;
        for(int j=1; j< n ; j++){
            this.y[i] += v.getValue(j);
        }
        this.y[i] /= n;
    }

    public double[] getX(){ return x; }
    public double[] getY(){ return y; }
}
