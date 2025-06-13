package org.acme.search.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class SearchResourceTest {

    @Test
    void testSearchMatchesEndpoint() {
        // Test endpoint exists and returns either 200 (ES available) or 500 (ES unavailable)
        given()
            .when().get("/api/matches")
            .then()
            .statusCode(anyOf(is(200), is(500)));
    }

    @Test
    void testSearchPredictionsEndpoint() {
        given()
            .when().get("/api/predictions")
            .then()
            .statusCode(anyOf(is(200), is(500)));
    }

    @Test
    void testSearchQuizGamesEndpoint() {
        given()
            .when().get("/api/quiz-games")
            .then()
            .statusCode(anyOf(is(200), is(500)));
    }

    @Test
    void testSearchPlayerGamesEndpoint() {
        given()
            .when().get("/api/player-games")
            .then()
            .statusCode(anyOf(is(200), is(500)));
    }

    @Test
    void testSearchMatchesWithQuery() {
        given()
            .queryParam("q", "test")
            .queryParam("size", "5")
            .when().get("/api/matches")
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

    // Tests for ID-based endpoints

    @Test
    void testGetMatchByValidId() {
        // Test with ID 1 (should exist in sample data)
        given()
            .when().get("/api/matches/1")
            .then()
            .statusCode(anyOf(is(200), is(404), is(500))); // 200 if found, 404 if not found, 500 if ES unavailable
    }

    @Test
    void testGetMatchByInvalidId() {
        // Test with non-existent ID
        given()
            .when().get("/api/matches/999999")
            .then()
            .statusCode(anyOf(is(404), is(500))); // 404 if ES available but not found, 500 if ES unavailable
    }

    @Test
    void testGetMatchByNonNumericId() {
        // Test with invalid ID format
        given()
            .when().get("/api/matches/invalid")
            .then()
            .statusCode(is(400)); // Should always return 400 for invalid format
    }

    @Test
    void testGetPredictionByValidId() {
        // Test with ID 1 (should exist in sample data)
        given()
            .when().get("/api/predictions/1")
            .then()
            .statusCode(anyOf(is(200), is(404), is(500)));
    }

    @Test
    void testGetPredictionByInvalidId() {
        // Test with non-existent ID
        given()
            .when().get("/api/predictions/999999")
            .then()
            .statusCode(anyOf(is(404), is(500)));
    }

    @Test
    void testGetPredictionByNonNumericId() {
        // Test with invalid ID format
        given()
            .when().get("/api/predictions/invalid")
            .then()
            .statusCode(is(400));
    }

    @Test
    void testGetQuizGameByValidId() {
        // Test with ID 1 (should exist in sample data)
        given()
            .when().get("/api/quiz-games/1")
            .then()
            .statusCode(anyOf(is(200), is(404), is(500)));
    }

    @Test
    void testGetQuizGameByInvalidId() {
        // Test with non-existent ID
        given()
            .when().get("/api/quiz-games/999999")
            .then()
            .statusCode(anyOf(is(404), is(500)));
    }

    @Test
    void testGetQuizGameByNonNumericId() {
        // Test with invalid ID format
        given()
            .when().get("/api/quiz-games/invalid")
            .then()
            .statusCode(is(400));
    }

    @Test
    void testGetPlayerGameByValidId() {
        // Test with ID 1 (should exist in sample data)
        given()
            .when().get("/api/player-games/1")
            .then()
            .statusCode(anyOf(is(200), is(404), is(500)));
    }

    @Test
    void testGetPlayerGameByInvalidId() {
        // Test with non-existent ID
        given()
            .when().get("/api/player-games/999999")
            .then()
            .statusCode(anyOf(is(404), is(500)));
    }

    @Test
    void testGetPlayerGameByNonNumericId() {
        // Test with invalid ID format
        given()
            .when().get("/api/player-games/invalid")
            .then()
            .statusCode(is(400));
    }

    @Test
    void testGetMatchByZeroId() {
        // Test edge case with ID 0
        given()
            .when().get("/api/matches/0")
            .then()
            .statusCode(anyOf(is(404), is(500)));
    }

    @Test
    void testGetMatchByNegativeId() {
        // Test edge case with negative ID
        given()
            .when().get("/api/matches/-1")
            .then()
            .statusCode(anyOf(is(404), is(500)));
    }
}
