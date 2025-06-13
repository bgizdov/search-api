package org.acme.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.search.dto.FootballMatchData;
import org.acme.search.dto.PlayerOfTheMatchGame;
import org.acme.search.dto.PredictionToMatch;
import org.acme.search.dto.QuizGame;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<FootballMatchData> searchMatches(String query, int size) throws IOException {
        String searchQuery = buildSearchQuery(query, size);
        Request request = new Request("POST", "/football_matches/_search");
        request.setJsonEntity(searchQuery);
        
        Response response = restClient.performRequest(request);
        return parseSearchResponse(response, FootballMatchData.class);
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
}
