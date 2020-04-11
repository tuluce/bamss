package net.bamss.bamssanalytics.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import net.bamss.bamssanalytics.connections.PostgreConnection;
import net.bamss.bamssanalytics.models.UserAnalytics;

public class DatabaseUtils {
  private static Connection db = PostgreConnection.getDatabase();

  static {
    try {
      Statement st = db.createStatement();
      st.execute("CREATE TABLE IF NOT EXISTS events("
        + "event_date timestamp,"
        + "event_type VARCHAR (16) NOT NULL,"
        + "account_type VARCHAR (16) NOT NULL,"
        + "key VARCHAR (64),"
        + "platform VARCHAR (16),"
        + "locale VARCHAR (16),"
        + "os VARCHAR (16));"
      );
      st.close();
      Statement st2 = db.createStatement();
      st2.execute("SET TIMEZONE='Europe/Istanbul';");
      st2.close();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private static void insertEvent(String eventType, String accountType,
      String key, String platform, String locale, String os) {
    try {
      Statement st = db.createStatement();
      st.execute("INSERT INTO " 
        + "events(event_date, event_type, account_type, key, platform, locale, os) "
        + String.format("VALUES(CURRENT_TIMESTAMP, '%s', '%s', '%s', '%s', '%s', '%s');",
            eventType, accountType, key, platform, locale, os)
      );
      st.close();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public static void insertUserEvent(String eventType,
      String key, String platform, String locale, String os) {
    insertEvent(eventType, null, key, platform, locale, os);
  }

  public static void insertAdminEvent(String eventType, String accountType) {
    insertEvent(eventType, accountType, null, null, null, null);
  }

  private static String formatDate(long dateTs) {
    Timestamp timestamp = new Timestamp(dateTs);  
    Date date = new Date(timestamp.getTime());  
    String pattern = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    String dateString = simpleDateFormat.format(date);
    return dateString;
  }

  public static UserAnalytics getUserLevelAnalytics(List<String> keys, long startDateTs, long endDateTs) {
    try {
      String startDate = formatDate(startDateTs);
      String endDate = formatDate(endDateTs);
      Statement st = db.createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM events WHERE "
        + String.format("key IN %s AND ", formatKeys(keys))
        + String.format("event_date BETWEEN '%s' AND '%s'", startDate, endDate)
      );
      UserAnalytics analytics = new UserAnalytics();
      while (rs.next()) {
        String key = rs.getString("key");
        analytics.addAnalytic(key, "platform", rs.getString("platform"));
        analytics.addAnalytic(key, "locale", rs.getString("locale"));
        analytics.addAnalytic(key, "os", rs.getString("os"));
        analytics.addAnalytic(key, "total", "total");
      }
      rs.close();
      st.close();
      return analytics;
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return null;
  }

  private static String formatKeys(List<String> keys) {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (int i = 0; i < keys.size(); i++) {
      sb.append("'");
      sb.append(keys.get(i));
      sb.append("'");
      if (i < keys.size() - 1) {
        sb.append(",");
      }
    }
    sb.append(")");
    return sb.toString();
  }
}
