package org.usa.soc.data;

import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoClient {
    String connectionString;
    ServerApi serverApi;
    MongoClientSettings settings;

    String databaseName;
    public MongoClient(String db){
        //this.connectionString = "mongodb+srv://"+System.getenv("MONGO_UNAME")+":"+System.getenv("MONGO_PASSWORD")+"@cluster-tsoa-test.y7i2i.mongodb.net/?retryWrites=true&w=majority&appName=Cluster-tsoa-test";
        this.connectionString = "mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+2.1.1";
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

    public ObjectId insertDocument(Document document, String collectionName){

        try (com.mongodb.client.MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            try {

                InsertOneResult result = collection.insertOne(document);
                System.out.println("Inserted document ids: " + result.getInsertedId());
                return result.getInsertedId().asObjectId().getValue();
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }finally {
                mongoClient.close();
            }
        }
        return null;
    }

    public ObjectId updateInserDocument(ObjectId objectId, Document document, String key, String collectionName){

        try (com.mongodb.client.MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            try {
                UpdateResult ur = collection.updateOne(Filters.eq("_id", objectId), Updates.set(key, document));
                System.out.println("Inserted update ids: " + objectId);
                return objectId;
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }finally {
                mongoClient.close();
            }
        }
        return null;
    }
}
