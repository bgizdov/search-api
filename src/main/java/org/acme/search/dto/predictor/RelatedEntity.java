package org.acme.search.dto.predictor;

/**
 * DTO representing a related entity
 */
public record RelatedEntity(
    String entityId,
    String entityType,
    String entityRelationship
) {
}
