package org.acme.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.search.dto.football.Match;
import org.acme.search.dto.PlayerOfTheMatchGame;
import org.acme.search.dto.PredictionToMatch;
import org.acme.search.dto.QuizGame;
import org.acme.search.dto.UnifiedSearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for searching DTOs from Elasticsearch
 */
@ApplicationScoped
public class SearchService {

    @Inject
    RestClient restClient;

    private final ObjectMapper objectMapper;

    public SearchService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Search for football matches
     */
    public List<Match> searchMatches(String query, int size) throws IOException {
        String searchQuery = buildSearchQuery(query, size);
        Request request = new Request("POST", "/football_matches/_search");
        request.setJsonEntity(searchQuery);

        Response response = restClient.performRequest(request);
        return parseSearchResponse(response, Match.class);
    }

    /**
     * Search for predictions
     */
    public List<PredictionToMatch> searchPredictions(String query, int size) throws IOException {
        String searchQuery = buildSearchQuery(query, size);
        Request request = new Request("POST", "/predictions/_search");
        request.setJsonEntity(searchQuery);
        
        Response response = restClient.performRequest(request);
        return parseSearchResponse(response, PredictionToMatch.class);
    }

    /**
     * Search for quiz games
     */
    public List<QuizGame> searchQuizGames(String query, int size) throws IOException {
        String searchQuery = buildSearchQuery(query, size);
        Request request = new Request("POST", "/quiz_games/_search");
        request.setJsonEntity(searchQuery);
        
        Response response = restClient.performRequest(request);
        return parseSearchResponse(response, QuizGame.class);
    }

    /**
     * Search for player of the match games
     */
    public List<PlayerOfTheMatchGame> searchPlayerGames(String query, int size) throws IOException {
        String searchQuery = buildSearchQuery(query, size);
        Request request = new Request("POST", "/player_games/_search");
        request.setJsonEntity(searchQuery);

        Response response = restClient.performRequest(request);
        return parseSearchResponse(response, PlayerOfTheMatchGame.class);
    }

    /**
     * Find a football match by ID
     */
    public Optional<Match> findMatchById(Long id) throws IOException {
        Request request = new Request("GET", "/football_matches/_doc/" + id);
        try {
            Response response = restClient.performRequest(request);
            return parseGetResponse(response, Match.class);
        } catch (Exception e) {
            // Document not found or other error
            return Optional.empty();
        }
    }

    /**
     * Find a prediction by ID
     */
    public Optional<PredictionToMatch> findPredictionById(Long id) throws IOException {
        Request request = new Request("GET", "/predictions/_doc/" + id);
        try {
            Response response = restClient.performRequest(request);
            return parseGetResponse(response, PredictionToMatch.class);
        } catch (Exception e) {
            // Document not found or other error
            return Optional.empty();
        }
    }

    /**
     * Find a quiz game by ID
     */
    public Optional<QuizGame> findQuizGameById(Long id) throws IOException {
        Request request = new Request("GET", "/quiz_games/_doc/" + id);
        try {
            Response response = restClient.performRequest(request);
            return parseGetResponse(response, QuizGame.class);
        } catch (Exception e) {
            // Document not found or other error
            return Optional.empty();
        }
    }

    /**
     * Find a player game by ID
     */
    public Optional<PlayerOfTheMatchGame> findPlayerGameById(Long id) throws IOException {
        Request request = new Request("GET", "/player_games/_doc/" + id);
        try {
            Response response = restClient.performRequest(request);
            return parseGetResponse(response, PlayerOfTheMatchGame.class);
        } catch (Exception e) {
            // Document not found or other error
            return Optional.empty();
        }
    }

    /**
     * Search across all entity types when no type is specified
     */
    public UnifiedSearchResponse searchAllTypes(String query, int size) throws IOException {
        // Search each type with a smaller size to distribute results
        int sizePerType = Math.max(1, size / 4); // Divide size among 4 types

        List<Match> matches = searchMatches(query, sizePerType);
        List<PredictionToMatch> predictions = searchPredictions(query, sizePerType);
        List<QuizGame> quizGames = searchQuizGames(query, sizePerType);
        List<PlayerOfTheMatchGame> playerGames = searchPlayerGames(query, sizePerType);

        return UnifiedSearchResponse.of(matches, predictions, quizGames, playerGames);
    }

    /**
     * Unified search method that handles all entity types
     */
    public Object unifiedSearch(String type, Long id, String query, int size) throws IOException {
        return switch (type.toLowerCase()) {
            case "matches" -> {
                if (id != null) {
                    yield findMatchById(id);
                } else {
                    yield searchMatches(query, size);
                }
            }
            case "predictions" -> {
                if (id != null) {
                    yield findPredictionById(id);
                } else {
                    yield searchPredictions(query, size);
                }
            }
            case "quiz-games" -> {
                if (id != null) {
                    yield findQuizGameById(id);
                } else {
                    yield searchQuizGames(query, size);
                }
            }
            case "player-games" -> {
                if (id != null) {
                    yield findPlayerGameById(id);
                } else {
                    yield searchPlayerGames(query, size);
                }
            }
            default -> throw new IllegalArgumentException("Unsupported type: " + type + ". Supported types: matches, predictions, quiz-games, player-games");
        };
    }

    /**
     * Build Elasticsearch search query
     */
    private String buildSearchQuery(String query, int size) {
        if (query == null || query.trim().isEmpty()) {
            return String.format("""
                {
                  "size": %d,
                  "query": {
                    "match_all": {}
                  }
                }
                """, size);
        } else {
            return String.format("""
                {
                  "size": %d,
                  "query": {
                    "multi_match": {
                      "query": "%s",
                      "type": "best_fields",
                      "fields": ["*"]
                    }
                  }
                }
                """, size, query.replace("\"", "\\\""));
        }
    }

    /**
     * Parse Elasticsearch response and convert to DTOs
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> parseSearchResponse(Response response, Class<T> clazz) throws IOException {
        String responseBody = new String(response.getEntity().getContent().readAllBytes());
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

        Map<String, Object> hits = (Map<String, Object>) responseMap.get("hits");
        List<Map<String, Object>> hitsList = (List<Map<String, Object>>) hits.get("hits");

        List<T> results = new ArrayList<>();
        for (Map<String, Object> hit : hitsList) {
            Map<String, Object> source = (Map<String, Object>) hit.get("_source");
            T dto = objectMapper.convertValue(source, clazz);
            results.add(dto);
        }

        return results;
    }

    /**
     * Parse Elasticsearch GET response and convert to DTO
     */
    @SuppressWarnings("unchecked")
    private <T> Optional<T> parseGetResponse(Response response, Class<T> clazz) throws IOException {
        String responseBody = new String(response.getEntity().getContent().readAllBytes());
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

        Boolean found = (Boolean) responseMap.get("found");
        if (found != null && found) {
            Map<String, Object> source = (Map<String, Object>) responseMap.get("_source");
            T dto = objectMapper.convertValue(source, clazz);
            return Optional.of(dto);
        }

        return Optional.empty();
    }
}
