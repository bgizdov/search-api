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
    List<Match> footballMatches,
    List<GameInstance> games,
    List<ClassicQuizPublicDto> classicQuizzes,
    List<PlayerOfTheMatch> playerOfTheMatchGames,
    int totalResults
) {

    /**
     * Calculate total results across all types
     */
    public static UnifiedSearchResponse of(
            List<Match> footballMatches,
            List<GameInstance> games,
            List<ClassicQuizPublicDto> classicQuizzes,
            List<PlayerOfTheMatch> playerOfTheMatchGames) {

        int total = footballMatches.size() + games.size() + classicQuizzes.size() + playerOfTheMatchGames.size();

        return new UnifiedSearchResponse(footballMatches, games, classicQuizzes, playerOfTheMatchGames, total);
    }
}
