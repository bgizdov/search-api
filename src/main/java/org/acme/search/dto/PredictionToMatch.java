package org.acme.search.dto;

import java.time.LocalDateTime;

/**
 * DTO representing a prediction for a football match
 */
public record PredictionToMatch(
    Long id,
    Long matchId,
    String userId,
    Integer predictedHomeScore,
    Integer predictedAwayScore,
    String predictedOutcome, // "HOME_WIN", "AWAY_WIN", "DRAW"
    LocalDateTime predictionTime,
    Integer confidence, // 1-100
    Boolean isCorrect
) {
}
