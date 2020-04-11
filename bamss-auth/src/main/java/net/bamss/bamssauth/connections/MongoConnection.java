package net.bamss.bamssauth.connections;

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
    MongoCollection<Document> collection = db.getCollection("user");
    collection.createIndex(Indexes.hashed("username"));
    collection.createIndex(Indexes.hashed("email"));
  }

  public static MongoDatabase getMongoDatabase() {
    if (db == null) {
      createInstance();
    }
    return db;
  }
}
