package net.bamss.bamssanalytics.connections;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;

public class PostgreConnection {
  private static Connection db;

  private static void createInstance() {
    try {
      URI dbUri = new URI(System.getenv("DATABASE_URL"));
      String username = dbUri.getUserInfo().split(":")[0];
      String password = dbUri.getUserInfo().split(":")[1];
      String dbUrl = "jdbc:postgresql://" 
        + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()
        + "?ssl=true&sslmode=require";
      db = DriverManager.getConnection(dbUrl, username, password);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public static Connection getDatabase() {
    if (db == null) {
      createInstance();
    }
    return db;
  }
}
