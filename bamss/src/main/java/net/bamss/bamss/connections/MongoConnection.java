package net.bamss.bamss.connections;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;


public class MongoConnection {

  private static MongoDatabase db;

  private static void createInstance() {
    MongoClientURI uri = new MongoClientURI(System.getenv("MONGODB_URI"));
    MongoClient mongoClient = new MongoClient(uri);
    db = mongoClient.getDatabase(uri.getDatabase());
  }

  public static MongoDatabase getMongoDatabase() {
    if (db == null) {
      createInstance();
    }
    return db;
  }
}
