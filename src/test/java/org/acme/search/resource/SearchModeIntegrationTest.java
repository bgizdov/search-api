package org.acme.search.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SearchModeIntegrationTest {

    @Test
    @Order(1)
    void testSearchModesEndpoint() {
        given()
            .when().get("/api/search/modes")
            .then()
            .statusCode(200)
            .body("modes", hasKey("CASE_INSENSITIVE"))
            .body("modes", hasKey("CASE_SENSITIVE"))
            .body("modes", hasKey("FULL_MATCH"))
            .body("modes.CASE_INSENSITIVE.default", equalTo(true))
            .body("examples", hasKey("case_insensitive"));
    }

    @Test
    @Order(2)
    void testCaseInsensitiveSearch() {
        // Wait a bit for data to be indexed
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        given()
            .queryParam("q", "barcelona")
            .queryParam("mode", "case_insensitive")
            .queryParam("size", "10")
            .when().get("/api/search")
            .then()
            .statusCode(200)
            .body("totalResults", greaterThanOrEqualTo(0));
    }

    @Test
    @Order(3)
    void testCaseSensitiveSearch() {
        given()
            .queryParam("q", "Barcelona")
            .queryParam("mode", "case_sensitive")
            .queryParam("size", "10")
            .when().get("/api/search")
            .then()
            .statusCode(200)
            .body("totalResults", greaterThanOrEqualTo(0));
    }

    @Test
    @Order(4)
    void testFullMatchSearch() {
        given()
            .queryParam("q", "Barcelona")
            .queryParam("mode", "full_match")
            .queryParam("size", "10")
            .when().get("/api/search")
            .then()
            .statusCode(200)
            .body("totalResults", greaterThanOrEqualTo(0));
    }

    @Test
    @Order(5)
    void testInvalidSearchMode() {
        given()
            .queryParam("q", "test")
            .queryParam("mode", "invalid_mode")
            .when().get("/api/search")
            .then()
            .statusCode(400)
            .body("error", containsString("Unknown search mode"));
    }

    @Test
    @Order(6)
    void testDefaultSearchMode() {
        given()
            .queryParam("q", "test")
            .queryParam("size", "5")
            .when().get("/api/search")
            .then()
            .statusCode(200)
            .body("totalResults", greaterThanOrEqualTo(0));
    }

    @Test
    @Order(7)
    void testTypeSpecificSearchWithMode() {
        given()
            .queryParam("type", "matches")
            .queryParam("q", "Barcelona")
            .queryParam("mode", "case_sensitive")
            .queryParam("size", "5")
            .when().get("/api/search")
            .then()
            .statusCode(200);
    }
}
