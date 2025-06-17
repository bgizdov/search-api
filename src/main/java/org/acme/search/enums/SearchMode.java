package org.acme.search.enums;

/**
 * Enum representing different search modes for text matching
 */
public enum SearchMode {
    /**
     * Case insensitive partial matching (default)
     * Example: "game" matches "Game 21", "Player Game", "GAME"
     */
    CASE_INSENSITIVE,
    
    /**
     * Case sensitive partial matching
     * Example: "Game" matches "Game 21", "Player Game" but not "game" or "GAME"
     */
    CASE_SENSITIVE,
    
    /**
     * Full string match (case insensitive)
     * Example: "Player of the Match Game 21" only matches exact title, not "Game 21" or "Game 22"
     */
    FULL_MATCH;
    
    /**
     * Default search mode
     */
    public static final SearchMode DEFAULT = CASE_INSENSITIVE;
    
    /**
     * Parse search mode from string, case insensitive
     * @param mode the mode string
     * @return the SearchMode enum value
     * @throws IllegalArgumentException if mode is not recognized
     */
    public static SearchMode fromString(String mode) {
        if (mode == null || mode.trim().isEmpty()) {
            return DEFAULT;
        }
        
        return switch (mode.toUpperCase().trim()) {
            case "CASE_INSENSITIVE", "CASE-INSENSITIVE", "INSENSITIVE" -> CASE_INSENSITIVE;
            case "CASE_SENSITIVE", "CASE-SENSITIVE", "SENSITIVE" -> CASE_SENSITIVE;
            case "FULL_MATCH", "FULL-MATCH", "FULL", "EXACT" -> FULL_MATCH;
            default -> throw new IllegalArgumentException("Unknown search mode: " + mode + 
                ". Supported modes: CASE_INSENSITIVE, CASE_SENSITIVE, FULL_MATCH");
        };
    }
    
    /**
     * Get a human-readable description of the search mode
     * @return description string
     */
    public String getDescription() {
        return switch (this) {
            case CASE_INSENSITIVE -> "Case insensitive partial matching";
            case CASE_SENSITIVE -> "Case sensitive partial matching";
            case FULL_MATCH -> "Full string match (case insensitive)";
        };
    }
}
