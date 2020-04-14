package net.bamss.bamss.connections;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class IpToRegionConnection {
  private static String IP_TO_REGION_API = "http://ip-api.com/json";
  private static final String[] IP_HEADER_CANDIDATES = {
    "X-Forwarded-For",
    "Proxy-Client-IP",
    "WL-Proxy-Client-IP",
    "HTTP_X_FORWARDED_FOR",
    "HTTP_X_FORWARDED",
    "HTTP_X_CLUSTER_CLIENT_IP",
    "HTTP_CLIENT_IP",
    "HTTP_FORWARDED_FOR",
    "HTTP_FORWARDED",
    "HTTP_VIA",
    "REMOTE_ADDR"
  };

  private static String getClientIpAddress(HttpServletRequest request) {
    for (String header : IP_HEADER_CANDIDATES) {
      String ip = request.getHeader(header);
      if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
        return ip;
      }
    }
    return request.getRemoteAddr();
  }

  public static String getRegion(HttpServletRequest request) {
    String ipAddress = getClientIpAddress(request);
    HashMap<String, Object> jsonResponse = HttpConnection.get(IP_TO_REGION_API + "/" + ipAddress);
    System.out.println("ADDRESS : " + IP_TO_REGION_API + "/" + ipAddress);
    System.out.println("RESPONSE : " + jsonResponse);
    if (jsonResponse == null || jsonResponse.get("city") == null) {
      return "unknown";
    }
    return (String) jsonResponse.get("city");
  }
}
