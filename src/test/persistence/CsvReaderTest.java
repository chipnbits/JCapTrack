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
        testReader = new CsvReader("./data/csv/SimonTest.csv");
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
        CsvReader reader = new CsvReader("./data/csv/SimonCorruptSecurity.csv");
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
        CsvReader reader = new CsvReader("./data/csv/SimonCorruptTransactions.csv");
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


}