package net.bamss.bamss.connections;

import java.util.HashMap;

public class AnalyticsConnection {
  private static String ANALYTICS_SERVER = System.getenv("ANALYTICS_SERVER");

  public static void recordRedirect(String key, String platform, String region, String os) {
    HashMap<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("event_type", "redirect");
    jsonMap.put("key", key);
    jsonMap.put("platform", platform);
    jsonMap.put("region", region);
    jsonMap.put("os", os);
    HttpConnection.post(ANALYTICS_SERVER + "/event", jsonMap);
  }

  public static void recordShorten(String accountType) {
    HashMap<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("event_type", "shorten");
    jsonMap.put("account_type", accountType);
    HttpConnection.post(ANALYTICS_SERVER + "/event", jsonMap);
  }
}
