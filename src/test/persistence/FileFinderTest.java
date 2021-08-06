package persistence;

import exceptions.DirectoryNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class FileFinderTest {
    String validDirectory = "./data/portfolios";
    String invalidDirectory = "./data/goingToBeBad";

    @Test
    void testGetNamesFromSystem() {
        try {
            FileFinder.getNamesFromSystem(validDirectory, ".json");
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
    void testDirectoryNotFoundException(){
        DirectoryNotFoundException e = new DirectoryNotFoundException("test");
        assertEquals("test", e.getMessage());
    }
}
