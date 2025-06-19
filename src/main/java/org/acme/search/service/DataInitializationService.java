package org.acme.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.acme.search.config.SampleDataConfig;
import org.acme.search.dto.football.*;
import org.acme.search.dto.potm.PlayerOfTheMatch;
import org.acme.search.dto.predictor.GameInstance;
import org.acme.search.dto.classicquiz.ClassicQuizPublicDto;
import org.acme.search.util.PerformanceDataGenerator;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service to initialize sample data in Elasticsearch on startup
 */
@ApplicationScoped
public class DataInitializationService {

    private static final Logger LOG = Logger.getLogger(DataInitializationService.class);

    @Inject
    RestClient restClient;

    @Inject
    SampleDataConfig sampleDataConfig;

    private final ObjectMapper objectMapper;

    public DataInitializationService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    void onStart(@Observes StartupEvent ev) {
        // Use a separate thread to avoid blocking startup
        new Thread(() -> {
            try {
                // Wait a bit for Elasticsearch to be ready (Dev Services need time to start)
                Thread.sleep(10000);

                SampleDataConfig.Mode mode = sampleDataConfig.mode();
                LOG.infof("Sample data mode: %s", mode);

                if (mode == SampleDataConfig.Mode.NONE) {
                    LOG.info("Sample data loading is disabled");
                    return;
                }

                LOG.info("Initializing sample data in Elasticsearch...");
                initializeSampleData(mode);
                LOG.info("Sample data initialization completed.");
            } catch (Exception e) {
                LOG.warn("Failed to initialize sample data: " + e.getMessage());
                LOG.info("Application will continue without sample data. Elasticsearch might not be available.");
            }
        }).start();
    }

    private void initializeSampleData(SampleDataConfig.Mode mode) throws Exception {
        switch (mode) {
            case BASIC -> {
                LOG.info("Loading basic sample data...");
                createBasicSampleData();
            }
            case PERFORMANCE_SMALL -> {
                LOG.info("Loading performance test data (small)...");
                int recordsPerType = Math.max(1, sampleDataConfig.recordsPerType());
                createPerformanceData(recordsPerType);
            }
            case PERFORMANCE_LARGE -> {
                LOG.info("Loading performance test data (large)...");
                createPerformanceData(250_000); // 1M total records (250k per type)
            }
            default -> {
                LOG.warn("Unknown sample data mode: " + mode);
                createBasicSampleData();
            }
        }
    }

    private void createBasicSampleData() throws Exception {
        // Create sample football matches
        createSampleMatches();

        // Create sample predictions
        createSamplePredictions();

        // Create sample quiz games
        createSampleQuizGames();

        // Create sample player games
        createSamplePlayerGames();
    }

    private void createPerformanceData(int recordsPerType) throws Exception {
        PerformanceDataGenerator generator = new PerformanceDataGenerator(restClient);
        generator.generatePerformanceData(recordsPerType);
    }

    private void createSampleMatches() throws Exception {
        // Create simplified match data that works well with Elasticsearch
        String matchData1 = """
            {
                "id": "fb:m:123",
                "kickoffAt": %d,
                "finishedAt": %d,
                "updatedAt": %d,
                "status": {
                    "id": 1,
                    "type": "finished",
                    "name": "Finished",
                    "code": "FT"
                },
                "homeTeam": {
                    "id": "fb:t:456",
                    "name": "Barcelona",
                    "shortName": "Barca"
                },
                "awayTeam": {
                    "id": "fb:t:789",
                    "name": "Real Madrid",
                    "shortName": "Real"
                },
                "competition": {
                    "id": "fb:c:101",
                    "name": "La Liga"
                },
                "goalsFullTimeHome": 2,
                "goalsFullTimeAway": 1,
                "goalsHalfTimeHome": 1,
                "goalsHalfTimeAway": 0,
                "venue": "Camp Nou",
                "referee": "Carlos del Cerro Grande",
                "lineupsConfirmed": true,
                "startedAt": %d,
                "minute": "90+3",
                "isDeleted": false,
                "undecided": false
            }
            """;

        String matchData2 = """
            {
                "id": "fb:m:124",
                "kickoffAt": %d,
                "finishedAt": %d,
                "updatedAt": %d,
                "status": {
                    "id": 1,
                    "type": "finished",
                    "name": "Finished",
                    "code": "FT"
                },
                "homeTeam": {
                    "id": "fb:t:111",
                    "name": "Manchester United",
                    "shortName": "Man Utd"
                },
                "awayTeam": {
                    "id": "fb:t:222",
                    "name": "Liverpool",
                    "shortName": "Liverpool"
                },
                "competition": {
                    "id": "fb:c:102",
                    "name": "Premier League"
                },
                "goalsFullTimeHome": 1,
                "goalsFullTimeAway": 3,
                "goalsHalfTimeHome": 0,
                "goalsHalfTimeAway": 2,
                "venue": "Old Trafford",
                "referee": "Michael Oliver",
                "lineupsConfirmed": true,
                "startedAt": %d,
                "minute": "90+5",
                "isDeleted": false,
                "undecided": false
            }
            """;

        String matchData3 = """
            {
                "id": "fb:m:125",
                "kickoffAt": %d,
                "updatedAt": %d,
                "status": {
                    "id": 2,
                    "type": "scheduled",
                    "name": "Scheduled",
                    "code": "NS"
                },
                "homeTeam": {
                    "id": "fb:t:333",
                    "name": "Bayern Munich",
                    "shortName": "Bayern"
                },
                "awayTeam": {
                    "id": "fb:t:444",
                    "name": "Borussia Dortmund",
                    "shortName": "BVB"
                },
                "competition": {
                    "id": "fb:c:103",
                    "name": "Bundesliga"
                },
                "venue": "Allianz Arena",
                "referee": "Felix Brych",
                "lineupsConfirmed": false,
                "isDeleted": false,
                "undecided": false
            }
            """;

        long now = System.currentTimeMillis();
        long yesterday = now - 24 * 60 * 60 * 1000;
        long twoDaysAgo = now - 2 * 24 * 60 * 60 * 1000;
        long tomorrow = now + 24 * 60 * 60 * 1000;

        // Index the matches as raw JSON
        indexRawDocument("football_matches", "fb:m:123", String.format(matchData1, yesterday, yesterday, now, yesterday));
        indexRawDocument("football_matches", "fb:m:124", String.format(matchData2, twoDaysAgo, twoDaysAgo, now, twoDaysAgo));
        indexRawDocument("football_matches", "fb:m:125", String.format(matchData3, tomorrow, now));
    }

