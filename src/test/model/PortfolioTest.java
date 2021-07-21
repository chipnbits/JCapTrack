package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

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
        assertTrue(testPort.addTransaction(buyBNS));
    }

    @Test
    void testAddTransactionNonExisting() {
        setTransactions();
        testPort.addNewSecurity("BNS");
        assertFalse(testPort.addTransaction(buyBRKusd));
    }

    @Test
    void testGetTickers(){
        testPort.addNewSecurity("BRK");
        testPort.addNewSecurity("BNS");
        assertTrue(testPort.getTickers().contains("BNS"));
        assertTrue(testPort.getTickers().contains("BRK"));
        assertEquals(2,testPort.getTickers().size());
    }

    @Test
    void hasTicker(){
        testPort.addNewSecurity("BRK");
        testPort.addNewSecurity("BNS");
        assertTrue(testPort.hasTicker("BNS"));
        assertTrue(testPort.hasTicker("BRK"));
        assertFalse(testPort.hasTicker("AAA"));
    }
}