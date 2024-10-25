package examples.si;

import examples.si.algo.tsoa.Tree;
import examples.si.benchmarks.cec2005.ShiftedSphereFunction;
import examples.si.benchmarks.cec2018.MIDTLZ1;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.*;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.AckleysFunction;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.CrossInTrayFunction;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.GoldsteinPrice;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.McCormickFunction;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.StyblinskiTangFunction;
import examples.si.benchmarks.nonGeneral.classical.multimodal.nonseparable.ThreeHumpCamelFunction;
import examples.si.benchmarks.nonGeneral.classical.multimodal.separable.*;
import examples.si.benchmarks.nonGeneral.classical.multimodal.separable.Bukin4Function;
import examples.si.benchmarks.nonGeneral.classical.multimodal.separable.EasomFunction;
import examples.si.benchmarks.nonGeneral.classical.multimodal.separable.SphereFunction;
import examples.si.benchmarks.nonGeneral.classical.unimodal.nonseparable.*;
import examples.si.benchmarks.nonGeneral.classical.unimodal.nonseparable.BealeFunction;
import examples.si.benchmarks.nonGeneral.classical.unimodal.nonseparable.BoothsFunction;
import examples.si.benchmarks.nonGeneral.classical.unimodal.nonseparable.MatyasFunction;
import examples.si.benchmarks.nonGeneral.classical.unimodal.nonseparable.SchafferFunction;
import examples.si.benchmarks.nonGeneral.classical.unimodal.separable.ChungReynoldsSquares;
import examples.si.benchmarks.nonGeneral.classical.unimodal.separable.QuarticFunction;
import examples.si.benchmarks.nonGeneral.classical.unimodal.separable.SumSquares;
import examples.si.benchmarks.singleObjective.*;
import org.usa.soc.si.runners.FunctionsFactory;
import org.usa.soc.si.runners.Main;

public class AllDisplay {

    public static void main(String[] args) {

        Main.executeMain(new FunctionsFactory()
                .register(AckleysFunction.class)
                .register(BealeFunction.class)
                .register(BoothsFunction.class)
                .register(Bukin4Function.class)
                .register(CrossInTrayFunction.class)
                .register(EasomFunction.class)
                .register(EggholderFunction.class)
                .register(GoldsteinPrice.class)
                .register(HimmelblausFunction.class)
                .register(HolderTableFunction.class)
                .register(LevyFunction.class)
                .register(MatyasFunction.class)
                .register(McCormickFunction.class)
                .register(SchafferFunction.class)
                .register(SchafferFunctionN4.class)
                .register(SphereFunction.class)
                .register(ThreeHumpCamelFunction.class)
                .register(ChungReynoldsSquares.class)
                .register(SumSquares.class)
                .register(DixonPriceFunction.class)
                .register(Debfunction.class)
                .register(ZakharovFunction.class)
                .register(CsendesFunction.class)
                .register(QuarticFunction.class)
                .register(Michalewicz5.class)
                .register(Michalewicz10.class)
                .register(Trid.class)
                .register(Alpine1Function.class)
                .register(RastriginFunction.class)
                .register(StyblinskiTangFunction.class)
                .register(BentCigarFunction.class)
                .register(ShiftedSphereFunction.class)
                .register(MIDTLZ1.class)
                .build()
        );
    }
}
