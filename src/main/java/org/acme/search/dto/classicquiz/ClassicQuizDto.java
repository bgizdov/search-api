package org.acme.search.dto.classicquiz;

import java.util.List;

/**
 * DTO representing a classic quiz with extended information
 */
public record ClassicQuizDto(
    String rules,
    List<ClassicQuizQuestionsDto> questions,
    BrandingDTO branding
) {
}
