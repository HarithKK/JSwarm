package utils;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import examples.si.algo.fa.FA;
import examples.si.algo.tsoa.TSOA;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.util.Commons;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MongoClientConn {

    String connectionString;
    ServerApi serverApi;
    MongoClientSettings settings;

    String databaseName;
    public MongoClientConn(String db){
        //this.connectionString = "mongodb+srv://"+System.getenv("MONGO_UNAME")+":"+System.getenv("MONGO_PASSWORD")+"@cluster-tsoa-test.y7i2i.mongodb.net/?retryWrites=true&w=majority&appName=Cluster-tsoa-test";
        this.connectionString = "mongodb+srv://hkw2021:S5lfEVzu8h4aBocJ@cluster-tsoa-test.y7i2i.mongodb.net/?retryWrites=true&w=majority&appName=Cluster-tsoa-test";
        serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        this.databaseName = db;

    }

    public boolean runPing(){
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("admin");
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
                return true;
            } catch (MongoException e) {
                e.printStackTrace();
                return false;
            }finally {
                mongoClient.close();
            }
        }
    }

    public void updateAlgoTestResult(String testid, SIAlgorithm algorithm,  String testName, int tr){

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection("history");

            try {

                ArrayList<List> phistory = new ArrayList<>();

                for(int i=0; i<algorithm.getPHistory().size();i++){
                    phistory.add(Arrays.asList(algorithm.getPHistory().get(i).getPositionIndexes()));
                }

                Document gen= new Document()
                        .append("_id", new ObjectId())
                        .append("testid", testid)
                        .append("algorithm", algorithm.getName())
                        .append("function_name", testName)
                        .append("final_best_result", algorithm.getBestDoubleValue())
                        .append("final_best_position", Arrays.asList(algorithm.getGBest().getPositionIndexes()))
                        .append("best_result_history", algorithm.getHistory())
                        .append("time", algorithm.getNanoDuration())
                        .append("d", algorithm.getFunction().getNumberOfDimensions())
                        .append("convergence", algorithm.getConvergenceValue())
                        .append("term", tr);
                        //.append("p_history", phistory);

                if(algorithm instanceof TSOA){
                    TSOA t = (TSOA)algorithm;
                    gen.append("z", t.z_history);
                    double tt = t.t1 + t.t2 + t.t3;
                    gen.append("t1", t.t1/tt);
                    gen.append("t2", t.t2/tt);
                    gen.append("t3", t.t3/tt);
                    gen.append("zSize", t.zSize);
                    gen.append("minZ", t.minimumDistance);
                }

                InsertOneResult result = collection.insertOne(gen);
                // Prints the IDs of the inserted documents
                System.out.println("Inserted document ids: " + result.getInsertedId());

                // Prints a message if any exceptions occur during the operation
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }finally {
                mongoClient.close();
            }
        }
    }

    public void updateTestInfo(String testid, String description){

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection("test_info");

            try {

                InsertOneResult result = collection.insertOne(new Document()
                        .append("test_id", testid)
                        .append("test_description", description)
                        .append("execution_date", new Date().toString()));
                // Prints the IDs of the inserted documents
                System.out.println("Inserted document ids: " + result.getInsertedId());

                // Prints a message if any exceptions occur during the operation
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }finally {
                mongoClient.close();
            }
        }
    }


    public void updateAlgoFinalTestResult(String testid, String testName, String[] names, List<Double>[] algos, SIAlgorithm algorithm){

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection("final_result");

            try {
                List<Document> docs = new ArrayList<>();
                for(int i=0; i< algos.length; i++){
                    if(algos[i].size() == 0)
                        continue;
                    double[] dr = algos[i].stream().mapToDouble(t->t).toArray();
                    double[] dl = Commons.fill(algorithm.getObjectiveFunction().getExpectedBestValue(), algos.length);
                    docs.add(new Document()
                            .append("testid", testid)
                            .append("algorithm", names[i])
                            .append("test", testName)
                            .append("mean", algos[i].stream().mapToDouble(d -> (Double)d).average().getAsDouble())
                            .append("max", algos[i].stream().mapToDouble(d -> (Double)d).max().getAsDouble())
                            .append("min", algos[i].stream().mapToDouble(d -> (Double)d).min().getAsDouble())
                            .append("std", Utils.calcStd(algos[i]))
                            .append("pValue", Commons.calculatePValue(dr, dl))
                            .append("values", algos[i]));
                }

                InsertManyResult result = collection.insertMany(docs);
                // Prints the IDs of the inserted documents
                System.out.println("Inserted document ids: " + result.getInsertedIds());

                // Prints a message if any exceptions occur during the operation
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }finally {
                mongoClient.close();
            }
        }
    }

}
