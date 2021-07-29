package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.
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
            p = reader.read();
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
            testPort = reader.read();
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}