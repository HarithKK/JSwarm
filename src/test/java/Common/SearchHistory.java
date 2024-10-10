package Common;

import examples.si.AlgorithmFactory;
import examples.si.benchmarks.nonGeneral.classical.multimodal.separable.CsendesFunction;
import examples.si.benchmarks.singleObjective.*;
import org.junit.jupiter.api.Test;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.si.SIAlgorithm;

import java.util.List;

public class SearchHistory {
    @Test
    public void Ackleys() throws Exception {
        SIAlgorithm algorithm = new AlgorithmFactory(22, new AckleysFunction()).getAlgorithm(1000, 10);

        algorithm.initialize();
        algorithm.run();


        List<Vector> h = algorithm.getPHistory();

        StringBuilder sb = new StringBuilder("x=np.array([");
        for(int i=0; i<200; i+=1){
            sb.append(h.get(i).getPositionIndexes()[0]);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("])");
        System.out.println(sb.toString());

        sb = new StringBuilder("y=np.array([");
        for(int i=0; i<200; i+=1){
            sb.append(h.get(i).getPositionIndexes()[1]);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("])");
        System.out.println(sb.toString());

        sb = new StringBuilder("y1=np.array([");
        for(int i=0; i<200; i+=1){
            sb.append(h.get(i).getMagnitude());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("])");
        System.out.println(sb.toString());
    }

    @Test
    public void Sphere() throws Exception {
        SIAlgorithm algorithm = new AlgorithmFactory(22, new SphereFunction()).getAlgorithm(1000, 10);

        algorithm.initialize();
        algorithm.run();


        List<Vector> h = algorithm.getPHistory();

        StringBuilder sb = new StringBuilder("x=np.array([");
        for(int i=0; i<200; i+=1){
            sb.append(h.get(i).getPositionIndexes()[0]);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("])");
        System.out.println(sb.toString());

        sb = new StringBuilder("y=np.array([");
        for(int i=0; i<200; i+=1){
            sb.append(h.get(i).getPositionIndexes()[1]);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("])");
        System.out.println(sb.toString());

        sb = new StringBuilder("y1=np.array([");
        for(int i=0; i<200; i+=1){
            sb.append(h.get(i).getMagnitude());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("])");
        System.out.println(sb.toString());
    }

    @Test
    public void cit() throws Exception {
        SIAlgorithm algorithm = new AlgorithmFactory(22, new CrossInTrayFunction()).getAlgorithm(1000, 10);

        algorithm.initialize();
        algorithm.run();


        List<Vector> h = algorithm.getPHistory();

        StringBuilder sb = new StringBuilder("x=np.array([");
        for(int i=0; i<200; i+=1){
            sb.append(h.get(i).getPositionIndexes()[0]);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("])");
        System.out.println(sb.toString());

        sb = new StringBuilder("y=np.array([");
        for(int i=0; i<200; i+=1){
            sb.append(h.get(i).getPositionIndexes()[1]);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("])");
        System.out.println(sb.toString());

        sb = new StringBuilder("y1=np.array([");
        for(int i=0; i<200; i+=1){
            sb.append(h.get(i).getMagnitude());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("])");
        System.out.println(sb.toString());
    }

    @Test
    public void cs() throws Exception {
        SIAlgorithm algorithm = new AlgorithmFactory(22, new CsendesFunction()).getAlgorithm(1000, 10);

        algorithm.initialize();
        algorithm.run();


        List<Vector> h = algorithm.getPHistory();

        StringBuilder sb = new StringBuilder("x=np.array([");
        for(int i=0; i<200; i+=1){
            sb.append(h.get(i).getPositionIndexes()[0]);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("])");
        System.out.println(sb.toString());

        sb = new StringBuilder("y=np.array([");
        for(int i=0; i<200; i+=1){
            sb.append(h.get(i).getPositionIndexes()[1]);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("])");
        System.out.println(sb.toString());

        sb = new StringBuilder("y1=np.array([");
        for(int i=0; i<200; i+=1){
            sb.append(h.get(i).getMagnitude());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("])");
        System.out.println(sb.toString());
    }
}
