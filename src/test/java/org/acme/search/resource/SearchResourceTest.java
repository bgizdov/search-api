package org.acme.search.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class SearchResourceTest {

    @Test
    void testSearchMatchesEndpoint() {
        given()
            .when().get("/api/matches")
            .then()
            .statusCode(200);
    }

    @Test
    void testSearchPredictionsEndpoint() {
        given()
            .when().get("/api/predictions")
            .then()
            .statusCode(200);
    }

    @Test
    void testSearchQuizGamesEndpoint() {
        given()
            .when().get("/api/quiz-games")
            .then()
            .statusCode(200);
    }

    @Test
    void testSearchPlayerGamesEndpoint() {
        given()
            .when().get("/api/player-games")
            .then()
            .statusCode(200);
    }

    @Test
    void testSearchMatchesWithQuery() {
        given()
            .queryParam("q", "test")
            .queryParam("size", "5")
            .when().get("/api/matches")
            .then()
            .statusCode(200);
    }
}
