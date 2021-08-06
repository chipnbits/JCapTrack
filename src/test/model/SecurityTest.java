package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SecurityTest {
    Calendar date1 = Calendar.getInstance();
    Calendar date2 = Calendar.getInstance();
    Calendar date3 = Calendar.getInstance();
    Transaction buyBNS1;
    Transaction buyBNS2;
    Transaction sellBNS;
    Security bns;

    @BeforeEach
    public void setup() {
        bns = new Security("BNS");
        date1.set(2019, Calendar.NOVEMBER, 20);
        date2.set(2020, Calendar.JUNE, 5);
        date3.set(2021, Calendar.MARCH, 20);
    }

    void makeTransactionsBNS() {
        buyBNS1 = new Transaction("BNS", date1, false, 1089.18,
                false, 0, 10, 4.99);
        sellBNS = new Transaction("BNS", date2, true, 420.20,
                false, 0, 5, 4.99);
        buyBNS2 = new Transaction("BNS", date3, false, 1850.10,
                false, 0, 20, 4.99);
    }

    @Test
    void testSecurityConstruct() {
        Security brk = new Security("BRK");
        assertEquals("BNS", (bns.getTicker()));
        assertEquals("BRK", (brk.getTicker()));
        assertEquals(0, bns.getNumTransactions());
        assertEquals(0, brk.getNumTransactions());
    }

    @Test
        // Correct values calculated using https://www.adjustedcostbase.ca/index.cgi
    void testAddTransactionToEmptySecurity() {
        makeTransactionsBNS();
        bns.addTransaction(buyBNS1);
        //Transaction Updates
        assertEquals(0, buyBNS1.getGains(), .005);
        assertEquals(10, buyBNS1.getNewTotalShares());
        assertEquals(1094.17, buyBNS1.getNewTotalACB(), .005);
        // Security Updates
        assertEquals(1, bns.getNumTransactions());
        assertEquals(10, bns.getShares());
        assertEquals(1094.17, bns.getAcb());
    }

    @Test
        // Test transactions added in chronological order
    void testAddTransactionInOrder() {
        makeTransactionsBNS();
        bns.addTransaction(buyBNS1);
        bns.addTransaction(buyBNS2);
        //Transaction Updates
        assertEquals(0, buyBNS2.getGains(), .005);
        assertEquals(30, buyBNS2.getNewTotalShares());
        assertEquals(2949.26, buyBNS2.getNewTotalACB(), .005);
        // Security Updates
        assertEquals(2, bns.getNumTransactions());
        assertEquals(30, bns.getShares());
        assertEquals(2949.26, bns.getAcb(), .005);
        // Previous transaction remains the same
        assertEquals(0, buyBNS1.getGains(), .005);
        assertEquals(10, buyBNS1.getNewTotalShares());
        assertEquals(1094.17, buyBNS1.getNewTotalACB(), .005);
    }

    @Test
        // Test transactions added in reversed order
    void testAddTransactionOutOfOrder() {
        makeTransactionsBNS();
        bns.addTransaction(buyBNS2);
        bns.addTransaction(buyBNS1);
        //Later dated transaction updates correctly
        assertEquals(0, buyBNS2.getGains(), .005);
        assertEquals(30, buyBNS2.getNewTotalShares());
        assertEquals(2949.26, buyBNS2.getNewTotalACB(), .005);
        // Previous transaction remains the same
        assertEquals(0, buyBNS1.getGains(), .005);
        assertEquals(10, buyBNS1.getNewTotalShares());
        assertEquals(1094.17, buyBNS1.getNewTotalACB(), .005);
        // Security Updates
        assertEquals(2, bns.getNumTransactions());
        assertEquals(30, bns.getShares());
        assertEquals(2949.26, bns.getAcb(), .005);
    }

    @Test
        // Test that inserting a past transaction correctly updates future transactions without altering past ones
    void testAddTransactionInMiddle() {
        makeTransactionsBNS();
        bns.addTransaction(buyBNS1);    // Early date
        bns.addTransaction(buyBNS2);    // Late date
        bns.addTransaction(sellBNS);    // Middle date
        // Previous transaction remains the same
        assertEquals(0, buyBNS1.getGains(), .005);
        assertEquals(10, buyBNS1.getNewTotalShares());
        assertEquals(1094.17, buyBNS1.getNewTotalACB(), .005);
        //Later dated transaction updates correctly
        assertEquals(0, buyBNS2.getGains(), .005);
        assertEquals(25, buyBNS2.getNewTotalShares());
        assertEquals(2402.18, buyBNS2.getNewTotalACB(), .0051);
        // New transaction has correct information
        assertEquals(-131.88, sellBNS.getGains(), .005);
        assertEquals(5, sellBNS.getNewTotalShares());
        assertEquals(547.09, sellBNS.getNewTotalACB(), .005);
        // Security Updates
        assertEquals(3, bns.getNumTransactions());
        assertEquals(25, bns.getShares());
        assertEquals(2402.18, bns.getAcb(), .0051);
    }

    @Test
    void testToString() {
        // Buy and partially dispose of BNS
        buyBNS1 = new Transaction("BNS", date1, false, 100,
                false, 0, 10, 0);
        sellBNS = new Transaction("BNS", date2, true, 50,
                false, 0, 5, 0);
        bns.addTransaction(buyBNS1);
        bns.addTransaction(sellBNS);
        String str = bns.toString();
        assertTrue(str.contains("BNS"));
        assertTrue(str.contains("5"));
        assertTrue(str.contains("$50.00"));
        assertTrue(str.contains("2"));
        System.out.println(bns);
    }

    @Test
    void testGetTransactionRecordEmpty(){
        List<String> emptyRecord = bns.getTransactionRecord();
        assertEquals(0, emptyRecord.size());
    }

    @Test
    void testGetTransactionRecordMultiple(){
        List<String> record;

        makeTransactionsBNS();
        bns.addTransaction(buyBNS1);
        bns.addTransaction(sellBNS);
        bns.addTransaction(buyBNS2);

        record = bns.getTransactionRecord();
        assertEquals(3, record.size());
        // check share quantities recorded
        assertTrue(record.get(0).contains("10"));
        assertTrue(record.get(1).contains("5"));
        assertTrue(record.get(2).contains("20"));
    }

    @Test
    void testGetTransactionList(){
        makeTransactionsBNS();
        bns.addTransaction(buyBNS1);
        bns.addTransaction(sellBNS);
        bns.addTransaction(buyBNS2);

        List<Transaction> testList = bns.getTransactionList();

        try {
            testList.add(buyBNS1);
            fail();
        } catch (UnsupportedOperationException e) {
            //pass
        }

        try {
            testList.remove(buyBNS1);
            fail();
        } catch (UnsupportedOperationException e) {
            //pass
        }
    }

}
