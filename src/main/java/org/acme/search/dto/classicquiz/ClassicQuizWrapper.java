package org.acme.search.dto.classicquiz;

import java.util.List;

/**
 * Wrapper DTO for ClassicQuizPublicDto with additional search metadata
 */
public record ClassicQuizWrapper(
    String id,
    String searchTitle,
    String searchDescription,
    List<String> tags,
    List<String> flags,
    List<String> entityIds,
    ClassicQuizPublicDto data
) {

    /**
     * Create a wrapper with search metadata for a ClassicQuizPublicDto
     */
    public static ClassicQuizWrapper of(ClassicQuizPublicDto quiz, String searchTitle, String searchDescription, 
                                       List<String> tags, List<String> flags, List<String> entityIds) {
        return new ClassicQuizWrapper(
            quiz.id() != null ? quiz.id().toString() : null,
            searchTitle,
            searchDescription,
            tags,
            flags,
            entityIds,
            quiz
        );
    }

    /**
     * Create a simple wrapper with minimal metadata
     */
    public static ClassicQuizWrapper of(ClassicQuizPublicDto quiz) {
        String title = quiz.title() != null ? quiz.title() : "Classic Quiz";
        String description = quiz.description() != null ? quiz.description() : "Quiz game";
        
        return new ClassicQuizWrapper(
            quiz.id() != null ? quiz.id().toString() : null,
            title,
            description,
            List.of("quiz", "game", quiz.category() != null ? quiz.category().toLowerCase() : "general"),
            quiz.flags() != null ? quiz.flags() : List.of(),
            List.of(quiz.id() != null ? quiz.id().toString() : ""),
            quiz
        );
    }
}
