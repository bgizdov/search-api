package org.acme.search.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.search.dto.football.Match;
import org.acme.search.dto.potm.PlayerOfTheMatch;
import org.acme.search.dto.predictor.GameInstance;
import org.acme.search.dto.classicquiz.ClassicQuizPublicDto;
import org.acme.search.dto.UnifiedSearchResponse;
import org.acme.search.service.SearchService;

import java.util.List;
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
     * GET /api/search?type=matches&q=searchTerm&size=10
     * GET /api/search?type=matches&id=1
     * GET /api/search?q=searchTerm&size=10 (searches all types)
     */
    @GET
    @Path("/search")
    public Response unifiedSearch(
            @QueryParam("type") String type,
            @QueryParam("id") String idStr,
            @QueryParam("q") String query,
            @QueryParam("size") @DefaultValue("10") int size) {

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
                UnifiedSearchResponse result = searchService.searchAllTypes(query, size);
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

            Object result = searchService.unifiedSearch(type, id, query, size);

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


}
