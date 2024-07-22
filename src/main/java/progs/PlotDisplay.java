package progs;

import examples.si.AlgorithmFactory;
import org.usa.soc.si.runners.FunctionDisplay;
import org.usa.soc.si.Algorithm;
import org.usa.soc.si.ObjectiveFunction;
import examples.si.benchmarks.FunctionsList;
import org.usa.soc.si.view.FunctionChartPlotter;

import java.util.List;
import java.util.Scanner;

public class PlotDisplay {

    public static void main(String[] args) {
        new PlotDisplay();
    }

    private int TIME = 50;
    FunctionChartPlotter fp;
    ObjectiveFunction fns[] = new FunctionsList().getFunctionList();

    public PlotDisplay() {

        Scanner scanner = new Scanner(System.in);

        List<String> r = AlgorithmFactory.generateAlgo();
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
        Algorithm ad = new AlgorithmFactory(a, fns[f]).getAlgorithm(100, 100);

        new FunctionDisplay(f);

        fp = new FunctionChartPlotter("Algorithm Viewer", 600, 600);
        if(a==7){
            fp.setTime(10);
        }
        fp.setTime(TIME);
        fp.setChart(ad);
        fp.display();
        fp.execute();
    }


}
