package org.acme.search.dto.football;

import java.time.LocalDateTime;

/**
 * DTO representing a football match
 */
public record Match(
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
