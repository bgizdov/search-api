package org.acme.search.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchModeTest {

    @Test
    void testFromString() {
        // Test case insensitive
        assertEquals(SearchMode.CASE_INSENSITIVE, SearchMode.fromString("case_insensitive"));
        assertEquals(SearchMode.CASE_INSENSITIVE, SearchMode.fromString("CASE_INSENSITIVE"));
        assertEquals(SearchMode.CASE_INSENSITIVE, SearchMode.fromString("case-insensitive"));
        assertEquals(SearchMode.CASE_INSENSITIVE, SearchMode.fromString("insensitive"));
        
        // Test case sensitive
        assertEquals(SearchMode.CASE_SENSITIVE, SearchMode.fromString("case_sensitive"));
        assertEquals(SearchMode.CASE_SENSITIVE, SearchMode.fromString("CASE_SENSITIVE"));
        assertEquals(SearchMode.CASE_SENSITIVE, SearchMode.fromString("case-sensitive"));
        assertEquals(SearchMode.CASE_SENSITIVE, SearchMode.fromString("sensitive"));
        
        // Test full match
        assertEquals(SearchMode.FULL_MATCH, SearchMode.fromString("full_match"));
        assertEquals(SearchMode.FULL_MATCH, SearchMode.fromString("FULL_MATCH"));
        assertEquals(SearchMode.FULL_MATCH, SearchMode.fromString("full-match"));
        assertEquals(SearchMode.FULL_MATCH, SearchMode.fromString("full"));
        assertEquals(SearchMode.FULL_MATCH, SearchMode.fromString("exact"));
        
        // Test default for null/empty
        assertEquals(SearchMode.DEFAULT, SearchMode.fromString(null));
        assertEquals(SearchMode.DEFAULT, SearchMode.fromString(""));
        assertEquals(SearchMode.DEFAULT, SearchMode.fromString("   "));
    }

    @Test
    void testFromStringInvalid() {
        assertThrows(IllegalArgumentException.class, () -> SearchMode.fromString("invalid"));
        assertThrows(IllegalArgumentException.class, () -> SearchMode.fromString("unknown"));
    }

    @Test
    void testGetDescription() {
        assertEquals("Case insensitive partial matching", SearchMode.CASE_INSENSITIVE.getDescription());
        assertEquals("Case sensitive partial matching", SearchMode.CASE_SENSITIVE.getDescription());
        assertEquals("Full string match (case insensitive)", SearchMode.FULL_MATCH.getDescription());
    }

    @Test
    void testDefault() {
        assertEquals(SearchMode.CASE_INSENSITIVE, SearchMode.DEFAULT);
    }
}
