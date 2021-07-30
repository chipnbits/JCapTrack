package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
This Test suite has borrowed some code structure and concepts from JsonSerializationDemo UBC CPSC 210
 */

class JsonReaderTest {
    Portfolio testPort;

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            testPort = reader.readPortfolio();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyPortfolio() {
        try {
            JsonReader reader = new JsonReader("./data/testWriterEmptyPortfolio.json");
            testPort = reader.readPortfolio();
            assertEquals("Test", testPort.getName());
            assertEquals(0, testPort.getNumHoldings());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralPortfolio() {
        try {
            JsonReader reader = new JsonReader("./data/testWriterGeneralPortfolio.json");
            testPort = reader.readPortfolio();
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }

        assertEquals("Simon", testPort.getName());
        assertEquals(2, testPort.getNumHoldings());

        List<String> tickers = testPort.getTickers();
        assertTrue(tickers.contains("BNS"));
        assertTrue(tickers.contains("BRK"));

        List<String> bnsTransactions = testPort.searchTransactions("BNS");
        List<String> brkTransactions = testPort.searchTransactions("BRK");
        assertEquals(2, bnsTransactions.size());
        assertEquals(1, brkTransactions.size());

        assertTrue(bnsTransactions.get(0).contains(
                "Date: 2019-November-20\n" +
                        "Buy 10 shares of BNS\n" +
                        "Value: $1,089.18 CAD\n" +
                        "Commission: $4.99 CAD\n" +
                        "Gains: $0.00\n" +
                        "TotalShares: 10\n" +
                        "ACB: $1,094.17 CAD"));

        assertTrue(bnsTransactions.get(1).contains(
                "Date: 2020-June-05\n" +
                        "Sell 5 shares of BNS\n" +
                        "Value: $500.00 CAD\n" +
                        "Commission: $4.99 CAD\n" +
                        "Gains: -$52.08\n" +
                        "TotalShares: 5\n" +
                        "ACB: $547.09 CAD"));

        assertTrue(brkTransactions.get(0).contains(
                "Date: 2021-March-20\n" +
                        "Buy 90 shares of BRK\n" +
                        "Value: $6,543.21 USD\n" +
                        "Commission: $5.99 USD\n" +
                        "Gains: $0.00\n" +
                        "TotalShares: 90\n" +
                        "ACB: $8,747.11 CAD"));
    }

    @Test
    void testReaderEmptyNamesList() {
        List<String> names;

        try {
            JsonReader reader = new JsonReader("./data/testWriterEmptyNames.json");
            names = reader.readList();
            assertTrue(names.isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testReaderGeneralNames() {
        List<String> names = new ArrayList<>();

        try {
            JsonReader reader = new JsonReader("./data/testWriterGeneralNames.json");
            names = reader.readList();
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }

        assertEquals(4, names.size());
        assertEquals("a", names.get(0));
        assertEquals("b", names.get(1));
        assertEquals("c", names.get(2));
        assertEquals("d", names.get(3));
    }
}