package examples.multiagent.leader_election;

import org.apache.commons.math3.analysis.UnivariateMatrixFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RRQRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.usa.soc.util.Commons;

public class StateSpaceModel {

    RealMatrix GA, GB, K0, K1, KR, A, B, Gc, C, D, KC, KD;
    int n;

    public StateSpaceModel(int n){
        GA = MatrixUtils.createRealMatrix(n,n);
        GB = MatrixUtils.createRealMatrix(n,n);
        KD = MatrixUtils.createRealIdentityMatrix(n);
        this.n = n;
    }

    public void reset(){
        GA = MatrixUtils.createRealMatrix(n,n);
        GB = MatrixUtils.createRealMatrix(n,n);
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

    public void setKD(double []d){
        KD = MatrixUtils.createRealDiagonalMatrix(d);
    }

    public void setKC(double []d){
        KC = MatrixUtils.createRealDiagonalMatrix(d);
    }

    public void derive(){

        B = K1.scalarMultiply(-1);

        A = this.GB.multiply(MatrixUtils.createColumnRealMatrix(Commons.fill(1, n)));
        A = MatrixUtils.createRealDiagonalMatrix(A.getColumn(0));
        A = K1.multiply(A).scalarMultiply(-1);
        A = K0.multiply(K1).scalarMultiply(-1).add(A);

        C = elementMultiplier(GB, GB).subtract(GA);
        D = MatrixUtils.createRealIdentityMatrix(n).multiply(KD);
    }

    public RealMatrix elementMultiplier(RealMatrix r1, RealMatrix r2){
        RealMatrix rm = MatrixUtils.createRealMatrix(n, n);
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                rm.setEntry(i,j, r1.getEntry(i,j) * r2.getEntry(i,j));
            }
        }
        return rm;
    }

    public RealMatrix calcControllabilityGramian(long t1, long t2){

        UnivariateMatrixFunction mx = new UnivariateMatrixFunction() {
            @Override
            public double[][] value(double x) {
                RealMatrix rm = A.scalarMultiply(t2 - x);
                rm = Commons.expm(rm);
                rm = rm.multiply(B).multiply(B.transpose());

                RealMatrix rm1 = A.transpose().scalarMultiply(t2 -x);
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
            A.setEntry(0,i, val);
            A.setEntry(i, 0, val);
        }
    }

}
