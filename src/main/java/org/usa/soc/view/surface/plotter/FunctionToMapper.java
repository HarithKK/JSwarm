package org.usa.soc.view.surface.plotter;

import net.ericaro.surfaceplotter.Mapper;
import org.usa.soc.si.ObjectiveFunction;

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
        return fn.setParameters(new Double[]{
            Double.valueOf(v),
            Double.valueOf(v1)
        }).call().floatValue();
    }

    @Override
    public float f2(float v, float v1) {
        return 0;
    }
}
