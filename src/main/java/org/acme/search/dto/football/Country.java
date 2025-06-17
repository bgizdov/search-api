package org.acme.search.dto.football;

/**
 * DTO representing a country
 */
public record Country(
    String id,
    String name,
    String alias,
    String countryCode
) {
}
