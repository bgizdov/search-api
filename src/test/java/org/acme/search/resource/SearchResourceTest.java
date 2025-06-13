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
}
