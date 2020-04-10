package net.bamss.bamss.connections;

import java.util.HashMap;

import net.bamss.bamss.models.Validation;

public class AuthConnection {
  private static String AUTH_SERVER = System.getenv("AUTH_SERVER");

  private static Validation validateEntity(String authEntity, String entityType) {
    HashMap<String, Object> jsonMap = new HashMap<>();
    jsonMap.put(entityType, authEntity);
    HashMap<String, Object> jsonResponse = HttpConnection.post(AUTH_SERVER + "/validate", jsonMap);
    if (jsonResponse == null) {
      return null;
    }
    String username = (String) jsonResponse.get("username");
    Boolean hasQuota = (Boolean) jsonResponse.get("hasQuota");
    return new Validation(username, hasQuota);
  }

  public static Validation validateToken(String token) {
    return validateEntity(token, "token");
  }

  public static Validation validateApiKey(String apiKey) {
    return validateEntity(apiKey, "api_key");
  }
}
