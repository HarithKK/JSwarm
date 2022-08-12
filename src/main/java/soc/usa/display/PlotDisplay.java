package soc.usa.display;

import org.usa.soc.Algorithm;
import org.usa.soc.ObjectiveFunction;
import org.usa.soc.aco.ACO;
import org.usa.soc.benchmarks.FunctionsList;
import org.usa.soc.benchmarks.singleObjective.AckleysFunction;
import org.usa.soc.mbo.MBO;
import org.usa.soc.ms.MS;
import org.usa.soc.pso.PSO;
import org.usa.soc.surfacePlotter.FunctionToMapper;
import org.usa.soc.surfacePlotter.Plot;
import org.usa.soc.util.Mathamatics;
import org.usa.soc.wso.WSO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlotDisplay {

    ObjectiveFunction fns[] = new FunctionsList().getFunctionList();
    FunctionChartPlotter fp;

    public PlotDisplay() {

        Scanner scanner = new Scanner(System.in);



        List<String> r = generateAlgo();
        for (int i=0;i<r.size();i++){
            System.out.println(i +": "+r.get(i));
        }
        System.out.println("Algorithm Id: ");
        int a = scanner.nextInt();
        System.out.println("Selected Algorithm Is "+ r.get(a));

        for (int i=0;i<fns.length;i++){
            System.out.println(i +": "+fns[i].getClass().getSimpleName());
        }
        System.out.println("Function Id: ");
        int f = scanner.nextInt();
        System.out.println("Selected Function Is "+ fns[f].getClass().getSimpleName());
        Algorithm ad = getAlgorithm(a,f);

        new org.usa.soc.surfacePlotter.FunctionDisplay(f);

        fp = new FunctionChartPlotter("Algorithm Viewer", 600, 600);
        fp.setChart(ad);
        fp.display();
        fp.execute();


    }

    private Algorithm getAlgorithm(int a, int f) {
        ObjectiveFunction fn = fns[f];
        switch (a){
            case 0 : return new PSO(
                    fn,
                    1000,
                    fn.getNumberOfDimensions(),
                    1500,
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
                double sr = Mathamatics.getMinimumDimensionDistance(fn.getMin(), fn.getMax(), fn.getNumberOfDimensions());
                return new org.usa.soc.gso.GSO(
                        fn,
                        fn.getNumberOfDimensions(),
                        1500,
                        1000,
                        fn.getMin(),
                        fn.getMax(),
                        5,
                        sr,
                        0.4,
                        0.6,
                        5,
                        sr,
                        0.08,
                        0.03,
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
                    100,
                    1000,
                    fn.getNumberOfDimensions(),
                    10,
                    fn.getMin(),
                    fn.getMax(),
                    0.2,
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
        }
        return null;
    }

    private List<String> generateAlgo() {
        List<String> algo = new ArrayList<>();

        algo.add("PSO");
        algo.add("ACO");
        algo.add("CSO");
        algo.add("GSO");
        algo.add("MBO");
        algo.add("MS");
        algo.add("WSO");

        return algo;
    }

    public static void main(String[] args) {
        new PlotDisplay();
    }
}
