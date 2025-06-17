package org.acme.search.dto.football;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO representing a football player
 */
public record Player(
    String id,
    String name,
    Country country,
    LocalDate birthDate,
    String firstName,
    String lastName,
    boolean active,
    String position,
    Boolean isDeleted
) {
}
