package org.acme.search.dto.classicquiz;

import java.util.List;

/**
 * DTO representing classic quiz questions
 */
public record ClassicQuizQuestionsDto(
    int questionId,
    String question,
    GameImagesDto images,
    List<ClassicQuizOptionsDto> options,
    String embedCode,
    String explanation
) {
}
