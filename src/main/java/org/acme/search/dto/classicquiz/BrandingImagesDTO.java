package org.acme.search.dto.classicquiz;

/**
 * DTO representing branding images
 */
public record BrandingImagesDTO(
    String mainLogo,
    String mobileLogo,
    String backgroundImage,
    String mobileBackgroundImage,
    String additionalImage
) {
}
