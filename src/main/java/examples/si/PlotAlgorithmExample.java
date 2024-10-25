package examples.si;

import examples.si.benchmarks.singleObjective.CrossInTrayFunction;
import org.usa.soc.core.ds.Margins;
import org.usa.soc.multiagent.Algorithm;
import org.usa.soc.multiagent.runners.Executor;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.si.runners.AlgorithmFactory;

public class PlotAlgorithmExample {
    public static void main(String[] args) {

      //  for(int i=0; i<30; i++){
            ObjectiveFunction function = new CrossInTrayFunction();
            Algorithm algorithm = new AlgorithmFactory(1, function).getAlgorithm(100, 100);
            //Executor.getInstance().executeSIAlgorithm("TSOA", (SIAlgorithm) algorithm, 600, 600);
            Executor.getInstance().executePlain2D(algorithm.getName(), algorithm, 600, 600, new Margins(function.getMin()[0], function.getMax()[0], function.getMin()[1], function.getMax()[1]));

       //}
    }
}
