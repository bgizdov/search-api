package org.acme.search.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class SearchModeIntegrationTest {

    @Test
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
    void testInvalidSearchMode() {
        given()
            .queryParam("q", "test")
            .queryParam("mode", "invalid_mode")
            .when().get("/api/search")
            .then()
            .statusCode(400)
            .body("error", containsString("Unknown search mode"));
    }
}
