package net.bamss.bamss.connections;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpConnection {
  public static HashMap<String, Object> post(String url, HashMap<String, Object> jsonMap) {
    try {
      MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(jsonMap);
      OkHttpClient client = new OkHttpClient();
      RequestBody body = RequestBody.create(jsonString, JSON_TYPE);
      Request request = new Request.Builder()
          .url(url)
          .post(body)
          .build();
      try (Response response = client.newCall(request).execute()) {
        String responseString = response.body().string();
        if (responseString.isEmpty()) {
          return null;
        }
        HashMap<String, Object> responseMap = objectMapper.readValue(responseString, HashMap.class);
        return responseMap;
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return null;
  }

  public static HashMap<String, Object> get(String url) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      OkHttpClient client = new OkHttpClient();
      Request request = new Request.Builder()
              .url(url)
              .build();

      try (Response response = client.newCall(request).execute()) {
        String responseString = response.body().string();
        if (responseString.isEmpty()) {
          return null;
        }
        HashMap<String, Object> responseMap = objectMapper.readValue(responseString, HashMap.class);
        return responseMap;
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return null;
  }
}
