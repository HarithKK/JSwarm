package org.usa.soc.benchmarks;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.nonGeneral.classical.multimodal.nonseparable.ColvilleFunction;
import org.usa.soc.benchmarks.nonGeneral.classical.multimodal.nonseparable.ZakharovFunction;
import org.usa.soc.benchmarks.nonGeneral.classical.unimodal.separable.PowellSumFunction;
import org.usa.soc.benchmarks.nonGeneral.classical.unimodal.separable.StepintFunction;
import org.usa.soc.benchmarks.singleObjective.*;

public class FunctionsList {
    public ObjectiveFunction[] getFunctionList(){
        return new ObjectiveFunction[]{
                new AckleysFunction(),
                new BealeFunction(),
                new BoothsFunction(),
                new Bukin4Function(),
                new CrossInTrayFunction(),
                new EasomFunction(),
                new EggholderFunction(),
                new GoldsteinPrice(),
                new HimmelblausFunction(),
                new HolderTableFunction(),
                new LevyFunction(),
                new MatyasFunction(),
                new McCormickFunction(),
                new RastriginFunction(),
                new RosenbrockFunction(),
                new SchafferFunctionN4(),
                new SchafferFunction(),
                new SphereFunction(),
                new StyblinskiTangFunction(),
                new ThreeHumpCamelFunction(),
                new ChungReynoldsSquares(),
                new SumSquares()
        };
    }
}
