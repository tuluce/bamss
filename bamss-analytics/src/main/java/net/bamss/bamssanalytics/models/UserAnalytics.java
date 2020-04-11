package net.bamss.bamssanalytics.models;

import java.util.HashMap;
import java.util.Map;

public class UserAnalytics {
  private final Map<String, Map<String, Map<String, Integer>>> data;

  public UserAnalytics() {
    data = new HashMap<>();
  }

  public void addAnalytic(String key, String analyticName, String value) {
    if (!data.containsKey(key)) {
      data.put(key, new HashMap<>());
    }
    Map<String, Map<String, Integer>> analyticMap = data.get(key);
    if (!analyticMap.containsKey(analyticName)) {
      analyticMap.put(analyticName, new HashMap<>());
    }
    Map<String, Integer> valueMap = analyticMap.get(analyticName);
    if (!valueMap.containsKey(value)) {
      valueMap.put(value, 0);
    }
    valueMap.put(value, valueMap.get(value) + 1);
  }

  public Map<String, Map<String, Map<String, Integer>>> getData() {
    return data;
  }
}
