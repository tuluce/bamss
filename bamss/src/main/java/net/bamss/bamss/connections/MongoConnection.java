package net.bamss.bamss.connections;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import org.bson.Document;

public class MongoConnection {

  private static MongoDatabase db;

  private static void createInstance() {
    MongoClientURI uri = new MongoClientURI(System.getenv("MONGODB_URI"));
    MongoClient mongoClient = new MongoClient(uri);
    db = mongoClient.getDatabase(uri.getDatabase());
    createIndices();
  }

  private static void createIndices() {
    MongoCollection<Document> collection = db.getCollection("urls");
    collection.createIndex(Indexes.hashed("key"));
    collection.createIndex(Indexes.ascending("creator"));
  }

  public static MongoDatabase getMongoDatabase() {
    if (db == null) {
      createInstance();
    }
    return db;
  }
}
