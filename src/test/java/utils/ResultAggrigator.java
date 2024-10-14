package utils;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.util.Commons;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static utils.Utils.calcStd;

public class ResultAggrigator {

    public static final int REP_COUNT = 30;
    public static final int STEPS = 500;
    public static final int POPULATION = 30;

    private String dbName = "tsoa-data";

    private String testDescription = "uni modal and multi modal 30 rounds";
    DB db;
    private static ResultAggrigator instance;

    MongoClientConn mongoClientConn;

    private static UUID uuid;

    public static ResultAggrigator getInstance(){
        if(instance == null){
            instance = new ResultAggrigator();
            uuid = UUID.randomUUID();
            update_test_info();
        }
        return instance;
    }

    public static void clear(){
        instance = null;
    }

    private static void update_test_info() {
        try{
            instance.db.connect();
            instance.db.addTestInfo(uuid.toString(), getInstance().testDescription);
            instance.db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        instance.mongoClientConn.updateTestInfo(uuid.toString(), getInstance().testDescription);
    }

    public void resetDb(String name){
        this.dbName = name;
        instance = null;
        db.close();
    }

    private ResultAggrigator(){
        mongoClientConn = new MongoClientConn(dbName);
        db = new DB();
        db.createTables();
    }

    public void aggrigateInfo(String testName, String[] names, List<Double>[] algos, SIAlgorithm algorithm){
        mongoClientConn.updateAlgoFinalTestResult(uuid.toString(),testName,names, algos, algorithm);

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<algos.length; i++){
            sb.append(",");

            if(algos[i].size() == 0){
                sb.append("NULL");
            }else{
                sb.append(algos[i].stream().mapToDouble(d -> (Double)d).average().getAsDouble());
            }

        }
        Utils.writeToFile("data/result_mean.csv",uuid.toString() +","+ testName + sb.toString() + "\n");

        sb = new StringBuilder();
        for(int i=0; i<algos.length; i++){
            sb.append(",");
            if(algos[i].size() == 0){
                sb.append("NULL");
            }else {
                sb.append(calcStd(algos[i]));
            }
        }
        Utils.writeToFile("data/result_std.csv",uuid.toString() +","+testName + sb.toString() + "\n");

        sb = new StringBuilder();
        for(int i=0; i<algos.length; i++){
            sb.append(",");
            if(algos[i].size() == 0){
                sb.append("NULL");
            }else {
                sb.append(algos[i].stream().mapToDouble(d -> (Double) d).min().getAsDouble());
            }
        }
        Utils.writeToFile("data/result_min.csv",uuid.toString() +","+testName + sb.toString() + "\n");

        sb = new StringBuilder();
        for(int i=0; i<algos.length; i++){
            sb.append(",");
            if(algos[i].size() == 0){
                sb.append("NULL");
            }else {
                sb.append(algos[i].stream().mapToDouble(d -> (Double) d).max().getAsDouble());
            }
        }
        Utils.writeToFile("data/result_max.csv",uuid.toString() +","+testName + sb.toString() + "\n");

        sb = new StringBuilder();
        for(int i=0; i<algos.length; i++){
            sb.append(",");
            if(algos[i].size() == 0){
                sb.append("NULL");
            }else {
                double[] dr = algos[i].stream().mapToDouble(t -> t).toArray();
                double[] dl = Commons.fill(algorithm.getObjectiveFunction().getExpectedBestValue(), algos.length);
                sb.append(Commons.calculatePValue(dl, dr));
            }
        }
        Utils.writeToFile("data/pValue.csv",uuid.toString() +","+testName + sb.toString() + "\n");

        try {
            db.connect();
            for(int i=0; i<algos.length; i++){
                if(algos[i].size() == 0){
                    continue;
                }
                double[] dr = algos[i].stream().mapToDouble(t->t).toArray();
                double[] dl = Commons.fill(algorithm.getObjectiveFunction().getExpectedBestValue(), algos.length);
                db.addFinal(names[i], testName,
                        uuid.toString(),
                        algos[i].stream().mapToDouble(d -> (Double)d).average().getAsDouble(),
                        algos[i].stream().mapToDouble(d -> (Double)d).max().getAsDouble(),
                        algos[i].stream().mapToDouble(d -> (Double)d).min().getAsDouble(),
                        calcStd(algos[i]),
                        Commons.calculatePValue(dr, dl),
                        10);
            }

            db.addFinal(testName, testName, uuid.toString(),algos, algorithm,10);
            db.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTestResuly(SIAlgorithm algorithm, String testName,  int tr){
        try{
            db.connect();
            db.addData(uuid.toString(), algorithm.getName(), testName, algorithm.getBestDoubleValue(),tr);
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        mongoClientConn.updateAlgoTestResult(uuid.toString(), algorithm, testName,tr);
    }

    public void updateDocument(Document document){
        document.append("_id", new ObjectId()).append("testid", uuid.toString());
        mongoClientConn.updateDocument(document);
    }
}
