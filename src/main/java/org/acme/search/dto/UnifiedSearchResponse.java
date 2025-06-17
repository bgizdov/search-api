package org.acme.search.dto;

import org.acme.search.dto.football.SimpleMatch;
import org.acme.search.dto.predictor.GameInstance;
import org.acme.search.dto.classicquiz.ClassicQuizPublicDto;
import org.acme.search.dto.potm.PlayerOfTheMatch;
import java.util.List;

/**
 * DTO representing a unified search response containing multiple types of data
 */
public record UnifiedSearchResponse(
    List<SimpleMatch> footballMatches,
    List<GameInstance> gameInstances,
    List<ClassicQuizPublicDto> classicQuizzes,
    List<PlayerOfTheMatch> playerOfTheMatchGames,
    int totalResults
) {

    /**
     * Calculate total results across all types
     */
    public static UnifiedSearchResponse of(
            List<SimpleMatch> footballMatches,
            List<GameInstance> gameInstances,
            List<ClassicQuizPublicDto> classicQuizzes,
            List<PlayerOfTheMatch> playerOfTheMatchGames) {

        int total = footballMatches.size() + gameInstances.size() + classicQuizzes.size() + playerOfTheMatchGames.size();

        return new UnifiedSearchResponse(footballMatches, gameInstances, classicQuizzes, playerOfTheMatchGames, total);
    }
}
