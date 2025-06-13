package org.acme.search.dto;

import java.time.LocalDateTime;

/**
 * DTO representing football match data
 */
public record FootballMatchData(
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
