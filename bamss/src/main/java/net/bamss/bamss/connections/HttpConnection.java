package net.bamss.bamss.connections;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpConnection {
  public static HashMap<String,String> post(String url, HashMap<String,String> jsonMap) {
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
        HashMap<String,String> responseMap = objectMapper.readValue(responseString, HashMap.class);
        return responseMap;
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return null;
  }
}
