package org.acme.search.dto.football;

import java.util.List;

/**
 * DTO representing a football competition
 */
public record Competition(
    String id,
    Country country,
    String gender,
    String competitionType,
    String name
) {
}
