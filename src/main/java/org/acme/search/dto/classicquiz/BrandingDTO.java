package org.acme.search.dto.classicquiz;

/**
 * DTO representing branding configuration
 */
public record BrandingDTO(
    BrandingColorsDTO colors,
    BrandingUrlsDTO urls,
    BrandingImagesDTO images
) {
}
