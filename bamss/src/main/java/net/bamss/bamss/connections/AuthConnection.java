package net.bamss.bamss.connections;

import net.bamss.bamss.models.Validation;

public class AuthConnection {
  public static Validation validateToken(String token) {
    return new Validation("my_standart_user", true);
  }

  public static Validation validateApiKey(String apiKey) {
    return new Validation("my_standart_user", true);
  }
}
