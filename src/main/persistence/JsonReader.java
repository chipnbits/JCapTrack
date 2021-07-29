package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads Portfolio from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads portfolio from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Portfolio read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePortfolio(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses Portfolio from JSON object and returns it
    private Portfolio parsePortfolio(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Portfolio p = new Portfolio(name);

        addSecurities(p, jsonObject);
        addTransactions(p, jsonObject);
        return p;
    }


    // MODIFIES: p
    // EFFECTS: parses holdings from JSON object and adds them to Portfolio
    private void addSecurities(Portfolio p, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("holdings");
        for (Object json : jsonArray) {
            JSONObject nextSecurity = (JSONObject) json;
            addSecurity(p, nextSecurity);
        }
    }

    // MODIFIES: p
    // EFFECTS: parses security from JSON object and adds it to Portfolio
    private void addSecurity(Portfolio p, JSONObject jsonObject) {
        String ticker = jsonObject.getString("ticker");
        //TODO sort out name being null
        //String name = jsonObject.getString("name");

        p.addNewSecurity(ticker);
       // p.setName(name);
    }

    // MODIFIES: P
    // EFFECTS: parses all of the stored transactions from JSON object and adds them to Portfolio
    private void addTransactions(Portfolio p, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("transactions");
        for (Object json : jsonArray) {
            JSONObject nextTransaction = (JSONObject) json;
            addTransaction(p, nextTransaction);
        }
    }

    private void addTransaction(Portfolio p, JSONObject jsonObject) {
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

}


