package examples.si.benchmarks.DynamicUnimodalObjectiveFunctions;

import examples.si.benchmarks.DynamicObjectiveFunction;
import org.usa.soc.util.Randoms;

public class Function7 extends DynamicObjectiveFunction {

    public Function7(int n){
        super(n, -1.28, 1.28, -0.25, 0);
    }

    public  Function7(int n, double p, double q){
        super(n, p, q, -30, 0);
    }

    @Override
    public Double call() {
        Double sum = 0.0;
        for(int i=0;i< getNumberOfDimensions();i++){
            Double data = (Double)super.getParameters()[i];
            sum += i* Math.pow(data,4) + Randoms.randLBmax(0, 1);
        }
        return sum;
    }
}
