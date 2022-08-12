package org.usa.soc.cso;

import org.usa.soc.core.Vector;
import org.usa.soc.util.Randoms;

public class ClonedCat {
    private Vector position;
    private double fsValue;

    void randomUpdatePosition(int n, int D, double srd){
        for(int i=0; i<n; i++){
            int index = Randoms.rand(D);
            double v = Randoms.rand(0,1)*srd;
            v = (1+v)*position.getValue(index);
            position.setValue(v, index);
        }
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public double getFsValue() {
        return fsValue;
    }

    public void setFsValue(double fsValue) {
        this.fsValue = fsValue;
    }
}
