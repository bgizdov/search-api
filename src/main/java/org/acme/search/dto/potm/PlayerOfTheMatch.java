package org.acme.search.dto.potm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO representing a player of the match game
 */
public record PlayerOfTheMatch(
    Long id,
    Long matchId,
    String gameTitle,
    List<String> playerOptions,
    String correctPlayer,
    String userId,
    String selectedPlayer,
    Integer points,
    LocalDateTime submissionTime,
    Boolean isCorrect,
    String gameStatus, // "ACTIVE", "COMPLETED", "EXPIRED"
    String matchIdStr,
    Map<String, Integer> votes
) {
}
