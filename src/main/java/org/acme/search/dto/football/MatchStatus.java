package org.acme.search.dto.football;

/**
 * DTO representing a match status
 */
public record MatchStatus(
    byte id,
    String type,
    String name,
    String code
) {
}
