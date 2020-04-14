package net.bamss.bamss.connections;

import java.util.HashMap;

public class IpToRegionConnection {
  private static String IP_TO_REGION_API = "http://ip-api.com/json";

  public static String getRegion(String ipAddress) {
    HashMap<String, Object> jsonResponse = HttpConnection.get(IP_TO_REGION_API + "/" + ipAddress);
    System.out.println("ADDRESS : " + IP_TO_REGION_API + "/" + ipAddress);
    System.out.println("RESPONSE : " + jsonResponse);
    if (jsonResponse == null || jsonResponse.get("city") == null) {
      return "unknown";
    }
    return (String) jsonResponse.get("city");
  }
}
