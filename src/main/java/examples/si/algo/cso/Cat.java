package examples.si.algo.cso;

import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.util.Randoms;
import org.usa.soc.util.Validator;

public class Cat {

    private double[] minBoundary, maxBoundary;

    private int numberOfDimensions;
    private Vector position, velocity;

    private Mode mode;

    private int smp;
    private double cdc, srd;
    private boolean spc;

    private ClonedCat[] memoryPool;

    private int n;

    private ClonedCat nextBestShadowCat;

    private int index;

    public Cat(int index, double[] minBoundary, double[] maxBoundary, int numberOfDimensions, Mode mode, int smp, double cdc, double srd, boolean spc) {

        this.index = index;
        this.minBoundary = minBoundary;
        this.maxBoundary = maxBoundary;
        this.numberOfDimensions = numberOfDimensions;
        this.mode = mode;
        this.smp = smp;
        this.cdc = cdc;
        this.srd = srd;
        this.spc = spc;

        this.memoryPool = new ClonedCat[smp];
        this.n = (int)(numberOfDimensions* (1-cdc));
        this.position = Randoms.getRandomVector(numberOfDimensions, minBoundary, maxBoundary);
        this.velocity = position.getClonedVector();

    }

    public Vector getPosition() {
        return position.getClonedVector();
    }

    public boolean isSeeker() {
        return this.mode == Mode.SEEKER;
    }

    public void seek(ObjectiveFunction fn, boolean isMinima) {

        double minFs = Double.POSITIVE_INFINITY, maxFs= Double.NEGATIVE_INFINITY;

        for(int i=0; i<smp;i++){
            ClonedCat c = new ClonedCat();
            c.setPosition(this.position.getClonedVector());
            c.randomUpdatePosition(this.n, this.numberOfDimensions, this.srd);
            double rValue = fn.setParameters(c.getPosition().getPositionIndexes()).call();
            c.setFsValue(rValue);
            minFs = Math.min(minFs, rValue);
            maxFs = Math.max(maxFs, rValue);
            memoryPool[i] = c;
        }

        nextBestShadowCat = selectNextBestCat(maxFs, minFs, isMinima);

    }

    private ClonedCat selectNextBestCat(double maxFs, double minFs, boolean isMinima){
        double deltaFs = maxFs - minFs;

        if(deltaFs == 0){
            return this.memoryPool[0];
        }

        double fsb = isMinima ? maxFs : minFs;
        double p0 = Randoms.rand(0,1);

        // rouletteWheelSelection
        for(int i=0; i<smp; i++){
            double p = Math.abs(memoryPool[i].getFsValue() - fsb) / deltaFs;
            if(p > p0){
                return memoryPool[i];
            }
        }

        // tournament
        ClonedCat tmp = memoryPool[0];
        for(int i=1; i<smp; i++){
            if(Validator.validateBestValue(memoryPool[i].getFsValue(), tmp.getFsValue(), isMinima)){
                tmp = memoryPool[i];
            }
        }
        return tmp;
    }

    public void trace(double c, double w, Vector best) {
        double rc = c * Randoms.rand(0,1);
        Vector v = nextBestShadowCat == null ? position : nextBestShadowCat.getPosition();
        Vector v1 = best.operate(Vector.OPERATOR.SUB, v).operate(Vector.OPERATOR.MULP, rc);
        Vector v2 = v.operate(Vector.OPERATOR.MULP, w);

        this.velocity.setVector(v1.operate(Vector.OPERATOR.ADD, v2), minBoundary, maxBoundary);
        this.position.setVector(position.operate(Vector.OPERATOR.ADD, velocity), minBoundary, maxBoundary);
    }

    public void updateMode() {
        if(Randoms.rand(0,1) < 0.5){
            this.mode = Mode.SEEKER;
        }else{
            this.mode = Mode.TRACER;
        }
    }

    public int getIndex() {
        return index;
    }
}
