package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioTest {
    Portfolio testPort;
    Security bns;
    Security brk;
    Calendar date1 = Calendar.getInstance();
    Calendar date2 = Calendar.getInstance();
    Calendar date3 = Calendar.getInstance();
    Transaction buyBNS;
    Transaction buyBRKusd;

    @BeforeEach
    void setup(){
        bns = new Security("BNS", "Bank of Nova Scotia");
        brk = new Security("BRK", "Berkshire Hathaway");
        testPort = new Portfolio( "Simon" );
    }

    void setTransactions(){
        date1.set(2020, Calendar.NOVEMBER, 20);
        date2.set(2019, Calendar.JUNE, 5);
        date3.set(2021, Calendar.MARCH, 20);
        buyBNS = new Transaction(bns, date1, false, 1089.18,
                false, 0, 10, 4.99);
        buyBRKusd = new Transaction(brk, date3, false, 6543.21,
                true, 1.3356, 90, 5.99);
    }

    @Test
    void testPortfolio(){
        assertEquals("Simon", testPort.getName());
        assertEquals(0, testPort.getHoldings().size());
    }

    @Test
    void addNewSecurity() {
        testPort.addNewSecurity(bns);
        assertEquals(1, testPort.getHoldings().size());
        assertTrue(testPort.getHoldings().contains(bns));

        testPort.addNewSecurity(brk);
        assertEquals(2, testPort.getHoldings().size());
        assertTrue(testPort.getHoldings().contains(brk) && testPort.getHoldings().contains(bns));
    }

    @Test
    void removeSecurity() {
        testPort.addNewSecurity(brk);
        testPort.addNewSecurity(bns);
        testPort.removeSecurity(bns);
        assertEquals(1, testPort.getHoldings().size());
        assertTrue(testPort.getHoldings().contains(brk));
    }

    @Test
    void addTransaction() {
        testPort.addNewSecurity(bns);
        testPort.addNewSecurity(brk);
        testPort.addTransaction(buyBNS);
        assertEquals(1, bns.getNumTransactions());
        assertEquals(0, brk.getNumTransactions());
        testPort.addTransaction(buyBRKusd);
        assertEquals(1, bns.getNumTransactions());
        assertEquals(1, brk.getNumTransactions());
    }
}