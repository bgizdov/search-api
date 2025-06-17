package org.acme.search.dto;

/**
 * DTO representing a related entity
 */
public record RelatedEntity(
    String entityId,
    String entityType,
    String entityRelationship
) {
}
