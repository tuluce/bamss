package net.bamss.bamssanalytics.connections;

import java.util.HashMap;

public class AuthConnection {
  private static String AUTH_SERVER = System.getenv("AUTH_SERVER");

  private static String validateEntity(String authEntity, String entityType) {
    HashMap<String, Object> jsonMap = new HashMap<>();
    jsonMap.put(entityType, authEntity);
    HashMap<String, Object> jsonResponse = HttpConnection.post(AUTH_SERVER + "/validate", jsonMap);
    if (jsonResponse == null) {
      return null;
    }
    String username = (String) jsonResponse.get("username");
    return username;
  }

  public static String validateToken(String token) {
    return validateEntity(token, "token");
  }

  public static String validateApiKey(String apiKey) {
    return validateEntity(apiKey, "api_key");
  }

  public static boolean validateAdminKey(String adminKey) {
    return "admin".equals(validateEntity(adminKey, "admin_key"));
  }
}
