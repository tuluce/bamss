package net.bamss.bamssauth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mongodb.client.MongoDatabase;

import net.bamss.bamssauth.connections.MongoConnection;

public class AuthUtils {
  private static final MongoDatabase db = MongoConnection.getMongoDatabase();
  private static final String CRYPTO_SECRET = System.getenv("CRYPTO_SECRET");
  private static final String CRYPTO_HEADER = System.getenv("CRYPTO_HEADER");
  private static final Algorithm CRYPTO_ALGORITHM = Algorithm.HMAC256(CRYPTO_SECRET);
  private static final int CRYPTO_SIGNATURE_LEN = 43;

  public static String getToken(String username) {
    String token = JWT.create()
      .withSubject(username)
      .sign(CRYPTO_ALGORITHM);
    return token;
  }

  public static String getApiKey(String username) {
    String token = JWT.create()
      .withSubject(username)
      .sign(CRYPTO_ALGORITHM);
    String apiKey = token.substring(CRYPTO_HEADER.length()).replaceAll("\\.", "");
    return apiKey;
  }

  public static String validateToken(String token) {
    try {
      JWTVerifier verifier = JWT.require(CRYPTO_ALGORITHM).build();
      DecodedJWT jwt = verifier.verify(token);
      return jwt.getSubject();
    } catch (JWTVerificationException exception) {
      return null;
    }
  }

  public static String validateApiKey(String apiKey) {
    try {
      String tokenBody = apiKey.substring(0, apiKey.length() - CRYPTO_SIGNATURE_LEN);
      String tokenSignature = apiKey.substring(apiKey.length() - CRYPTO_SIGNATURE_LEN);
      String token = CRYPTO_HEADER + "." + tokenBody + "." + tokenSignature;
      JWTVerifier verifier = JWT.require(CRYPTO_ALGORITHM).build();
      DecodedJWT jwt = verifier.verify(token);
      return jwt.getSubject();
    } catch (JWTVerificationException exception) {
      return null;
    }
  }

  public static boolean checkQuota(String username) {
    db.getCollection("user");
    return true;
  }
}
