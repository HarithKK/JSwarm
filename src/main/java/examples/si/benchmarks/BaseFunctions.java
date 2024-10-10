package examples.si.benchmarks;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

public class BaseFunctions {

    public static double BentCigar(RealMatrix rw, double fBias, int n){
        Double res =0.0;
        for(int i=1; i<n; i++){
            res += Math.pow(rw.getEntry(0,i),2);
        }
        return (res *1000000) + Math.pow(rw.getEntry(0,0),2) + fBias;
    }

    public static double DiscusFunction(RealMatrix rw, double fBias){
        Double res =0.0;
        for(int i=1; i<rw.getRow(0).length; i++){
            res += Math.pow(rw.getEntry(0,i),2);
        }
        return res + (Math.pow(rw.getEntry(0,0),2)*1000000) + fBias;
    }

    public static double EllipticFunction(RealMatrix rw, double fBias, int n){
        Double res =0.0;
        for(int i=0; i<n; i++){
            double p = ((i-1)/(n-1));
            res += Math.pow(1000000, p) * Math.pow(rw.getEntry(0,i),2);
        }
        return res + fBias;
    }

    public static double AckleyFunction(RealMatrix rw, double fBias, int n){
        double d1 =0;
        double d2=0;

        for(int i=1; i< n; i++){
            double x = rw.getEntry(0,i);
            d1 += Math.pow(x,2);
            d2 = Math.cos(2*Math.PI*x);
        }
        double p1 = -20*Math.exp(-0.2*Math.sqrt(d1/n));
        double p2 = Math.exp(d2/n);
        return (p1 - p2 + Math.E + 20) + fBias;
    }

    public static double GriewanksFunction(RealMatrix rw, double fBias, int n){
        double y1 =0;
        double y2=0;

        for(int i=1; i< n; i++){
            double x = rw.getEntry(0,i);
            y1 += Math.pow(x,2);
            y2 *= Math.cos(x / Math.sqrt(i));
        }
        return ((y1 - y2 +1)/4000) + fBias;
    }

    public static double RosenbrocksFunction(RealMatrix rw, double fBias, int n){
        double sum =0;
        for(int i=0;i<n-1;i++){
            sum += (Math.pow((rw.getEntry(0,i+1) - Math.pow(rw.getEntry(0,i), 2)), 2) * 100) + Math.pow((rw.getEntry(0,i)-1),2);
        }
        return sum + fBias;
    }

    public static double RastriginsFunction(RealMatrix rw, double fBias, int n){
        double sum =0;
        for(int i=0;i<n-1;i++){
            sum += (Math.pow(rw.getEntry(0,i),2) - (10*Math.cos(2*Math.PI*rw.getEntry(0,i))) + 10);
        }
        return sum + fBias;
    }

    public static double ZakharovFunction(RealMatrix rw, double fBias, int n){
        Double res1 =0.0;
        Double res =0.0;
        for(int i=0; i<n; i++){
            res1 += 0.5 * (i+1) * rw.getEntry(0,i);
            res += Math.pow(rw.getEntry(0,i),2);
        }

        return Math.pow(res1, 2) + Math.pow(res1, 4) + res + fBias;
    }

    public static double SphereFunction(RealMatrix rw, double fBias, int n){
        double sum =0;
        for(int i=0; i<n; i++){
            sum += Math.pow(rw.getEntry(0, i),2);
        }
        return sum + fBias;
    }

    public static double Schwefel12Function(RealMatrix rw, double fBias, int n){
        Double res =0.0;
        for(int i=0; i<n; i++){
            Double r = 0.0;
            for(int j=0; j<=i;j++){
                r += rw.getEntry(0, i);
            }
            res += Math.pow(r, 2);
        }
        return res +fBias;
    }

    public static double ModifiedSchwefelFunction(RealMatrix rw, double fBias, int n){
        Double res =0.0;
        for(int i=0; i<n; i++){
            Double z = rw.getEntry(0, i)+ 420.9687462275036;
            Double gz = 0.0;
            if(Math.abs(z) > 500){
                gz = (500 - (z % 500))*Math.sin(Math.sqrt(Math.abs(500 - (z % 500)))) - (Math.pow(z - 500,2)/(10000*n));
            }else if (Math.abs(z) < -500){
                gz = ((Math.abs(z) % 500) - 500)*Math.sin(Math.sqrt(Math.abs((Math.abs(z) % 500) - 500))) - (Math.pow(z - 500,2)/(10000*n));
            }else{
                gz = z * Math.sin(Math.pow(Math.abs(z),0.5));
            }
        }
        return ((418.9829*n) - res) +fBias;
    }

    public static double SchafferFunction(RealMatrix rw, double fBias, int n){
        double rx = Math.pow(rw.getEntry(0,0), 2) - Math.pow(rw.getEntry(0,1), 2);
        return ((Math.pow(Math.sin(rx), 2) - 0.5)/Math.pow(1 + 0.001*rx,2)) + 0.5 + fBias;
    }

    public static double HgBat(RealMatrix rw, double fBias, int n){

        double t1 = Arrays.stream(rw.getRow(0)).sum();
        double t2 = Arrays.stream(rw.getRow(0)).map(d -> Math.pow(d, 2)).sum();

        return (Math.sqrt(Math.abs(Math.pow(t2,2) - Math.pow(t1,2))) + (0.5 * t2 + t1) / n) + 0.5 + fBias;
    }

    public static double Katsuura(RealMatrix rw, double fBias, int n){

        Double result = 1.0;
        for(int i=0; i<n; i++){
            double sum = 0.0;
            for(int j=0; j<n; j++){
                sum += Math.pow(2, j)*rw.getEntry(0, j)*Math.pow(2, -j);
            }
            result *= sum*(i+1)+ 1;
        }
        return result + fBias;
    }
}
