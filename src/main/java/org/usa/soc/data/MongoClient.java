package org.usa.soc.data;

import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;

public class MongoClient {
    String connectionString;
    ServerApi serverApi;
    MongoClientSettings settings;

    String databaseName;
    public MongoClient(String db){
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
        try (com.mongodb.client.MongoClient mongoClient = MongoClients.create(settings)) {
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

    public void updateDocument(Document document, String collectionName){

        try (com.mongodb.client.MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            try {

                InsertOneResult result = collection.insertOne(document);
                System.out.println("Inserted document ids: " + result.getInsertedId());
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }finally {
                mongoClient.close();
            }
        }
    }
}
