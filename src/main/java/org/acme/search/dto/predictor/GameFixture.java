package org.acme.search.dto.predictor;

/**
 * DTO representing a game fixture
 */
public record GameFixture(
    String matchId,
    MatchType matchType,
    String market,
    String matchStatus
) {
}
