package persistence;

import exceptions.FileCorruptException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class CsvReaderTest {

    CsvReader testReader;

    @BeforeEach
    void setup() {
        testReader = new CsvReader("./data/csv/test/SimonTest.csv");
    }

    @Test
    void testReaderNonExistentFile() {
        CsvReader reader = new CsvReader("./data/noSuchFile.json");
        try {
            CsvExportData ced = reader.parseData();
            fail("IOException expected");
        } catch (FileCorruptException e) {
            fail("File does not exist");
        } catch (FileNotFoundException e) {
            // Pass
        }

    }

    @Test
    void testReaderCorruptSecurity() {
        CsvReader reader = new CsvReader("./data/csv/test/SimonCorruptSecurity.csv");
        try {
            CsvExportData ced = reader.parseData();
            fail("File is corrupt");
        } catch (FileNotFoundException e) {
            fail("File exists");
        } catch (FileCorruptException e) {
            //Pass
        }
    }

    @Test
    void testReaderCorruptTransactionDate() {
        CsvReader reader = new CsvReader("./data/csv/test/SimonCorruptTransactions.csv");
        try {
            CsvExportData ced = reader.parseData();
            fail("File is corrupt");
        } catch (FileNotFoundException e) {
            fail("File exists");
        } catch (FileCorruptException e) {
            //Pass
        }

    }

    @Test
    void testReaderReadFile() {
        try {
            CsvExportData ced = testReader.parseData();
            assertEquals(16, ced.getSecurityNames().size());
            assertTrue(ced.getSecurityNames().contains("CHP.UN"));
            assertTrue(ced.getSecurityNames().contains("HBGD"));
            assertTrue(ced.getSecurityNames().contains("TFII"));
            assertTrue(ced.getSecurityNames().contains("GOOG"));

            assertEquals(37, ced.getTransactions().size());
            assertFalse(ced.getTransactions().get(0).getBuyOrSell());
            assertTrue(ced.getTransactions().get(4).getBuyOrSell());
            assertEquals(.77, ced.getTransactions().get(0).getFxRate());
            assertEquals(2582.44, ced.getTransactions().get(0).getValue());
            assertTrue(ced.getTransactions().get(0).toString().contains("USD"));


        } catch (FileCorruptException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            fail("Exception not expected");
        }


    }

    @Test
    void testReaderNoNewLineTransaction() {
        CsvReader reader = new CsvReader("./data/csv/test/NoNewLineTransaction.csv");
        try {
            CsvExportData ced = reader.parseData();
            assertTrue(ced.getSecurityNames().contains("CHP.UN"));
            assertEquals(1, ced.getSecurityNames().size());
            assertTrue(ced.getTransactions().get(0).toString().equals(
                    "Date: 2019-December-26\n" +
                    "Buy 2 shares of GOOG\n" +
                    "Value: $2,582.44 USD\n" +
                    "Commission: $12.97 USD\n" +
                    "Gains: $0.00\n" +
                    "TotalShares: 0\n" +
                    "ACB: $0.00 CAD"));
            assertEquals(1, ced.getTransactions().size());
        } catch (FileNotFoundException e) {
            fail("File exists");
        } catch (FileCorruptException e) {
            fail("File is corrupt");
        }
    }

    @Test
    void testReaderNoNewLineSecurity() {
        CsvReader reader = new CsvReader("./data/csv/test/NoNewLineSecurity.csv");
        try {
            CsvExportData ced = reader.parseData();
            assertTrue(ced.getSecurityNames().contains("CHP.UN"));
            assertEquals(1, ced.getSecurityNames().size());
            assertEquals(0, ced.getTransactions().size());
        } catch (FileNotFoundException e) {
            fail("File exists");
        } catch (FileCorruptException e) {
            fail("File is corrupt");
        }
    }

    @Test
    void testReaderNoTransactionStart() {
        CsvReader reader = new CsvReader("./data/csv/test/NoNewLineNoTransactions.csv");
        try {
            CsvExportData ced = reader.parseData();
            assertTrue(ced.getSecurityNames().contains("CHP.UN"));
            assertEquals(1, ced.getSecurityNames().size());
            assertEquals(0, ced.getTransactions().size());
        } catch (FileNotFoundException e) {
            fail("File exists");
        } catch (FileCorruptException e) {
            fail("File is corrupt");
        }
    }

    @Test
    void testReaderNoSecurityStart() {
        CsvReader reader = new CsvReader("./data/csv/test/NoNewLineNoSecurities.csv");
        try {
            CsvExportData ced = reader.parseData();
            assertEquals(0, ced.getSecurityNames().size());
            assertEquals(0, ced.getTransactions().size());
        } catch (FileNotFoundException e) {
            fail("File exists");
        } catch (FileCorruptException e) {
            fail("File is corrupt");
        }
    }
}