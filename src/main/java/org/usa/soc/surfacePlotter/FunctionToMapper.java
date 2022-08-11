package org.usa.soc.surfacePlotter;

import net.ericaro.surfaceplotter.Mapper;
import org.usa.soc.ObjectiveFunction;

public class FunctionToMapper implements Mapper {

    private ObjectiveFunction fn;

    public FunctionToMapper(ObjectiveFunction fn){
        this.fn = fn;
    }

    @Override
    public float f1(float v, float v1) {
        Double[] p = new Double[2];
        p[0] = Double.valueOf(v);
        p[1] = Double.valueOf(v1);
        return fn.setParameters(p).call().floatValue();
    }

    @Override
    public float f2(float v, float v1) {
        return 0;
    }
}
