import org.example.ParsingFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParsingFileTest {
    private Set<Integer> integers;
    private Set<Double> floats;
    private Set<String> strings;

    @BeforeEach
    void setUp() {
        integers = new HashSet<>();
        floats = new HashSet<>();
        strings = new HashSet<>();
    }

    @Test
    void testParseInteger() {
        ParsingFile.parse("123", integers, floats, strings);

        assertEquals(1, integers.size());
        assertEquals(0, floats.size());
        assertEquals(0, strings.size());
        assertTrue(integers.contains(123));
    }

    @Test
    void testParseNegativeInteger() {
        ParsingFile.parse("-456", integers, floats, strings);

        assertEquals(1, integers.size());
        assertEquals(0, floats.size());
        assertEquals(0, strings.size());
        assertTrue(integers.contains(-456));
    }

    @Test
    void testParseFloat() {
        ParsingFile.parse("123.45", integers, floats, strings);

        assertEquals(0, integers.size());
        assertEquals(1, floats.size());
        assertEquals(0, strings.size());
        assertTrue(floats.contains(123.45));
    }

    @Test
    void testParseNegativeFloat() {
        ParsingFile.parse("-123.45", integers, floats, strings);

        assertEquals(0, integers.size());
        assertEquals(1, floats.size());
        assertEquals(0, strings.size());
        assertTrue(floats.contains(-123.45));
    }

    @Test
    void testParseString() {
        ParsingFile.parse("hello", integers, floats, strings);

        assertEquals(0, integers.size());
        assertEquals(0, floats.size());
        assertEquals(1, strings.size());
        assertTrue(strings.contains("hello"));
    }

    @Test
    void testParseMultipleLines() {
        ParsingFile.parse("123", integers, floats, strings);
        ParsingFile.parse("456.78", integers, floats, strings);
        ParsingFile.parse("hello", integers, floats, strings);

        assertEquals(1, integers.size());
        assertEquals(1, floats.size());
        assertEquals(1, strings.size());
        assertTrue(integers.contains(123));
        assertTrue(floats.contains(456.78));
        assertTrue(strings.contains("hello"));
    }
}
