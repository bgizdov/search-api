package org.acme.search.dto.common;

/**
 * DTO representing a related entity
 */
public record RelatedEntity(
    String entityId,
    String entityType,
    String entityRelationship
) {
}
