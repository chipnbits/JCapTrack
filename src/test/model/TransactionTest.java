package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    Calendar date1 = Calendar.getInstance();
    Calendar date2 = Calendar.getInstance();
    Calendar date3 = Calendar.getInstance();
    Transaction sellBNS;
    Transaction buyBNS;
    Transaction sellBRKusd;
    Transaction buyBRKusd;
    String bns = "BNS";
    String brk = "BRK";

    @BeforeEach
    void makeTransactions() {
        date1.set(2020, Calendar.NOVEMBER, 20);
        date2.set(2019, Calendar.JUNE, 5);
        date3.set(2021, Calendar.MARCH, 20);
        buyBNS = new Transaction(bns, date1, false, 1089.18,
                false, 0, 10, 4.99);
        sellBRKusd = new Transaction(brk, date3, true, 420.20,
                true, 1.3356, 5, 4.99);
        sellBNS = new Transaction(bns, date3, true, 420.20,
                false, 0, 5, 4.99);
        buyBRKusd = new Transaction(brk, date3, false, 6543.21,
                true, 1.3356, 90, 5.99);
    }

    @Test
    void testTransactionConstruct() {
        assertEquals(bns, buyBNS.getSecurity());
        assertEquals(date1, buyBNS.getDate());
        assertFalse(buyBNS.getBuyOrSell());
        assertEquals(buyBNS.getValue(), 1089.18);
        assertFalse(buyBNS.getCurrency());
        assertEquals(0, buyBNS.getFxRate());
        assertEquals(10, buyBNS.getShares());
        assertEquals(4.99, buyBNS.getCommission());
    }

    @Test
        // Test values generated using free online acb calculator
    void testUpdateTransactionSellCAD() {
        sellBNS.updateTransaction(10, 1089.18 + 4.99);  //Cost plus commission of buying
        assertEquals(-131.88, sellBNS.getGains(), .005);
        assertEquals(5, sellBNS.getNewTotalShares()); // Bought 10 and sold 5
        assertEquals(547.09, sellBNS.getNewTotalACB(), .005);

        sellBNS.updateTransaction(20, 2018.18 + 8.99);  //Cost plus commission of buying
        assertEquals(-91.58, sellBNS.getGains(), .005);
        assertEquals(15, sellBNS.getNewTotalShares()); // Bought 10 and sold 5
        assertEquals(1520.38, sellBNS.getNewTotalACB(), .005);
    }

    @Test
        // Test values generated using free online acb calculator
    void testUpdateTransactionBuyCAD() {
        // Test the base case where there is no previous shares
        buyBNS.updateTransaction(0, 0);  //Cost plus commission of buying
        assertEquals(0, buyBNS.getGains(), .005); // No gains on a buy trade
        assertEquals( 10, buyBNS.getNewTotalShares()); // 10 shares, buy 10 more
        assertEquals(1094.17, buyBNS.getNewTotalACB(), .005);

        // Test case with previous shares
        buyBNS.updateTransaction(10, 1089.18 + 4.99);  //Cost plus commission of buying
        assertEquals(0, buyBNS.getGains(), .005); // No gains on a buy trade
        assertEquals( 20, buyBNS.getNewTotalShares()); // 10 shares, buy 10 more
        assertEquals(2188.34, buyBNS.getNewTotalACB(), .005);
    }

    @Test
        // Test values generated using free online acb calculator
    void testUpdateTransactionSellUSD() {
        sellBRKusd.updateTransaction(100, 10550.55);
        assertEquals(27.03, sellBRKusd.getGains(), .005); // No gains on a buy trade
        assertEquals( 95, sellBRKusd.getNewTotalShares()); // 10 shares, buy 10 more
        assertEquals(10023.02, sellBRKusd.getNewTotalACB(), .005);
    }

    @Test
         //Test values generated using free online acb calculator
    void testUpdateTransactionBuyUSD(){
        buyBRKusd.updateTransaction(0, 0);
        assertEquals(0, buyBRKusd.getGains(), .005); // No gains on a buy trade
        assertEquals( 90, buyBRKusd.getNewTotalShares()); // 10 shares, buy 10 more
        assertEquals(8747.11, buyBRKusd.getNewTotalACB(), .005);
    }

    @Test
    void testToString() {
        buyBNS.updateTransaction(18, 1089.18);  // Invoke all fields filled in
        String str = buyBNS.toString();
        System.out.println(buyBNS);
        assertTrue(str.contains("Date") && str.contains("2020-November-20"));
        assertTrue(str.contains("Buy"));
        assertTrue(str.contains("10"));
        assertTrue(str.contains("BNS"));
        assertTrue(str.contains("1,089.18"));
        assertTrue(str.contains("CAD"));
        assertTrue(str.contains("0.00"));
        assertTrue(str.contains("28"));
        assertTrue(str.contains("2,183.35"));


        sellBRKusd.updateTransaction(500,10000);
        str = sellBRKusd.toString();
        assertTrue(str.contains("Date"));
        assertTrue(str.contains("2021-March-20"));
        assertTrue(str.contains("Sell"));
        assertTrue(str.contains("5"));
        assertTrue(str.contains("BRK"));
        assertTrue(str.contains("420.20"));
        assertTrue(str.contains("USD"));
        assertTrue(str.contains("454.55"));
        assertTrue(str.contains("495"));
        assertTrue(str.contains("9,900.00"));
    }


}