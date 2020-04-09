package net.bamss.bamss.connections;

import java.util.Random;

public class KeygenConnection {
    public static String getKey() {
      Random rand = new Random();
      String key = Integer.toString(rand.nextInt());
      return key;
    }
}
