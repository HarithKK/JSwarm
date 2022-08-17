package ui;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.aco.ACO;
import org.usa.soc.cs.CS;
import org.usa.soc.fa.FA;
import org.usa.soc.mbo.MBO;
import org.usa.soc.ms.MS;
import org.usa.soc.pso.PSO;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.wso.WSO;

import java.util.ArrayList;
import java.util.List;

public class AlgoStore {

    private int a;
    private ObjectiveFunction fn;

    public AlgoStore(int a, ObjectiveFunction fn) {
        this.a = a;
        this.fn = fn;
    }


    public Algorithm getAlgorithm() {
        switch (a){
            case 0 : return new PSO(
                    fn,
                    1000,
                    fn.getNumberOfDimensions(),
                    100,
                    1.496180,
                    1.496180,
                    0.729844,
                    fn.getMin(),
                    fn.getMax(),
                    true);
            case 1: return new ACO(
                    fn,
                    1000,
                    100,
                    10,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
                    true
            );
            case 2: return new org.usa.soc.cso.CSO(
                    fn,
                    fn.getNumberOfDimensions(),
                    1500,
                    1000,
                    0.2,
                    fn.getMin(),
                    fn.getMax(),
                    10,
                    0.2,
                    0.2,
                    true,
                    0.5,
                    0.2,
                    true
            );
            case 3: {
                double sr = Mathamatics.getMaximumDimensionDistance(fn.getMin(), fn.getMax(), fn.getNumberOfDimensions());
                return new org.usa.soc.gso.GSO(
                        fn,
                        fn.getNumberOfDimensions(),
                        1000,
                        1000,
                        fn.getMin(),
                        fn.getMax(),
                        10,
                        sr,
                        0.4,
                        0.6,
                        10,
                        sr,
                        0.08,
                        2.0,
                        true
                );
            }
            case 4: return new MBO(
                    fn,
                    500,
                    100,
                    30,
                    1000,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
                    true,
                    0.9,
                    0,
                    10,
                    0.5,
                    0.5,
                    10
            );
            case 5: return new MS(
                    fn,
                    1000,
                    1000,
                    fn.getNumberOfDimensions(),
                    50,
                    fn.getMin(),
                    fn.getMax(),
                    0.5,
                    true
            );
            case 6: return new WSO(
                    fn,
                    1000,
                    1000,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
                    0.4,
                    0.4,
                    true
            );
            case 7: return new CS(
                    fn,
                    100,
                    fn.getNumberOfDimensions(),
                    1000,
                    fn.getMin(),
                    fn.getMax(),
                    1,
                    0.5,
                    true
            );
            case 8: return new FA(
                    fn,
                    100,
                    fn.getNumberOfDimensions(),
                    500,
                    fn.getMin(),
                    fn.getMax(),
                    1,
                    0.2,
                    1,
                    true
            );
        }
        return null;
    }

    public static List<String> generateAlgo() {
        List<String> algo = new ArrayList<>();

        algo.add("PSO");
        algo.add("ACO");
        algo.add("CSO");
        algo.add("GSO");
        algo.add("MBO");
        algo.add("MS");
        algo.add("WSO");
        algo.add("CS");
        algo.add("FA");

        return algo;
    }
}
