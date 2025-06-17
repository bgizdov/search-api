package org.acme.search.dto.classicquiz;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO representing a classic quiz (replaces QuizGame)
 */
public record ClassicQuizPublicDto(
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
    Boolean isActive,
    String type,
    GameImagesDto images,
    int participationCount,
    int questionsCount,
    String status,
    String authRequirement,
    List<String> flags,
    int time,
    Map<String, String> customFields,
    Map<String, String> labels,
    float averageScore,
    int perfectScore,
    String adContent,
    BrandingDTO branding,
    boolean scored,
    String language,
    String alternativeTitle,
    int points,
    int maxAttempts,
    LocalDateTime updatedAt
) {
}
