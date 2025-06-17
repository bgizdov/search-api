package org.acme.search.dto.football;

import java.util.Date;

/**
 * DTO representing a football match for search purposes
 */
public record Match(
    String id,
    Date kickoffAt,
    Date finishedAt,
    Date updatedAt,
    MatchStatus status,
    Team homeTeam,
    Team awayTeam,
    Competition competition,
    Byte goalsFullTimeHome,
    Byte goalsFullTimeAway,
    Byte goalsHalfTimeHome,
    Byte goalsHalfTimeAway,
    Byte goalsExtraTimeHome,
    Byte goalsExtraTimeAway,
    Byte goalsAggregateHome,
    Byte goalsAggregateAway,
    Byte goalsPenaltyHome,
    Byte goalsPenaltyAway,
    String venue,
    String referee,
    Boolean lineupsConfirmed,
    Date startedAt,
    String minute,
    Boolean isDeleted,
    Boolean undecided
) {
}
