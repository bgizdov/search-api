package org.acme.search.dto.classicquiz;

/**
 * DTO representing branding colors
 */
public record BrandingColorsDTO(
    String primaryColor,
    String secondaryColor,
    String contentColor,
    String backgroundColor,
    String borderColor,
    String additionalColor
) {
}
