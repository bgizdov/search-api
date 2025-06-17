package org.acme.search.dto.classicquiz;

/**
 * DTO representing branding URLs
 */
public record BrandingUrlsDTO(
    String primaryUrl,
    String secondaryUrl,
    String privacyPolicyUrl,
    String termsAndConditionsUrl,
    String additionalUrl
) {
}
