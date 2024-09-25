package TSOA_Quatitative_Test;

import examples.si.algo.also.ALSO;
import examples.si.algo.tsoa.TSOA;
import examples.si.algo.tsoa.Tree;
import examples.si.benchmarks.DynamicUnimodalObjectiveFunctions.Function1;
import examples.si.benchmarks.nonGeneral.classical.unimodal.separable.StepFunction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.usa.soc.core.AbsAgent;
import org.usa.soc.core.action.StepAction;
import org.usa.soc.core.ds.Vector;
import org.usa.soc.multiagent.StepCompleted;
import org.usa.soc.si.ObjectiveFunction;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.util.PrintToFile;
import utils.Serializer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class TSOAStepTest {

    int nd = 30;
    int populationSize = 30;
    int stepCount = 500;
    ObjectiveFunction function = new Function1(nd);
    int itr = 10;


    public static void beforeAll(){
        DB db = new DB();
        db.connect();
        db.createTable("best_values", "data FLOAT");
        db.createTable("exe_time", "data FLOAT");
        db.createTable("p_history", "x FLOAT, y FLOAT, z FLOAT, rest Varchar(1000)");
        db.createTable("lambda", "agent varchar(60), data FLOAT");
        db.createTable("z_tree", "data FLOAT");
        db.createTable("p_z_tree", "x FLOAT, y FLOAT, z FLOAT");
        db.createTable("totalSproutedSeedsCount", "data INT(64)");
        db.close();
    }
    public void TestTSOA() throws Exception {
        DB db = new DB();
        db.connect();
        TSOA algo = new TSOA(
                function,
                populationSize,
                stepCount,
                function.getNumberOfDimensions(),
                function.getMin(),
                function.getMax(),
                true,
                10,
                0.7,
                2.05,
                0.1
        );

        algo.addStepAction(new StepAction() {
            @Override
            public void performAction(Vector best, Double bestValue, int step) {
                PreparedStatement pr = null;
                try {
                    pr = db.getStatement("best_values", "data", "?");
                    pr.setString(1, algo.getName());
                    pr.setString(2, function.getClass().getSimpleName());
                    pr.setDouble(3, algo.getBestDoubleValue());
                    pr.setInt(4, (int) step);
                    db.execute(pr);

                    pr = db.getStatement("exe_time", "data", "?");
                    pr.setString(1, algo.getName());
                    pr.setString(2, function.getClass().getSimpleName());
                    pr.setDouble(3, algo.getNanoDuration());
                    pr.setInt(4, (int) step);
                    db.execute(pr);

                    pr = db.getStatement("p_history", "x,y,z,rest", "?,?,?,?");
                    pr.setString(1, algo.getName());
                    pr.setString(2, function.getClass().getSimpleName());
                    pr.setDouble(3, algo.getGBest().getValue(0));
                    pr.setDouble(4, algo.getGBest().getValue(1));
                    pr.setDouble(5, algo.getGBest().getValue(2));
                    pr.setString(6, algo.getGBest().toStringArray());
                    pr.setInt(7, (int) step);
                    db.execute(pr);

                    for(AbsAgent agent : algo.getAgents("trees").getAgents()){
                        Tree t = (Tree)agent;
                        pr = db.getStatement("lambda", "agent,data", "?,?");
                        pr.setString(1, algo.getName());
                        pr.setString(2, function.getClass().getSimpleName());
                        pr.setString(3, t.getId());
                        pr.setDouble(4, t.getLambda());
                        pr.setInt(5, (int) step);
                        db.execute(pr);
                    }

                    pr = db.getStatement("z_tree", "data", "?");
                    pr.setString(1, algo.getName());
                    pr.setString(2, function.getClass().getSimpleName());
                    pr.setDouble(3, function.setParameters(algo.z.getPositionIndexes()).call());
                    pr.setInt(4, (int) step);
                    db.execute(pr);

                    pr = db.getStatement("p_z_tree", "x,y,z,rest", "?,?,?,?");
                    pr.setString(1, algo.getName());
                    pr.setString(2, function.getClass().getSimpleName());
                    pr.setDouble(3, algo.z.getValue(0));
                    pr.setDouble(4, algo.z.getValue(1));
                    pr.setDouble(5, algo.z.getValue(2));
                    pr.setString(6, algo.z.toStringArray());
                    pr.setInt(7, (int) step);
                    db.execute(pr);

                    pr = db.getStatement("totalSproutedSeedsCount", "data", "?");
                    pr.setString(1, algo.getName());
                    pr.setString(2, function.getClass().getSimpleName());
                    pr.setDouble(3, algo.totalSproutedSeedsCount);
                    pr.setInt(4, (int) step);
                    db.execute(pr);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        algo.initialize();
        algo.run();
        System.out.println(algo.getBestDoubleValue());
        db.close();
    }
    public void testALSO() throws Exception {
        ALSO algo = new ALSO(
                function,
                populationSize,
                stepCount,
                function.getNumberOfDimensions(),
                function.getMin(),
                function.getMax(),
                true,
                10, 210,
                2.5,
                1.0,
                10,
                10
        );

        algo.initialize();
        algo.run();

        System.out.println(algo.getBestDoubleValue());
    }
}
