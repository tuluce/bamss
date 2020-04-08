package net.bamss.bamss.configurations;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfiguration {
  @Bean(name = "db")
  public MongoDatabase getMongoDatabase() throws Exception {
    MongoClientURI uri = new MongoClientURI(System.getenv("MONGODB_URI"));
    MongoClient mongoClient = new MongoClient(uri);
    MongoDatabase db = mongoClient.getDatabase(System.getenv("MONGODB_DB"));
    return db;
  }
}
