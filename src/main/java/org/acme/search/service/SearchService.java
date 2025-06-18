package org.acme.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.search.dto.football.SimpleMatch;
import org.acme.search.dto.potm.PlayerOfTheMatch;
import org.acme.search.dto.predictor.GameInstance;
import org.acme.search.dto.classicquiz.ClassicQuizPublicDto;
import org.acme.search.enums.SearchMode;
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
     * Search for football matches with default search mode
     */
    public List<SimpleMatch> searchFootballMatches(String query, int size) throws IOException {
        return searchFootballMatches(query, size, SearchMode.DEFAULT);
    }

    /**
     * Search for football matches with specified search mode
     */
    public List<SimpleMatch> searchFootballMatches(String query, int size, SearchMode mode) throws IOException {
        String searchQuery = buildSearchQuery(query, size, mode);
        Request request = new Request("POST", "/football_matches/_search");
        request.setJsonEntity(searchQuery);

        Response response = restClient.performRequest(request);
        return parseSearchResponse(response, SimpleMatch.class);
    }

    /**
     * Search for football matches (legacy method name for backward compatibility)
     */
    public List<SimpleMatch> searchMatches(String query, int size) throws IOException {
        return searchFootballMatches(query, size);
    }

    /**
     * Search for game instances (predictions) with default search mode
     */
    public List<GameInstance> searchGameInstances(String query, int size) throws IOException {
        return searchGameInstances(query, size, SearchMode.DEFAULT);
    }

    /**
     * Search for game instances (predictions) with specified search mode
     */
    public List<GameInstance> searchGameInstances(String query, int size, SearchMode mode) throws IOException {
        String searchQuery = buildSearchQuery(query, size, mode);
        Request request = new Request("POST", "/predictions/_search");
        request.setJsonEntity(searchQuery);

        Response response = restClient.performRequest(request);
        return parseSearchResponse(response, GameInstance.class);
    }

    /**
     * Search for predictions (legacy method name for backward compatibility)
     */
    public List<GameInstance> searchPredictions(String query, int size) throws IOException {
        return searchGameInstances(query, size);
    }

    /**
     * Search for classic quiz games with default search mode
     */
    public List<ClassicQuizPublicDto> searchClassicQuizzes(String query, int size) throws IOException {
        return searchClassicQuizzes(query, size, SearchMode.DEFAULT);
    }

    /**
     * Search for classic quiz games with specified search mode
     */
    public List<ClassicQuizPublicDto> searchClassicQuizzes(String query, int size, SearchMode mode) throws IOException {
        String searchQuery = buildSearchQuery(query, size, mode);
        Request request = new Request("POST", "/quiz_games/_search");
        request.setJsonEntity(searchQuery);

        Response response = restClient.performRequest(request);
        return parseSearchResponse(response, ClassicQuizPublicDto.class);
    }

    /**
     * Search for quiz games (legacy method name for backward compatibility)
     */
    public List<ClassicQuizPublicDto> searchQuizGames(String query, int size) throws IOException {
        return searchClassicQuizzes(query, size);
    }

    /**
     * Search for player of the match games with default search mode
     */
    public List<PlayerOfTheMatch> searchPlayerOfTheMatchGames(String query, int size) throws IOException {
        return searchPlayerOfTheMatchGames(query, size, SearchMode.DEFAULT);
    }

    /**
     * Search for player of the match games with specified search mode
     */
    public List<PlayerOfTheMatch> searchPlayerOfTheMatchGames(String query, int size, SearchMode mode) throws IOException {
        String searchQuery = buildSearchQuery(query, size, mode);
        Request request = new Request("POST", "/player_games/_search");
        request.setJsonEntity(searchQuery);

        Response response = restClient.performRequest(request);
        return parseSearchResponse(response, PlayerOfTheMatch.class);
    }

    /**
     * Search for player games (legacy method name for backward compatibility)
     */
    public List<PlayerOfTheMatch> searchPlayerGames(String query, int size) throws IOException {
        return searchPlayerOfTheMatchGames(query, size);
    }

    /**
     * Find a football match by ID
     */
    public Optional<SimpleMatch> findFootballMatchById(Long id) throws IOException {
        Request request = new Request("GET", "/football_matches/_doc/" + id);
        try {
            Response response = restClient.performRequest(request);
            return parseGetResponse(response, SimpleMatch.class);
        } catch (Exception e) {
            // Document not found or other error
            return Optional.empty();
        }
    }

    /**
     * Find a match by ID (legacy method name for backward compatibility)
     */
    public Optional<SimpleMatch> findMatchById(Long id) throws IOException {
        return findFootballMatchById(id);
    }

    /**
     * Find a game instance by ID
     */
    public Optional<GameInstance> findGameInstanceById(Long id) throws IOException {
        Request request = new Request("GET", "/predictions/_doc/" + id);
        try {
            Response response = restClient.performRequest(request);
            return parseGetResponse(response, GameInstance.class);
        } catch (Exception e) {
            // Document not found or other error
            return Optional.empty();
        }
    }

    /**
     * Find a prediction by ID (legacy method name for backward compatibility)
     */
    public Optional<GameInstance> findPredictionById(Long id) throws IOException {
        return findGameInstanceById(id);
    }

    /**
     * Find a classic quiz by ID
     */
    public Optional<ClassicQuizPublicDto> findClassicQuizById(Long id) throws IOException {
        Request request = new Request("GET", "/quiz_games/_doc/" + id);
        try {
            Response response = restClient.performRequest(request);
            return parseGetResponse(response, ClassicQuizPublicDto.class);
        } catch (Exception e) {
            // Document not found or other error
            return Optional.empty();
        }
    }

    /**
     * Find a quiz game by ID (legacy method name for backward compatibility)
     */
    public Optional<ClassicQuizPublicDto> findQuizGameById(Long id) throws IOException {
        return findClassicQuizById(id);
    }

    /**
     * Find a player of the match game by ID
     */
    public Optional<PlayerOfTheMatch> findPlayerOfTheMatchGameById(Long id) throws IOException {
        Request request = new Request("GET", "/player_games/_doc/" + id);
        try {
            Response response = restClient.performRequest(request);
            return parseGetResponse(response, PlayerOfTheMatch.class);
        } catch (Exception e) {
            // Document not found or other error
            return Optional.empty();
        }
    }

    /**
     * Find a player game by ID (legacy method name for backward compatibility)
     */
    public Optional<PlayerOfTheMatch> findPlayerGameById(Long id) throws IOException {
        return findPlayerOfTheMatchGameById(id);
    }

    /**
     * Search across all entity types when no type is specified
     */
    public UnifiedSearchResponse searchAllTypes(String query, int size) throws IOException {
        return searchAllTypes(query, size, SearchMode.DEFAULT);
    }

    /**
     * Search across all entity types with specified search mode
     */
    public UnifiedSearchResponse searchAllTypes(String query, int size, SearchMode mode) throws IOException {
        // Search each type with a smaller size to distribute results
        int sizePerType = Math.max(1, size / 4); // Divide size among 4 types

        List<SimpleMatch> footballMatches = searchFootballMatches(query, sizePerType, mode);
        List<GameInstance> gameInstances = searchGameInstances(query, sizePerType, mode);
        List<ClassicQuizPublicDto> classicQuizzes = searchClassicQuizzes(query, sizePerType, mode);
        List<PlayerOfTheMatch> playerOfTheMatchGames = searchPlayerOfTheMatchGames(query, sizePerType, mode);

        return UnifiedSearchResponse.of(footballMatches, gameInstances, classicQuizzes, playerOfTheMatchGames);
    }

    /**
     * Unified search method that handles all entity types
     */
    public Object unifiedSearch(String type, Long id, String query, int size) throws IOException {
        return unifiedSearch(type, id, query, size, SearchMode.DEFAULT);
    }

    /**
     * Unified search method that handles all entity types with search mode
     */
    public Object unifiedSearch(String type, Long id, String query, int size, SearchMode mode) throws IOException {
        return switch (type.toLowerCase()) {
            case "matches", "football-matches" -> {
                if (id != null) {
                    yield findFootballMatchById(id);
                } else {
                    yield searchFootballMatches(query, size, mode);
                }
            }
            case "predictions", "game-instances" -> {
                if (id != null) {
                    yield findGameInstanceById(id);
                } else {
                    yield searchGameInstances(query, size, mode);
                }
            }
            case "quiz-games", "classic-quizzes" -> {
                if (id != null) {
                    yield findClassicQuizById(id);
                } else {
                    yield searchClassicQuizzes(query, size, mode);
                }
            }
            case "player-games", "player-of-the-match-games" -> {
                if (id != null) {
                    yield findPlayerOfTheMatchGameById(id);
                } else {
                    yield searchPlayerOfTheMatchGames(query, size, mode);
                }
            }
            default -> throw new IllegalArgumentException("Unsupported type: " + type + ". Supported types: matches, predictions, quiz-games, player-games");
        };
    }

    /**
     * Build Elasticsearch search query with default search mode
     */
    private String buildSearchQuery(String query, int size) {
        return buildSearchQuery(query, size, SearchMode.DEFAULT);
    }

    /**
     * Build Elasticsearch search query with specified search mode
     */
    private String buildSearchQuery(String query, int size, SearchMode mode) {
        if (query == null || query.trim().isEmpty()) {
            return String.format("""
                {
                  "size": %d,
                  "query": {
                    "match_all": {}
                  }
                }
                """, size);
        }

        return switch (mode) {
            case CASE_INSENSITIVE -> buildCaseInsensitiveQuery(query, size);
            case CASE_SENSITIVE -> buildCaseSensitiveQuery(query, size);
            case FULL_MATCH -> buildFullMatchQuery(query, size);
        };
    }

    /**
     * Build case insensitive partial match query (default behavior)
     */
    private String buildCaseInsensitiveQuery(String query, int size) {
        return String.format("""
            {
              "size": %d,
              "query": {
                "multi_match": {
                  "query": "%s",
                  "type": "best_fields",
                  "fields": ["*"],
                  "fuzziness": "AUTO"
                }
              }
            }
            """, size, escapeJsonString(query));
    }

    /**
     * Build case sensitive partial match query
     * Uses a practical approach that works reliably
     */
    private String buildCaseSensitiveQuery(String query, int size) {
        String escapedQuery = escapeJsonString(query);

        return String.format("""
            {
              "size": %d,
              "query": {
                "bool": {
                  "should": [
                    {
                      "multi_match": {
                        "query": "%s",
                        "fields": ["title^3", "gameTitle^3", "name^3", "homeTeam.name^2", "awayTeam.name^2", "competition.name^2", "venue", "referee"],
                        "type": "best_fields",
                        "fuzziness": 0
                      }
                    },
                    {
                      "query_string": {
                        "query": "%s",
                        "fields": ["title", "gameTitle", "name", "homeTeam.name", "awayTeam.name", "competition.name", "venue", "referee"],
                        "default_operator": "AND"
                      }
                    }
                  ]
                }
              }
            }
            """, size, escapedQuery, escapedQuery);
    }

    /**
     * Build full string match query (case insensitive)
     * Uses exact phrase matching to find complete string matches
     */
    private String buildFullMatchQuery(String query, int size) {
        String escapedQuery = escapeJsonString(query);
        return String.format("""
            {
              "size": %d,
              "query": {
                "bool": {
                  "should": [
                    {
                      "match_phrase": {
                        "title": {
                          "query": "%s",
                          "slop": 0
                        }
                      }
                    },
                    {
                      "match_phrase": {
                        "gameTitle": {
                          "query": "%s",
                          "slop": 0
                        }
                      }
                    },
                    {
                      "match_phrase": {
                        "name": {
                          "query": "%s",
                          "slop": 0
                        }
                      }
                    },
                    {
                      "match_phrase": {
                        "homeTeam.name": {
                          "query": "%s",
                          "slop": 0
                        }
                      }
                    },
                    {
                      "match_phrase": {
                        "awayTeam.name": {
                          "query": "%s",
                          "slop": 0
                        }
                      }
                    },
                    {
                      "match_phrase": {
                        "competition.name": {
                          "query": "%s",
                          "slop": 0
                        }
                      }
                    },
                    {
                      "match_phrase": {
                        "venue": {
                          "query": "%s",
                          "slop": 0
                        }
                      }
                    }
                  ],
                  "minimum_should_match": 1
                }
              }
            }
            """, size, escapedQuery, escapedQuery, escapedQuery, escapedQuery, escapedQuery, escapedQuery, escapedQuery);
    }

    /**
     * Escape special characters in JSON strings
     */
    private String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    /**
     * Escape special characters for wildcard queries
     */
    private String escapeWildcardString(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("*", "\\*")
                   .replace("?", "\\?")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    /**
     * Escape special characters for regex queries
     */
    private String escapeRegexString(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace(".", "\\.")
                   .replace("*", "\\*")
                   .replace("?", "\\?")
                   .replace("+", "\\+")
                   .replace("^", "\\^")
                   .replace("$", "\\$")
                   .replace("(", "\\(")
                   .replace(")", "\\)")
                   .replace("[", "\\[")
                   .replace("]", "\\]")
                   .replace("{", "\\{")
                   .replace("}", "\\}")
                   .replace("|", "\\|")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
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
