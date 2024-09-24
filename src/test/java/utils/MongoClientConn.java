package utils;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import examples.si.algo.fa.FA;
import org.bson.Document;
import org.junit.jupiter.api.Test;

public class MongoClientConn {

    String connectionString;
    ServerApi serverApi;
    MongoClientSettings settings;

    public MongoClientConn(){
        this.connectionString = "mongodb+srv://"+System.getenv("MONGO_UNAME")+":"+System.getenv("MONGO_PASSWORD")+"@cluster-tsoa-test.y7i2i.mongodb.net/?retryWrites=true&w=majority&appName=Cluster-tsoa-test";
        serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
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

    @Test
    public void testMongoConnection(){
        MongoClientConn con = new MongoClientConn();
        System.out.println(con.runPing());
    }
}
