package org.usa.soc.si.view.plotter;

import net.ericaro.surfaceplotter.Mapper;
import org.usa.soc.si.ObjectiveFunction;

public class FunctionToMapper implements Mapper {

    private ObjectiveFunction fn;
    public double xmin, xmax,ymin,ymax;

    public FunctionToMapper(ObjectiveFunction fn){
        this.fn = fn;
        fn.setFixedDimentions(2);
        xmin = fn.getMin()[0];
        ymin = fn.getMin()[1];
        xmax = fn.getMax()[0];
        ymax = fn.getMax()[1];
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
