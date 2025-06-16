package org.acme.search.dto;

import java.util.List;

/**
 * DTO representing a unified search response containing multiple types of data
 */
public record UnifiedSearchResponse(
    List<FootballMatchData> matches,
    List<PredictionToMatch> predictions,
    List<QuizGame> quizGames,
    List<PlayerOfTheMatchGame> playerGames,
    int totalResults
) {
    
    /**
     * Calculate total results across all types
     */
    public static UnifiedSearchResponse of(
            List<FootballMatchData> matches,
            List<PredictionToMatch> predictions,
            List<QuizGame> quizGames,
            List<PlayerOfTheMatchGame> playerGames) {
        
        int total = matches.size() + predictions.size() + quizGames.size() + playerGames.size();
        
        return new UnifiedSearchResponse(matches, predictions, quizGames, playerGames, total);
    }
}
