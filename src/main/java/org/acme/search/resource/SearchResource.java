package org.acme.search.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.search.dto.UnifiedSearchResponse;
import org.acme.search.service.SearchService;
import org.acme.search.enums.SearchMode;

import java.util.Map;
import java.util.Optional;

/**
 * REST Resource for searching DTOs from Elasticsearch
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SearchResource {

    @Inject
    SearchService searchService;

    /**
     * Unified search endpoint for all entity types
     * GET /api/search?type=matches&q=searchTerm&size=10&mode=case_insensitive
     * GET /api/search?type=matches&id=1
     * GET /api/search?q=searchTerm&size=10&mode=full_match (searches all types)
     *
     * Search modes:
     * - case_insensitive (default): Case insensitive partial matching
     * - case_sensitive: Case sensitive partial matching
     * - full_match: Full string match (case insensitive)
     */
    @GET
    @Path("/search")
    public Response unifiedSearch(
            @QueryParam("type") String type,
            @QueryParam("id") String idStr,
            @QueryParam("q") String query,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("mode") String modeStr) {

        // Parse search mode
        SearchMode mode;
        try {
            mode = SearchMode.fromString(modeStr);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }

        // If no type is specified, search across all types
        if (type == null || type.trim().isEmpty()) {
            // ID-based search requires a type
            if (idStr != null && !idStr.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "ID-based search requires 'type' parameter. Supported types: matches, predictions, quiz-games, player-games"))
                        .build();
            }

            // Search across all types
            try {
                UnifiedSearchResponse result = searchService.searchAllTypes(query, size, mode);
                return Response.ok(result).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(Map.of("error", "Failed to search across all types: " + e.getMessage()))
                        .build();
            }
        }

        try {
            Long id = null;
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    id = Long.parseLong(idStr);
                } catch (NumberFormatException e) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("error", "Invalid ID format: " + idStr))
                            .build();
                }
            }

            Object result = searchService.unifiedSearch(type, id, query, size, mode);

            // Handle Optional results (when searching by ID)
            if (result instanceof Optional<?> optional) {
                if (optional.isPresent()) {
                    return Response.ok(optional.get()).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity(Map.of("error", type + " with ID " + id + " not found"))
                            .build();
                }
            }

            // Handle List results (when searching by query)
            return Response.ok(result).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to search " + type + ": " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get available search modes
     * GET /api/search/modes
     */
    @GET
    @Path("/search/modes")
    public Response getSearchModes() {
        return Response.ok(Map.of(
            "modes", Map.of(
                "CASE_INSENSITIVE", Map.of(
                    "name", "CASE_INSENSITIVE",
                    "description", SearchMode.CASE_INSENSITIVE.getDescription(),
                    "aliases", new String[]{"case_insensitive", "case-insensitive", "insensitive"},
                    "default", true
                ),
                "CASE_SENSITIVE", Map.of(
                    "name", "CASE_SENSITIVE",
                    "description", SearchMode.CASE_SENSITIVE.getDescription(),
                    "aliases", new String[]{"case_sensitive", "case-sensitive", "sensitive"},
                    "default", false
                ),
                "FULL_MATCH", Map.of(
                    "name", "FULL_MATCH",
                    "description", SearchMode.FULL_MATCH.getDescription(),
                    "aliases", new String[]{"full_match", "full-match", "full", "exact"},
                    "default", false
                )
            ),
            "examples", Map.of(
                "case_insensitive", "?q=game&mode=case_insensitive - matches 'Game 21', 'Player Game', 'GAME'",
                "case_sensitive", "?q=Game&mode=case_sensitive - matches 'Game 21', 'Player Game' but not 'game' or 'GAME'",
                "full_match", "?q=Player of the Match Game 21&mode=full_match - only matches exact title, not 'Game 21' or 'Game 22'"
            )
        )).build();
    }

}
