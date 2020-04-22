package net.bamss.bamssanalytics.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAnalytics {
  private final int eventsLength;
  private final Map<String, Map<String, List<Integer>>> data;

  public AdminAnalytics(int eventsLength) {
    this.eventsLength = eventsLength;
    this.data = new HashMap<>();
  }

  public void addAnalytic(String eventType, String accountType, int eventIndex) {
    if (accountType.equals("null")) {
      accountType = "total";
    }
    if (!data.containsKey(eventType)) {
      data.put(eventType, new HashMap<>());
    }
    Map<String, List<Integer>> eventMap = data.get(eventType);
    if (!eventMap.containsKey(accountType)) {
      ArrayList<Integer> counts = new ArrayList<>();
      for (int i = 0; i < eventsLength; i++) {
        counts.add(0);
      }
      eventMap.put(accountType, counts);
    }
    List<Integer> counts = eventMap.get(accountType);
    if (eventIndex > counts.size()) {
      eventIndex = counts.size() - 1;
    }
    counts.set(eventIndex, counts.get(eventIndex) + 1);
  }

  public  Map<String, Map<String, List<Integer>>> getData() {
    return data;
  }
}
