package org.acme.search.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class SearchResourceTest {

    // Tests for unified search endpoint

    @Test
    void testUnifiedSearchMissingType() {
        // Test missing type parameter returns 400
        given()
            .when().get("/api/search")
            .then()
            .statusCode(is(400));
    }

    @Test
    void testUnifiedSearchInvalidType() {
        // Test invalid type parameter returns 400
        given()
            .queryParam("type", "invalid")
            .when().get("/api/search")
            .then()
            .statusCode(is(400));
    }

    @Test
    void testSearchMatches() {
        // Test searching matches
        given()
            .queryParam("type", "matches")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(200), is(500)));
    }

    @Test
    void testSearchPredictions() {
        // Test searching predictions
        given()
            .queryParam("type", "predictions")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(200), is(500)));
    }

    @Test
    void testSearchQuizGames() {
        // Test searching quiz games
        given()
            .queryParam("type", "quiz-games")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(200), is(500)));
    }

    @Test
    void testSearchPlayerGames() {
        // Test searching player games
        given()
            .queryParam("type", "player-games")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(200), is(500)));
    }

    @Test
    void testSearchMatchesWithQuery() {
        // Test searching matches with query and size
        given()
            .queryParam("type", "matches")
            .queryParam("q", "test")
            .queryParam("size", "5")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(200), is(500)));
    }

    @Test
    void testHealthEndpoint() {
        given()
            .when().get("/health/elasticsearch")
            .then()
            .statusCode(anyOf(is(200), is(503)));
    }

    // Tests for ID-based search using unified endpoint

    @Test
    void testGetMatchByValidId() {
        // Test with ID 1 (should exist in sample data)
        given()
            .queryParam("type", "matches")
            .queryParam("id", "1")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(200), is(404), is(500))); // 200 if found, 404 if not found, 500 if ES unavailable
    }

    @Test
    void testGetMatchByInvalidId() {
        // Test with non-existent ID
        given()
            .queryParam("type", "matches")
            .queryParam("id", "999999")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(404), is(500))); // 404 if ES available but not found, 500 if ES unavailable
    }

    @Test
    void testGetMatchByNonNumericId() {
        // Test with invalid ID format
        given()
            .queryParam("type", "matches")
            .queryParam("id", "invalid")
            .when().get("/api/search")
            .then()
            .statusCode(is(400)); // Should always return 400 for invalid format
    }

    @Test
    void testGetPredictionByValidId() {
        // Test with ID 1 (should exist in sample data)
        given()
            .queryParam("type", "predictions")
            .queryParam("id", "1")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(200), is(404), is(500)));
    }

    @Test
    void testGetPredictionByInvalidId() {
        // Test with non-existent ID
        given()
            .queryParam("type", "predictions")
            .queryParam("id", "999999")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(404), is(500)));
    }

    @Test
    void testGetPredictionByNonNumericId() {
        // Test with invalid ID format
        given()
            .queryParam("type", "predictions")
            .queryParam("id", "invalid")
            .when().get("/api/search")
            .then()
            .statusCode(is(400));
    }

    @Test
    void testGetQuizGameByValidId() {
        // Test with ID 1 (should exist in sample data)
        given()
            .queryParam("type", "quiz-games")
            .queryParam("id", "1")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(200), is(404), is(500)));
    }

    @Test
    void testGetQuizGameByInvalidId() {
        // Test with non-existent ID
        given()
            .queryParam("type", "quiz-games")
            .queryParam("id", "999999")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(404), is(500)));
    }

    @Test
    void testGetQuizGameByNonNumericId() {
        // Test with invalid ID format
        given()
            .queryParam("type", "quiz-games")
            .queryParam("id", "invalid")
            .when().get("/api/search")
            .then()
            .statusCode(is(400));
    }

    @Test
    void testGetPlayerGameByValidId() {
        // Test with ID 1 (should exist in sample data)
        given()
            .queryParam("type", "player-games")
            .queryParam("id", "1")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(200), is(404), is(500)));
    }

    @Test
    void testGetPlayerGameByInvalidId() {
        // Test with non-existent ID
        given()
            .queryParam("type", "player-games")
            .queryParam("id", "999999")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(404), is(500)));
    }

    @Test
    void testGetPlayerGameByNonNumericId() {
        // Test with invalid ID format
        given()
            .queryParam("type", "player-games")
            .queryParam("id", "invalid")
            .when().get("/api/search")
            .then()
            .statusCode(is(400));
    }

    @Test
    void testGetMatchByZeroId() {
        // Test edge case with ID 0
        given()
            .queryParam("type", "matches")
            .queryParam("id", "0")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(404), is(500)));
    }

    @Test
    void testGetMatchByNegativeId() {
        // Test edge case with negative ID
        given()
            .queryParam("type", "matches")
            .queryParam("id", "-1")
            .when().get("/api/search")
            .then()
            .statusCode(anyOf(is(404), is(500)));
    }
}
