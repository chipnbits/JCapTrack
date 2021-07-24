package exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoTickerExceptionTest {
    private NoTickerException nt;

    @Test
    void testGetBadArgument() {
        nt = new NoTickerException("test");
        assertEquals("test", nt.getBadArgument());
    }
}