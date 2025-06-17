package org.acme.search.dto.predictor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.acme.search.dto.Images;
import org.acme.search.dto.RelatedEntity;

/**
 * DTO representing a game instance (replaces PredictionToMatch)
 */
public record GameInstance(
    Long id,
    Long matchId,
    String userId,
    Integer predictedHomeScore,
    Integer predictedAwayScore,
    String predictedOutcome, // "HOME_WIN", "AWAY_WIN", "DRAW"
    LocalDateTime predictionTime,
    Integer confidence, // 1-100
    Boolean isCorrect,
    String title,
    String description,
    GameType type,
    GameStatus status,
    AuthRequirement authRequirement,
    List<GameFixture> fixtures,
    String rules,
    List<String> flags,
    Images images,
    GameTiebreaker tiebreaker,
    Integer participantsCount,
    List<String> excludedProfileIds,
    LocalDateTime predictionsCutoff,
    LocalDateTime scheduleOpenAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime systemLastKickoff,
    List<RelatedEntity> related,
    Map<String, String> labels,
    Map<String, String> customFields
) {
}
