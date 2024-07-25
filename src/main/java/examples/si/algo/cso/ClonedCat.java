package examples.si.algo.cso;

import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.Agent;
import org.usa.soc.util.Randoms;

public class ClonedCat extends Agent {
    private double fsValue;

    void randomUpdatePosition(int n, int D, double srd){
        for(int i=0; i<n; i++){
            int index = Randoms.rand(D);
            double v = Randoms.rand(0,1)*srd;
            v = (1+v)*position.getValue(index);
            position.setValue(v, index);
        }
    }
    public double getFsValue() {
        return fsValue;
    }

    public void setFsValue(double fsValue) {
        this.fsValue = fsValue;
    }
}
