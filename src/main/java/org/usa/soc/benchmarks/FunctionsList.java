package org.usa.soc.benchmarks;

import org.usa.soc.ObjectiveFunction;
import org.usa.soc.benchmarks.singleObjective.*;
import org.usa.soc.benchmarks.singleObjectiveConstrained.RosenbrockFunctionDish;

public class FunctionsList {
    public ObjectiveFunction[] getFunctionList(){
        return new ObjectiveFunction[]{
                new AckleysFunction(),
                new BealeFunction(),
                new BoothsFunction(),
                new BukinFunction(),
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
                new RosenbrockFunctionDish(),
                new SchafferFunctionN4(),
                new SchafferFunction(),
                new SphereFunction(),
                new StyblinskiTangFunction(),
                new ThreeHumpCamelFunction()
        };
    }
}
