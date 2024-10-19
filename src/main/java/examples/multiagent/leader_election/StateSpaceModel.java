package examples.multiagent.leader_election;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateMatrixFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RRQRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.util.Commons;

public class StateSpaceModel {

    RealMatrix A, B, K0, K1, KR, AA, BB, Gc;
    int n;

    public StateSpaceModel(int n){
        A = MatrixUtils.createRealMatrix(n,n);
        B = MatrixUtils.createRealMatrix(n,n);
        this.n = n;
    }

    public void setK0(double []d){
        K0 = MatrixUtils.createRealDiagonalMatrix(d);
    }

    public void setK1(double []d){
        K1 = MatrixUtils.createRealDiagonalMatrix(d);
    }

    public void setKR(double []d){
        KR = MatrixUtils.createRealDiagonalMatrix(d);
    }

    public void derive(){

        BB = K1.scalarMultiply(-1);

        AA = this.B.multiply(MatrixUtils.createColumnRealMatrix(Commons.fill(1, n)));
        AA = MatrixUtils.createRealDiagonalMatrix(AA.getColumn(0));
        AA = K1.multiply(AA).scalarMultiply(-1);
        AA = K0.multiply(K1).scalarMultiply(-1).add(AA);
    }

    public RealMatrix calcControllabilityGramian(long t1, long t2){

        UnivariateMatrixFunction mx = new UnivariateMatrixFunction() {
            @Override
            public double[][] value(double x) {
                RealMatrix rm = AA.scalarMultiply(t2 - x);
                rm = Commons.expm(rm);
                rm = rm.multiply(BB).multiply(BB.transpose());

                RealMatrix rm1 = AA.transpose().scalarMultiply(t2 -x);
                rm1 = Commons.expm(rm1);

                return rm.multiply(rm1).getData();
            }
        };

        UnivariateIntegrator integrator = new SimpsonIntegrator();
        Gc =MatrixUtils.createRealMatrix(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                final int r = i;
                final int c = j;
                Gc.setEntry(r,c,integrator.integrate(1000, (double x) -> mx.value(x)[r][c], t1, t2));
            }
        }


        return Gc;
    }

    public int getGcRank(){
        RRQRDecomposition decomposition = new RRQRDecomposition(Gc);
        return decomposition.getRank(0.00001);
    }

    public boolean isModelControllable(){
        return getGcRank() == n;
    }

    public void replace(int index, double val){
        for(int i =0; i<n;i++){
            AA.setEntry(0,i, val);
            AA.setEntry(i, 0, val);
        }
    }

}
