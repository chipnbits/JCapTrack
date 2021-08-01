package persistence;

import exceptions.FileCorruptException;
import model.Portfolio;
import model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImportDataTest {
    Portfolio p;
    ImportData data;
    TransactionBuilder tb = new TransactionBuilder();

    @BeforeEach
    void setup(){
        p = new Portfolio("Test");
        data = new ImportData(getSecurities(), tb.makeListOfTransactions());
    }

    @Test
    void testImportEmpty(){
        data = new ImportData(new ArrayList<>() , new ArrayList<>());
        assertTrue(data.getSecurityNames().isEmpty());
        assertTrue(data.getTransactions().isEmpty());
        data.addToPortfolio(p);
        assertEquals(0, p.getNumHoldings());
    }

    @Test
    void testImportDataNormal(){
        data.addToPortfolio(p);
        assertEquals(2, p.getNumHoldings());
        assertEquals(2, p.searchTransactions("BNS").size());
        assertEquals(2, p.searchTransactions("BRK").size());
        assertEquals("Test", p.getName());
    }

    @Test
    void testImportDataMissingSecurities(){
        List<String> sec= new ArrayList<>();
        sec.add("BNS");
        data = new ImportData(sec , tb.makeListOfTransactions());
        data.addToPortfolio(p);
        assertEquals(2, p.getNumHoldings());
        assertEquals(2, p.searchTransactions("BNS").size());
        assertEquals(2, p.searchTransactions("BRK").size());
        assertEquals("Test", p.getName());
    }

    @Test
    void testImportDataOverlappingSecurities(){
        data.addToPortfolio(p);
        data.addToPortfolio(p);
        assertEquals(2, p.getNumHoldings());
        assertEquals(4, p.searchTransactions("BNS").size());
        assertEquals(4, p.searchTransactions("BRK").size());
        assertEquals("Test", p.getName());
    }

    @Test
    void testImportFromReader(){
        CsvReader testReader = new CsvReader("./data/csv/test/ImportTestData.csv");
        Portfolio p = new Portfolio("Test");
        try {
            testReader.parseData().addToPortfolio(p);
        } catch (FileNotFoundException | FileCorruptException e) {
            fail("Exceptions not expected");
        }
        assertEquals(3, p.searchTransactions("GOOG").size());
        assertEquals(3, p.searchTransactions("REI").size());
        assertEquals(7, p.searchTransactions("ZLB").size());
        assertEquals(3, p.getTickers().size());
        assertEquals(6, p.getTaxTransactions(2020).size());

        System.out.println(p.getSummary());

    }

    private List<String> getSecurities() {
        List<String> sec= new ArrayList<>();
        sec.add("BNS");
        sec.add("BRK");

        return sec;
    }

    class TransactionBuilder {
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        Calendar date3 = Calendar.getInstance();
        Transaction sellBNS;
        Transaction buyBNS;
        Transaction sellBRKusd;
        Transaction buyBRKusd;
        String bns = "BNS";
        String brk = "BRK";

        protected TransactionBuilder() {
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

        protected List<Transaction> makeListOfTransactions(){
            List<Transaction> list = new ArrayList<>();
            list.add(buyBNS);
            list.add(sellBNS);
            list.add(buyBRKusd);
            list.add(sellBRKusd);

            return list;
        }
    }



}