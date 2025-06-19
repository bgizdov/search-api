package org.acme.search.dto.football;

import java.util.List;

/**
 * Wrapper DTO for SimpleMatch with additional search metadata
 */
public record MatchWrapper(
    String id,
    String searchTitle,
    String searchDescription,
    List<String> tags,
    List<String> flags,
    List<String> entityIds,
    Match data
) {

    /**
     * Create a wrapper with search metadata for a SimpleMatch
     */
    public static MatchWrapper of(Match match, String searchTitle, String searchDescription,
                                       List<String> tags, List<String> flags, List<String> entityIds) {
        return new MatchWrapper(
            match.id(),
            searchTitle,
            searchDescription,
            tags,
            flags,
            entityIds,
            match
        );
    }

    /**
     * Create a simple wrapper with minimal metadata
     */
    public static MatchWrapper of(Match match) {
        String title = String.format("%s vs %s",
            match.homeTeam() != null ? match.homeTeam().name() : "Unknown",
            match.awayTeam() != null ? match.awayTeam().name() : "Unknown");

        String description = String.format("Football match at %s",
            match.venue() != null ? match.venue() : "Unknown venue");

        // Build entity IDs - use IDs as they are since they already have the correct format
        List<String> entityIds = List.of(
            match.id(), // Match ID
            match.homeTeam() != null ? match.homeTeam().id() : null,
            match.awayTeam() != null ? match.awayTeam().id() : null,
            match.competition() != null ? match.competition().id() : null
        ).stream().filter(id -> id != null).toList();

        return new MatchWrapper(
            match.id(),
            title,
            description,
            List.of("football", "match"),
            List.of(),
            entityIds,
            match
        );
    }
}
