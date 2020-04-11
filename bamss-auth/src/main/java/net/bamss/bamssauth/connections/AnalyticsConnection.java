package net.bamss.bamssauth.connections;

import java.util.HashMap;

public class AnalyticsConnection {
  private static String ANALYTICS_SERVER = System.getenv("ANALYTICS_SERVER");

  public static void recordLogin(String accountType) {
    HashMap<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("event_type", "login");
    jsonMap.put("account_type", accountType);
    HttpConnection.post(ANALYTICS_SERVER + "/event", jsonMap);
  }

  public static void recordSignup(String accountType) {
    HashMap<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("event_type", "signup");
    jsonMap.put("account_type", accountType);
    HttpConnection.post(ANALYTICS_SERVER + "/event", jsonMap);
  }
}
