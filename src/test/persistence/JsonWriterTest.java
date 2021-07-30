package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
This Test suite has borrowed some code structure and concepts from JsonSerializationDemo UBC CPSC 210
 */

class JsonWriterTest {
    Portfolio testPort;
    Calendar date1;
    Calendar date2;
    Calendar date3;
    Transaction buyBNS;
    Transaction sellBNS;
    Transaction buyBRKusd;

    @BeforeEach
    void setup() {
        testPort = new Portfolio("Simon");
        date1 = Calendar.getInstance();
        date2 = Calendar.getInstance();
        date3 = Calendar.getInstance();
        date1.set(2019, Calendar.NOVEMBER, 20);
        date2.set(2020, Calendar.JUNE, 5);
        date3.set(2021, Calendar.MARCH, 20);
    }

    void setTransactions() {
        buyBNS = new Transaction("BNS", date1, false, 1089.18,
                false, 0, 10, 4.99);
        buyBRKusd = new Transaction("BRK", date3, false, 6543.21,
                true, 1.3356, 90, 5.99);
        sellBNS = new Transaction("BNS", date2, true, 500,
                false, 0, 5, 4.99);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            Portfolio p = new Portfolio("Test");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyPortfolio() {
        try {
            Portfolio p = new Portfolio("Test");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyPortfolio.json");
            writer.open();
            writer.write(p);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyPortfolio.json");
            p = reader.readPortfolio();
            assertEquals("Test", p.getName());
            assertEquals(0, p.getNumHoldings());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralPortfolio() {
        setupPortfolio();

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
    void testWriterEmptyNamesList() {
        try {
            List<String> names = new ArrayList<>();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyNames.json");
            writer.open();
            writer.write(names);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyNames.json");
            names = reader.readList();
            assertTrue(names.isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralNames() {
        List<String> names = setupNamesList();

        assertEquals(4, names.size());
        assertEquals("a", names.get(0));
        assertEquals("b", names.get(1));
        assertEquals("c", names.get(2));
        assertEquals("d", names.get(3));

        try {
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralNames.json");
            writer.open();
            writer.write(names);
            writer.close();

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



    private void setupPortfolio() {
        try {
            setTransactions();
            testPort.addNewSecurity("BNS");
            testPort.addNewSecurity("BRK");
            testPort.addTransaction(buyBNS);
            testPort.addTransaction(buyBRKusd);
            testPort.addTransaction(sellBNS);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralPortfolio.json");
            writer.open();
            writer.write(testPort);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralPortfolio.json");
            testPort = reader.readPortfolio();
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    private List<String> setupNamesList(){
        List<String> names = new ArrayList<>();

        names.add("a");
        names.add("b");
        names.add("c");
        names.add("d");

        return names;
    }
}