package persistence;

import exceptions.DirectoryNotFoundException;
import model.Portfolio;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileFinderTest {
    String validDirectory = "./data/portfolios/test";
    String invalidDirectory = "./data/goingToBeBad";
    String testWriteDelete = "./data/portfolios/testReadWrite.txt";

    @Test
    void testInstantiate(){

    }

    @Test
    void testGetNamesFromSystem() {
        try {
            List<String> names = FileFinder.getNamesFromSystem(validDirectory, ".json");
            assertEquals(5,names.size());
        } catch (DirectoryNotFoundException e) {
            fail("The directory should be valid");
        }


        try {
            FileFinder.getNamesFromSystem(invalidDirectory, ".json");
            fail("The directory should be invalid");
        } catch (DirectoryNotFoundException e) {
            //pass
        }
    }

    @Test
    void testDirectoryNotFoundException() {
        DirectoryNotFoundException e = new DirectoryNotFoundException("test");
        assertEquals("test", e.getMessage());
    }

    @Test
    void testDeleteNonExistFile() {
        assertFalse(FileFinder.deleteFile(invalidDirectory));
    }

    @Test
    void testDeleteExistingFile() {

        try {
            Files.write(Paths.get(testWriteDelete), Collections.singleton("test"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail("Unable to create a test file to delete");
        }
        assertTrue(FileFinder.deleteFile(testWriteDelete));
    }

    @Test
    void testWritePortfolio(){
        Portfolio p = new Portfolio("testAddDelete");

        FileFinder.writePortfolioSaveFile(p, "./data/portfolios/test/****");
        FileFinder.writePortfolioSaveFile(p,testWriteDelete);
        assertTrue(FileFinder.deleteFile(testWriteDelete));
    }


}