    private void createSamplePredictions() throws Exception {
        List<GameInstance> predictions = List.of(
            new GameInstance(1L, 1L, "user1", 2, 1, "HOME_WIN",
                LocalDateTime.now().minusHours(2), 85, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
            new GameInstance(2L, 2L, "user2", 0, 2, "AWAY_WIN",
                LocalDateTime.now().minusHours(3), 70, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
            new GameInstance(3L, 3L, "user1", 3, 1, "HOME_WIN",
                LocalDateTime.now().minusMinutes(30), 90, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
        );

        for (GameInstance prediction : predictions) {
            indexDocument("predictions", prediction.id().toString(), prediction);
        }
    }

    private void createSampleQuizGames() throws Exception {
        List<ClassicQuizPublicDto> quizGames = List.of(
            new ClassicQuizPublicDto(1L, "Football Trivia", "Test your football knowledge",
                List.of("Who won the 2022 World Cup?", "Which team has won the most Champions League titles?"),
                List.of("Argentina", "Real Madrid"),
                "Sports", 3, 300, LocalDateTime.now().minusDays(1), "admin", true, null, null, 0, 0, null, null, null, 0, null, null, 0.0f, 0, null, null, false, null, null, 0, 0, null),
            new ClassicQuizPublicDto(2L, "Premier League Quiz", "All about English football",
                List.of("Which team won the first Premier League title?", "Who is the top scorer in Premier League history?"),
                List.of("Manchester United", "Alan Shearer"),
                "Sports", 4, 600, LocalDateTime.now().minusDays(2), "admin", true, null, null, 0, 0, null, null, null, 0, null, null, 0.0f, 0, null, null, false, null, null, 0, 0, null)
        );

        for (ClassicQuizPublicDto quiz : quizGames) {
            indexDocument("quiz_games", quiz.id().toString(), quiz);
        }
    }

    private void createSamplePlayerGames() throws Exception {
        List<PlayerOfTheMatch> playerGames = List.of(
            new PlayerOfTheMatch(1L, 1L, "El Clasico Player of the Match",
                List.of("Lionel Messi", "Karim Benzema", "Pedri", "Vinicius Jr."),
                "Lionel Messi", "user1", "Lionel Messi", 10,
                LocalDateTime.now().minusHours(1), true, "COMPLETED", "1",
                Map.of("Lionel Messi", 150, "Karim Benzema", 75, "Pedri", 45, "Vinicius Jr.", 30)),
            new PlayerOfTheMatch(2L, 2L, "Premier League POTM",
                List.of("Mohamed Salah", "Bruno Fernandes", "Virgil van Dijk", "Marcus Rashford"),
                "Mohamed Salah", "user2", "Bruno Fernandes", 0,
                LocalDateTime.now().minusHours(2), false, "COMPLETED", "2",
                Map.of("Mohamed Salah", 200, "Bruno Fernandes", 120, "Virgil van Dijk", 80, "Marcus Rashford", 60))
        );

        for (PlayerOfTheMatch game : playerGames) {
            indexDocument("player_games", game.id().toString(), game);
        }
    }

    private void indexDocument(String index, String id, Object document) throws Exception {
        String json = objectMapper.writeValueAsString(document);
        Request request = new Request("PUT", "/" + index + "/_doc/" + id);
        request.setJsonEntity(json);
        restClient.performRequest(request);
        LOG.debug("Indexed document in " + index + " with id: " + id);
    }

    private void indexRawDocument(String index, String id, String jsonDocument) throws Exception {
        Request request = new Request("PUT", "/" + index + "/_doc/" + id);
        request.setJsonEntity(jsonDocument);
        restClient.performRequest(request);
        LOG.debug("Indexed raw document in " + index + " with id: " + id);
    }
}
