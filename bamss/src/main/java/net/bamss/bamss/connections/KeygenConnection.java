package net.bamss.bamss.connections;

import java.util.HashMap;

public class KeygenConnection {
    private static String KEYGEN_SERVER = System.getenv("KEYGEN_SERVER");

    public static String getKey() {
        HashMap<String, Object> jsonResponse = HttpConnection.get(KEYGEN_SERVER);
        if (jsonResponse == null) {
            return null;
        }
        return (String) jsonResponse.get("key");
    }
}
