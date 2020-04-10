package net.bamss.bamssanalytics.util;

import java.sql.Connection;
import java.sql.Statement;

import net.bamss.bamssanalytics.connections.PostgreConnection;

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
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public static void insertEvent(String eventType, String accountType,
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

  public static void insertEvent(String eventType, String accountType) {
    insertEvent(eventType, accountType, null, null, null, null);
  }
}
