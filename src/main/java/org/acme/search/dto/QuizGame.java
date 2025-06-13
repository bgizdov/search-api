package org.acme.search.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO representing a quiz game
 */
public record QuizGame(
    Long id,
    String title,
    String description,
    List<String> questions,
    List<String> correctAnswers,
    String category,
    Integer difficulty, // 1-5
    Integer timeLimit, // in seconds
    LocalDateTime createdAt,
    String createdBy,
    Boolean isActive
) {
}
