package org.acme.search.dto.football;

import java.util.List;

/**
 * DTO representing a football team
 */
public record Team(
    String id,
    Country country,
    String name,
    String fullName,
    String shortName,
    boolean national,
    String code,
    String gender,
    Boolean undecided,
    Boolean isDeleted
) {
}
