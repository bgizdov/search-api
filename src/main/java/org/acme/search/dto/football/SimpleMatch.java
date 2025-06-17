package org.acme.search.dto.football;

import java.time.LocalDateTime;

/**
 * Simple DTO representing a football match for search purposes
 */
public record SimpleMatch(
    Long id,
    String homeTeam,
    String awayTeam,
    Integer homeScore,
    Integer awayScore,
    LocalDateTime matchDate,
    String venue,
    String competition,
    String status
) {
}
