package persistence;

import model.*;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JsonTest {
    protected void checkTransaction(Transaction transaction, String ticker, Calendar date, boolean type, double val,
                                    boolean fx, double rate, int shares, double commission) {
        assertEquals(ticker, transaction.getSecurity());
        assertEquals(date, transaction.getDate());
        assertEquals(type, transaction.getBuyOrSell());
        assertEquals(val, transaction.getValue());
        assertEquals(fx, transaction.getCurrency());
        assertEquals(rate, transaction.getFxRate());
        assertEquals(shares, transaction.getShares());
        assertEquals(commission, transaction.getCommission());

    }

}
