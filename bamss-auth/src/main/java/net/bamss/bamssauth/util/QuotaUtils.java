package net.bamss.bamssauth.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bson.conversions.Bson;

import net.bamss.bamssauth.connections.MongoConnection;
import net.bamss.bamssauth.models.QuotaStatus;

public class QuotaUtils {
  private static final MongoDatabase db = MongoConnection.getMongoDatabase();
  private static final int STANDART_QUOTA = Integer.parseInt(System.getenv("STANDART_QUOTA"));
  private static final int BUSINESS_QUOTA = Integer.parseInt(System.getenv("BUSINESS_QUOTA"));

  public static int getQuota(String username) {
    MongoCollection<Document> collection = db.getCollection("user");
    Document result = collection.find(Filters.eq("username", username)).first();
    String accountType = (String) result.get("account_type");
    Document quotaStatus = (Document) result.get("quota_status");
    Date lastUsage = (Date) quotaStatus.get("last_usage");
    Integer usageCount = (Integer) quotaStatus.get("usage_count");
    Date now = new Date();
    if (!isSameDay(lastUsage, now)) {
      Bson update = Updates.set("quota_status", new QuotaStatus(0, null).getDocument());
      collection.updateOne(Filters.eq("username", username), update);
      return getMaxQuota(accountType);
    }
    return getMaxQuota(accountType) - usageCount;
  }

  public static void useQuota(String username) {
    MongoCollection<Document> collection = db.getCollection("user");
    Document result = collection.find(Filters.eq("username", username)).first();
    Document quotaStatus = (Document) result.get("quota_status");
    Integer usageCount = (Integer) quotaStatus.get("usage_count");
    Date now = new Date();
    Bson update = Updates.set("quota_status", new QuotaStatus(usageCount + 1, now).getDocument());
    collection.updateOne(Filters.eq("username", username), update);
  }

  private static int getMaxQuota(String accountType) {
    if (accountType.equals("standart")) {
      return STANDART_QUOTA;
    }
    if (accountType.equals("business")) {
      return BUSINESS_QUOTA;
    }
    return 0;
  }

  private static boolean isSameDay(Date date1, Date date2) {
    if (date1 == null || date2 == null) {
      return false;
    }
    LocalDate localDate1 = date1.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDate();
    LocalDate localDate2 = date2.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDate();
    return localDate1.isEqual(localDate2);
  }
}
