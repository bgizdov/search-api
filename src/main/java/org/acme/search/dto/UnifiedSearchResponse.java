package org.acme.search.dto;

import org.acme.search.dto.football.SimpleMatchWrapper;
import org.acme.search.dto.predictor.GameInstanceWrapper;
import org.acme.search.dto.classicquiz.ClassicQuizWrapper;
import org.acme.search.dto.potm.PlayerOfTheMatchWrapper;
import java.util.List;

/**
 * DTO representing a unified search response containing multiple types of data
 */
public record UnifiedSearchResponse(
    List<SimpleMatchWrapper> footballMatches,
    List<GameInstanceWrapper> gameInstances,
    List<ClassicQuizWrapper> classicQuizzes,
    List<PlayerOfTheMatchWrapper> playerOfTheMatchGames,
    int totalResults
) {

    /**
     * Calculate total results across all types
     */
    public static UnifiedSearchResponse of(
            List<SimpleMatchWrapper> footballMatches,
            List<GameInstanceWrapper> gameInstances,
            List<ClassicQuizWrapper> classicQuizzes,
            List<PlayerOfTheMatchWrapper> playerOfTheMatchGames) {

        int total = footballMatches.size() + gameInstances.size() + classicQuizzes.size() + playerOfTheMatchGames.size();

        return new UnifiedSearchResponse(footballMatches, gameInstances, classicQuizzes, playerOfTheMatchGames, total);
    }
}
