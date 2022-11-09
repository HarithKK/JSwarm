package ui;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.abc.ABC;
import org.usa.soc.aco.ACO;
import org.usa.soc.ba.BA;
import org.usa.soc.cs.CS;
import org.usa.soc.fa.FA;
import org.usa.soc.mbo.MBO;
import org.usa.soc.ms.MS;
import org.usa.soc.pso.PSO;
import org.usa.soc.tco.TCO;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
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


    public Algorithm getAlgorithm(int sc, int ac) {
        switch (a){
            case 0 : return new PSO(
                    fn,
                    ac,
                    fn.getNumberOfDimensions(),
                    sc,
                    1.496180,
                    1.496180,
                    0.729844,
                    fn.getMin(),
                    fn.getMax(),
                    true);
            case 1: return new ACO(
                    fn,
                    ac,
                    sc,
                    5,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
                    true
            );
            case 2: return new org.usa.soc.cso.CSO(
                    fn,
                    fn.getNumberOfDimensions(),
                    sc,
                    ac,
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
                return new org.usa.soc.gso.GSO(
                        fn,
                        fn.getNumberOfDimensions(),
                        sc,
                        ac,
                        fn.getMin(),
                        fn.getMax(),
                        5,
                        1,
                        0.4,
                        0.6,
                        2,
                        1,
                        0.08,
                        0.03,
                        true
                );
            }
            case 4: return new MBO(
                    fn,
                    ac,
                    100,
                    30,
                    sc,
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
                    sc,
                    ac,
                    fn.getNumberOfDimensions(),
                    50,
                    fn.getMin(),
                    fn.getMax(),
                    0.5,
                    true
            );
            case 6: return new WSO(
                    fn,
                    sc,
                    ac,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
                    0.4,
                    0.4,
                    true
            );
            case 7: return new CS(
                    fn,
                    sc,
                    fn.getNumberOfDimensions(),
                    ac,
                    fn.getMin(),
                    fn.getMax(),
                    1,
                    0.75,
                    true
            );
            case 8: return new FA(
                    fn,
                    sc,
                    fn.getNumberOfDimensions(),
                    ac,
                    fn.getMin(),
                    fn.getMax(),
                    1,
                    0.2,
                    1,
                    true
            );
            case 9: return new ABC(
                    fn,
                    sc,
                    fn.getNumberOfDimensions(),
                    ac,
                    fn.getMin(),
                    fn.getMax(),
                    100,
                    true
            );
            case 10:  return new BA(
                    fn,
                    sc,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
                    ac,
                    0,
                    100,
                    0.9,
                    0.9,
                    100,
                    Randoms.rand(0,1),
                    true
            );
            case 11: return new TCO(
                    fn,
                    sc,
                    fn.getNumberOfDimensions(),
                    ac,
                    fn.getMin(),
                    fn.getMax(),
                    1,
                    0.8,
                    0.72,
                    1.49,
                    true
            );
            case 12: return new org.usa.soc.gwo.GWO(
                    fn,
                    sc,
                    fn.getNumberOfDimensions(),
                    ac,
                    fn.getMin(),
                    fn.getMax(),
                    true
            );
            case 13: return new org.usa.soc.mfa.MFA(
                    fn,
                    sc,
                    fn.getNumberOfDimensions(),
                    ac,
                    fn.getMin(),
                    fn.getMax(),
                    1.0
            );
            case 14: return new org.usa.soc.alo.ALO(
                    fn,
                    ac,
                    sc,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
                    true
            );
            case 15: return new org.usa.soc.also.ALSO(
                    fn,
                    ac,
                    sc,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
                    true,
                    10,
                    10,
                    2.5,
                    1.0,
                    10,
                    10
            );
            case 16: return new org.usa.soc.geo.GEO(
                    fn,
                    ac,
                    sc,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
                    true,
                    2,
                    0.5,
                    0.5,
                    1
            );
            case 17: return new org.usa.soc.avoa.AVOA(
                    fn,
                    ac,
                    sc,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
                    true,
                    2.5,
                    0.8,
                    0.2,
                    0.6,
                    0.4,
                    0.6
            );
            case 18: return new org.usa.soc.tsa.TSA(
                    fn,
                    1000,
                    200,
                    fn.getNumberOfDimensions(),
                    fn.getMin(),
                    fn.getMax(),
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
        algo.add("ABC");
        algo.add("BA");
        algo.add("TCO");
        algo.add("GWO");
        algo.add("MFA");
        algo.add("ALO");
        algo.add("ALSO");
        algo.add("GEO");
        algo.add("AVOA");
        algo.add("TSA");

        return algo;
    }
}
