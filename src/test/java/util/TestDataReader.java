package util;

import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;

public class TestDataReader {

    private static JSONObject testData;

    // Load JSON once (Singleton style)
    static {
        try {
            FileReader reader = new FileReader("src/test/resources/testdata.json");
            JSONTokener tokener = new JSONTokener(reader);
            testData = new JSONObject(tokener);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data JSON file", e);
        }
    }

    /**
     * Get value using dot notation
     * Example: login.invalid.errorMessage
     */
    public static String get(String key) {
        String[] keys = key.split("\\.");
        JSONObject current = testData;

        for (int i = 0; i < keys.length - 1; i++) {
            current = current.getJSONObject(keys[i]);
        }

        return current.get(keys[keys.length - 1]).toString();
    }

    /**
     * Optional: Get entire JSON object
     */
    public static JSONObject getJson() {
        return testData;
    }
}