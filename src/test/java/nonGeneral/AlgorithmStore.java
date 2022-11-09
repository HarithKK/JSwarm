package nonGeneral;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.abc.ABC;
import org.usa.soc.ba.BA;
import org.usa.soc.cs.CS;
import org.usa.soc.fa.FA;
import org.usa.soc.pso.PSO;
import org.usa.soc.tco.TCO;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.util.Randoms;
import org.usa.soc.wso.WSO;

public class AlgorithmStore {

    public Algorithm getPSO(ObjectiveFunction fn, int ac, int sc){
        return new PSO(
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
    }

    public Algorithm getCSO(ObjectiveFunction fn, int ac, int sc){
        return new org.usa.soc.cso.CSO(
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
    }

    public Algorithm getGSO(ObjectiveFunction fn, int ac, int sc){
        double sr = Mathamatics.getMaximumDimensionDistance(fn.getMin(), fn.getMax(), fn.getNumberOfDimensions());
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
                3,
                1,
                0.08,
                0.03,
                true
        );
    }

    public Algorithm getWSO(ObjectiveFunction fn, int ac, int sc){
        return new WSO(
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
    }
    public Algorithm getCS(ObjectiveFunction fn, int ac, int sc){
        return new CS(
                fn,
                sc,
                fn.getNumberOfDimensions(),
                ac,
                fn.getMin(),
                fn.getMax(),
                1,
                0.25,
                true
        );
    }
    public Algorithm getFA(ObjectiveFunction fn, int ac, int sc){
        return new FA(
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
    }

    public Algorithm getABC(ObjectiveFunction fn, int ac, int sc){
        return new ABC(
                fn,
                sc,
                fn.getNumberOfDimensions(),
                ac,
                fn.getMin(),
                fn.getMax(),
                100,
                true
        );
    }

    public Algorithm getBA(ObjectiveFunction fn, int ac, int sc){
        return new BA(
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
    }

    public Algorithm getTCO(ObjectiveFunction fn, int ac, int sc){
        return new TCO(
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
    }

    public Algorithm getGWO(ObjectiveFunction fn, int ac, int sc){
        return new org.usa.soc.gwo.GWO(
                fn,
                sc,
                fn.getNumberOfDimensions(),
                ac,
                fn.getMin(),
                fn.getMax(),
                true
        );
    }

    public Algorithm getMFA(ObjectiveFunction fn, int ac, int sc){
        return new org.usa.soc.mfa.MFA(
                fn,
                sc,
                fn.getNumberOfDimensions(),
                ac,
                fn.getMin(),
                fn.getMax(),
                1.0
        );
    }

    public Algorithm getALO(ObjectiveFunction fn, int ac, int sc){
        return new org.usa.soc.alo.ALO(
                fn,
                ac,
                sc,
                fn.getNumberOfDimensions(),
                fn.getMin(),
                fn.getMax(),
                true
        );
    }

    public Algorithm getALSO(ObjectiveFunction fn, int ac, int sc){
        return new org.usa.soc.also.ALSO(
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
    }

    public Algorithm getGEO(ObjectiveFunction fn, int ac, int sc){
        return new org.usa.soc.geo.GEO(
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
    }



}
