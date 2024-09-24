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

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoClientConn {

    String connectionString;
    ServerApi serverApi;
    MongoClientSettings settings;

    String databaseName;

    public MongoClientConn(String db){
        this.connectionString = "mongodb+srv://"+System.getenv("MONGO_UNAME")+":"+System.getenv("MONGO_PASSWORD")+"@cluster-tsoa-test.y7i2i.mongodb.net/?retryWrites=true&w=majority&appName=Cluster-tsoa-test";
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
            }
        }
    }

    public void updateAlgoTestResult(SIAlgorithm algorithm){

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
                        .append("algorithm", algorithm.getName())
                        .append("function_name", algorithm.getFunction().getClass().getSimpleName())
                        .append("final_best_result", algorithm.getBestDoubleValue())
                        .append("final_best_position", Arrays.asList(algorithm.getGBest().getPositionIndexes()))
                        .append("best_result_history", algorithm.getHistory())
                        .append("time", algorithm.getNanoDuration())
                        .append("d", algorithm.getFunction().getNumberOfDimensions())
                        .append("convergence", algorithm.getConvergenceValue());
                        //.append("p_history", phistory);

                if(algorithm instanceof TSOA){
                    TSOA t = (TSOA)algorithm;
                    gen.append("z", t.z_history);
                }

                InsertOneResult result = collection.insertOne(gen);
                // Prints the IDs of the inserted documents
                System.out.println("Inserted document ids: " + result.getInsertedId());

                // Prints a message if any exceptions occur during the operation
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }
        }
    }

    public void updateAlgoFinalTestResult(String testName, String[] names, List<Double>[] algos){

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection("final_result");

            try {
                List<Document> docs = new ArrayList<>();
                for(int i=0; i< algos.length; i++){
                    docs.add(new Document()
                            .append("test", testName)
                            .append("algorithm", names[i])
                            .append("mean", algos[i].stream().mapToDouble(d -> (Double)d).average().getAsDouble())
                            .append("max", algos[i].stream().mapToDouble(d -> (Double)d).max().getAsDouble())
                            .append("min", algos[i].stream().mapToDouble(d -> (Double)d).min().getAsDouble())
                            .append("std", Utils.calcStd(algos[i]))
                            .append("values", algos[i]));
                }

                InsertManyResult result = collection.insertMany(docs);
                // Prints the IDs of the inserted documents
                System.out.println("Inserted document ids: " + result.getInsertedIds());

                // Prints a message if any exceptions occur during the operation
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }
        }
    }

}
