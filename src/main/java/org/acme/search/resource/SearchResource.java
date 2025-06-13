package org.acme.search.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.search.dto.FootballMatchData;
import org.acme.search.dto.PlayerOfTheMatchGame;
import org.acme.search.dto.PredictionToMatch;
import org.acme.search.dto.QuizGame;
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
     * Search for football matches
     * GET /api/matches?q=searchTerm&size=10
     */
    @GET
    @Path("/matches")
    public Response searchMatches(
            @QueryParam("q") String query,
            @QueryParam("size") @DefaultValue("10") int size) {
        try {
            List<FootballMatchData> matches = searchService.searchMatches(query, size);
            return Response.ok(matches).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to search matches: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get a specific football match by ID
     * GET /api/matches/{id}
     */
    @GET
    @Path("/matches/{id}")
    public Response getMatchById(@PathParam("id") String idStr) {
        try {
            Long id = Long.parseLong(idStr);
            Optional<FootballMatchData> match = searchService.findMatchById(id);
            if (match.isPresent()) {
                return Response.ok(match.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Match with ID " + id + " not found"))
                        .build();
            }
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Invalid ID format: " + idStr))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to get match: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Search for predictions
     * GET /api/predictions?q=searchTerm&size=10
     */
    @GET
    @Path("/predictions")
    public Response searchPredictions(
            @QueryParam("q") String query,
            @QueryParam("size") @DefaultValue("10") int size) {
        try {
            List<PredictionToMatch> predictions = searchService.searchPredictions(query, size);
            return Response.ok(predictions).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to search predictions: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get a specific prediction by ID
     * GET /api/predictions/{id}
     */
    @GET
    @Path("/predictions/{id}")
    public Response getPredictionById(@PathParam("id") String idStr) {
        try {
            Long id = Long.parseLong(idStr);
            Optional<PredictionToMatch> prediction = searchService.findPredictionById(id);
            if (prediction.isPresent()) {
                return Response.ok(prediction.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Prediction with ID " + id + " not found"))
                        .build();
            }
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Invalid ID format: " + idStr))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to get prediction: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Search for quiz games
     * GET /api/quiz-games?q=searchTerm&size=10
     */
    @GET
    @Path("/quiz-games")
    public Response searchQuizGames(
            @QueryParam("q") String query,
            @QueryParam("size") @DefaultValue("10") int size) {
        try {
            List<QuizGame> quizGames = searchService.searchQuizGames(query, size);
            return Response.ok(quizGames).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to search quiz games: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get a specific quiz game by ID
     * GET /api/quiz-games/{id}
     */
    @GET
    @Path("/quiz-games/{id}")
    public Response getQuizGameById(@PathParam("id") String idStr) {
        try {
            Long id = Long.parseLong(idStr);
            Optional<QuizGame> quizGame = searchService.findQuizGameById(id);
            if (quizGame.isPresent()) {
                return Response.ok(quizGame.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Quiz game with ID " + id + " not found"))
                        .build();
            }
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Invalid ID format: " + idStr))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to get quiz game: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Search for player of the match games
     * GET /api/player-games?q=searchTerm&size=10
     */
    @GET
    @Path("/player-games")
    public Response searchPlayerGames(
            @QueryParam("q") String query,
            @QueryParam("size") @DefaultValue("10") int size) {
        try {
            List<PlayerOfTheMatchGame> playerGames = searchService.searchPlayerGames(query, size);
            return Response.ok(playerGames).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to search player games: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get a specific player game by ID
     * GET /api/player-games/{id}
     */
    @GET
    @Path("/player-games/{id}")
    public Response getPlayerGameById(@PathParam("id") String idStr) {
        try {
            Long id = Long.parseLong(idStr);
            Optional<PlayerOfTheMatchGame> playerGame = searchService.findPlayerGameById(id);
            if (playerGame.isPresent()) {
                return Response.ok(playerGame.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Player game with ID " + id + " not found"))
                        .build();
            }
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Invalid ID format: " + idStr))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Failed to get player game: " + e.getMessage()))
                    .build();
        }
    }
}
