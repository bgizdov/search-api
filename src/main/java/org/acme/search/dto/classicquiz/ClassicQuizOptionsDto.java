package org.acme.search.dto.classicquiz;

/**
 * DTO representing classic quiz options
 */
public record ClassicQuizOptionsDto(
    int optionId,
    String option,
    Boolean correct,
    GameImagesDto images
) {
}
