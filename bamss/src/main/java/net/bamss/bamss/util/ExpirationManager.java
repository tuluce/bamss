package net.bamss.bamss.util;

import java.time.Instant;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.bamss.bamss.connections.MongoConnection;

@Service
public class ExpirationManager {
  private static final long DISABLE_PERIOD = 2628000000L; // 1 month
  private static final long SCHEDULE_RATE = 86400000L; // 1 day
  private static final MongoDatabase db = MongoConnection.getMongoDatabase();
  
  private static void deleteDisabledUrls() {
    MongoCollection<Document> collection = db.getCollection("urls");
    collection.deleteMany(Filters.and(
      Filters.eq("disabled", 1),
      Filters.lt("expireDate", Instant.now().plusMillis(DISABLE_PERIOD).toEpochMilli())
    ));
  }

  private static void disableExpiredUrls() {
    MongoCollection<Document> collection = db.getCollection("urls");
    collection.updateMany(Filters.and(
      Filters.eq("disabled", 0),
      Filters.lt("expireDate", Instant.now().toEpochMilli())
    ),
      Updates.set("disabled", 1)
    );
  }

  @Scheduled(fixedRate = SCHEDULE_RATE)
  public static void schedule() {
    disableExpiredUrls();
    deleteDisabledUrls();
  }
}
