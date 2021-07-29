package model;

import exceptions.NoTickerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioTest {
    Portfolio testPort;
    Calendar date1;
    Calendar date2;
    Calendar date3;
    Transaction buyBNS;
    Transaction buyBRKusd;

    @BeforeEach
    void setup() {
        testPort = new Portfolio("Simon");
        date1 = Calendar.getInstance();
        date2 = Calendar.getInstance();
        date3 = Calendar.getInstance();
        date1.set(2020, Calendar.NOVEMBER, 20);
        date2.set(2019, Calendar.JUNE, 5);
        date3.set(2021, Calendar.MARCH, 20);
    }

    void setTransactions() {
        buyBNS = new Transaction("BNS", date1, false, 1089.18,
                false, 0, 10, 4.99);
        buyBRKusd = new Transaction("BRK", date3, false, 6543.21,
                true, 1.3356, 90, 5.99);
    }

    @Test
    void testPortfolioConstruct() {
        assertEquals("Simon", testPort.getName());
        assertEquals(0, testPort.getNumHoldings());
        testPort.setName("Josh");
        assertEquals("Josh", testPort.getName());
    }

    @Test
    void testAddNewSecurity() {
        assertTrue(testPort.addNewSecurity("BNS"));
        assertEquals(1, testPort.getNumHoldings());
        assertTrue(testPort.hasTicker("BNS"));
        assertFalse(testPort.hasTicker("BRK"));

        assertTrue(testPort.addNewSecurity("BRK"));
        assertEquals(2, testPort.getNumHoldings());
        assertTrue(testPort.hasTicker("BNS") && testPort.hasTicker("BRK"));

        // Check that duplicates are caught
        assertFalse(testPort.addNewSecurity("BRK"));
    }

    @Test
    void testRemoveSecurityExisting() {
        testPort.addNewSecurity("BRK");
        testPort.addNewSecurity("BNS");
        assertTrue(testPort.removeSecurity("BNS"));
        assertEquals(1, testPort.getNumHoldings());
        assertTrue(testPort.hasTicker("BRK"));
        assertFalse(testPort.hasTicker("BNS"));
    }

    @Test
    void testRemoveSecurityNonExisting() {
        assertEquals(0, testPort.getNumHoldings());
        assertFalse(testPort.removeSecurity("BNS"));
        assertEquals(0, testPort.getNumHoldings());
    }

    @Test
    void testAddTransactionExisting() {
        setTransactions();
        testPort.addNewSecurity("BNS");
        testPort.addTransaction(buyBNS);
        assertEquals(1,testPort.searchTransactions("BNS").size());
    }

    @Test
    void testAddTransactionNonExisting() {
        setTransactions();
        testPort.addNewSecurity("BNS");
        try {
            testPort.addTransaction(buyBRKusd);
            fail();
        } catch (NoTickerException e){
            //Pass
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetTickers() {
        testPort.addNewSecurity("BRK");
        testPort.addNewSecurity("BNS");
        testPort.addNewSecurity("AAA");
        assertTrue(testPort.getTickers().contains("BNS"));
        assertTrue(testPort.getTickers().contains("BRK"));
        assertTrue(testPort.getTickers().contains("AAA"));
        assertEquals(3, testPort.getTickers().size());
        assertEquals("AAA", testPort.getTickers().get(0));
    }

    @Test
    void testHasTicker() {
        testPort.addNewSecurity("BRK");
        testPort.addNewSecurity("BNS");
        assertTrue(testPort.hasTicker("BNS"));
        assertTrue(testPort.hasTicker("BRK"));
        assertFalse(testPort.hasTicker("AAA"));
    }

    @Test
    void testGetSummaryOrdering() {
        String tickerA = "Aa";
        String tickerB = "Ba";
        String tickerC = "C1";
        String tickerD = "Dz";
        testPort.addNewSecurity(tickerC);
        testPort.addNewSecurity(tickerD);
        testPort.addNewSecurity(tickerA);
        testPort.addNewSecurity(tickerB);

        List<String> output = testPort.getSummary();

        //Check that they are not in original order
        assertFalse(output.get(0).contains(tickerC));
        assertFalse(output.get(1).contains(tickerD));
        assertFalse(output.get(2).contains(tickerA));
        assertFalse(output.get(3).contains(tickerB));

        // Check that they are in order
        assertTrue(output.get(0).contains(tickerA));
        assertTrue(output.get(1).contains(tickerB));
        assertTrue(output.get(2).contains(tickerC));
        assertTrue(output.get(3).contains(tickerD));
    }

    @Test
    void testSearchTransactionsValidTickerNoTransactions() {
        String tickerA = "Aa";

        testPort.addNewSecurity(tickerA);
        assertTrue(testPort.hasTicker(tickerA));

        try {
            assertTrue(testPort.searchTransactions(tickerA).isEmpty());
        } catch (NoTickerException e) {
            fail();
        }
    }

    @Test
    void testSearchTransactionsNoValidTickerNoTransactions() {
        String tickerA = "Aa";

        assertEquals(0, testPort.getNumHoldings());

        try {
            testPort.searchTransactions(tickerA);
            fail();
        } catch (NoTickerException e) {
            // Pass condition
        }
    }

    @Test
    void testSearchTransactionsValidTickerWithTransactions() {
        List<String> record;

        setTransactions();
        testPort.addNewSecurity("BNS");
        testPort.addNewSecurity("BRK");
        testPort.addTransaction(buyBNS);
        testPort.addTransaction(buyBRKusd);

        assertTrue(testPort.hasTicker("BNS"));
        assertTrue(testPort.hasTicker("BRK"));

        try {
            record = testPort.searchTransactions("BNS");
            assertEquals(1, record.size());
            assertTrue(record.contains(buyBNS.toString()));

        } catch (NoTickerException e) {
            fail();
        }
    }

    @Test
    void testSearchTransactionsInvalidValidTickerWithTransactions() {

        setTransactions();
        testPort.addNewSecurity("BNS");
        testPort.addNewSecurity("BRK");
        testPort.addTransaction(buyBNS);
        testPort.addTransaction(buyBRKusd);

        assertTrue(testPort.hasTicker("BNS"));
        assertTrue(testPort.hasTicker("BRK"));

        try {
            testPort.searchTransactions("BAS");
            fail();
        } catch (NoTickerException e) {
            //Pass
        }
    }

    @Test
    void testGetTaxTransactions() {
        testPort.addNewSecurity("BNS");
        Transaction tax = new Transaction("BNS", date2, true, 500,
                false, 0, 5, 4.99);
        setTransactions();

        date2.set(2021, Calendar.JUNE, 5);
        testPort.addTransaction(tax);
        assertTrue(testPort.getTaxTransactions(2020).isEmpty());
        assertTrue(testPort.getTaxTransactions(2022).isEmpty());
        assertEquals(1, testPort.getTaxTransactions(2021).size());
        assertTrue(testPort.getTaxTransactions(2021).contains(tax.toString()));

    }


}