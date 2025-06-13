package org.acme.search.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

import java.util.Map;

/**
 * Health check endpoint for Elasticsearch connectivity
 */
@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @Inject
    RestClient restClient;

    /**
     * Check Elasticsearch connectivity
     * GET /health/elasticsearch
     */
    @GET
    @Path("/elasticsearch")
    public Response checkElasticsearch() {
        try {
            Request request = new Request("GET", "/");
            restClient.performRequest(request);
            return Response.ok(Map.of(
                "status", "UP",
                "elasticsearch", "Connected"
            )).build();
        } catch (Exception e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(Map.of(
                    "status", "DOWN",
                    "elasticsearch", "Disconnected",
                    "error", e.getMessage()
                )).build();
        }
    }
}
