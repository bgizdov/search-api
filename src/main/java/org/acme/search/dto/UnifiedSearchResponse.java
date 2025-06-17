package org.acme.search.dto;

import org.acme.search.dto.football.Match;
import org.acme.search.dto.predictor.GameInstance;
import org.acme.search.dto.classicquiz.ClassicQuizPublicDto;
import org.acme.search.dto.potm.PlayerOfTheMatch;
import java.util.List;

/**
 * DTO representing a unified search response containing multiple types of data
 */
public record UnifiedSearchResponse(
    List<Match> matches,
    List<GameInstance> predictions,
    List<ClassicQuizPublicDto> quizGames,
    List<PlayerOfTheMatch> playerGames,
    int totalResults
) {

    /**
     * Calculate total results across all types
     */
    public static UnifiedSearchResponse of(
            List<Match> matches,
            List<GameInstance> predictions,
            List<ClassicQuizPublicDto> quizGames,
            List<PlayerOfTheMatch> playerGames) {

        int total = matches.size() + predictions.size() + quizGames.size() + playerGames.size();

        return new UnifiedSearchResponse(matches, predictions, quizGames, playerGames, total);
    }
}
