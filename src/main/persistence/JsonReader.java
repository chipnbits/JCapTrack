package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Stream;

import org.json.*;

/*
This JSON implementation has  borrowed some code structure and concepts from JsonSerializationDemo UBC CPSC 210
 */

// Represents a reader that reads Portfolio from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }


    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: reads portfolio from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Portfolio readPortfolio() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePortfolio(jsonObject);
    }

    // EFFECTS: parses Portfolio from JSON object and returns it
    private Portfolio parsePortfolio(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Portfolio p = new Portfolio(name);

        addSecurities(p, jsonObject);
        parseTransactions(p, jsonObject);
        return p;
    }


    // MODIFIES: p
    // EFFECTS: parses holdings from JSON object and adds them to Portfolio
    private void addSecurities(Portfolio p, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("holdings");
        for (Object json : jsonArray) {
            JSONObject nextSecurity = (JSONObject) json;
            parseSecurity(p, nextSecurity);
        }
    }

    // MODIFIES: p
    // EFFECTS: parses security from JSON object and adds it to Portfolio
    private void parseSecurity(Portfolio p, JSONObject jsonObject) {
        String ticker = jsonObject.getString("ticker");
        p.addNewSecurity(ticker);
    }

    // MODIFIES: P
    // EFFECTS: parses all of the stored transactions from JSON object and adds them to Portfolio
    private void parseTransactions(Portfolio p, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("transactions");
        for (Object json : jsonArray) {
            JSONObject nextTransaction = (JSONObject) json;
            parseTransaction(p, nextTransaction);
        }
    }

    private void parseTransaction(Portfolio p, JSONObject jsonObject) {
        String ticker = jsonObject.getString("ticker");
        Calendar date = Calendar.getInstance();
        date.set(jsonObject.getInt("year"), jsonObject.getInt("month"), jsonObject.getInt("day"));
        boolean isSell = jsonObject.getBoolean("isSell");
        double value = jsonObject.getDouble("value");
        boolean isUSD = jsonObject.getBoolean("isUSD");
        double fxRate = jsonObject.getDouble("fxRate");
        int shares = jsonObject.getInt("shares");
        double commission = jsonObject.getDouble("commission");

        Transaction add = new Transaction(ticker, date, isSell, value,
                isUSD, fxRate, shares, commission);
        p.addTransaction(add);
    }

    // EFFECTS: reads account names from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ArrayList<String> readList() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSavedNames(jsonObject);
    }

    // EFFECTS: reads saved account names from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ArrayList<String> parseSavedNames(JSONObject jsonObject) {
        ArrayList<String> names = new ArrayList<>();
        JSONArray savedNames = jsonObject.getJSONArray("names");

        for (Object json : savedNames) {
            String nextName = (String) json;
            names.add(nextName);
        }

        return names;
    }
}


