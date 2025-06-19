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
import org.acme.search.dto.potm.PlayerOfTheMatchWrapper;
import org.acme.search.dto.predictor.GameInstance;
import org.acme.search.dto.predictor.GameInstanceWrapper;
import org.acme.search.dto.classicquiz.ClassicQuizPublicDto;
import org.acme.search.dto.classicquiz.ClassicQuizWrapper;
import org.acme.search.util.PerformanceDataGenerator;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

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
    private final Random random = new Random();

    // ID generators for different entity types
    private final AtomicLong matchIdGenerator = new AtomicLong(1000);
    private final AtomicLong teamIdGenerator = new AtomicLong(2000);
    private final AtomicLong competitionIdGenerator = new AtomicLong(3000);
    private final AtomicLong gameInstanceIdGenerator = new AtomicLong(4000);
    private final AtomicLong quizIdGenerator = new AtomicLong(5000);
    private final AtomicLong playerGameIdGenerator = new AtomicLong(6000);

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
        // Generate IDs for teams and competitions
        String barcelonaId = generateTeamId();
        String realMadridId = generateTeamId();
        String manUtdId = generateTeamId();
        String liverpoolId = generateTeamId();
        String bayernId = generateTeamId();
        String dortmundId = generateTeamId();

        String laLigaId = generateCompetitionId();
        String premierLeagueId = generateCompetitionId();
        String bundesligaId = generateCompetitionId();

        // Generate match IDs
        String match1Id = generateMatchId();
        String match2Id = generateMatchId();
        String match3Id = generateMatchId();

        // Create simplified match data that works well with Elasticsearch
        String matchData1 = """
            {
                "id": "%s",
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
                    "id": "%s",
                    "name": "Barcelona",
                    "shortName": "Barca"
                },
                "awayTeam": {
                    "id": "%s",
                    "name": "Real Madrid",
                    "shortName": "Real"
                },
                "competition": {
                    "id": "%s",
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
                "id": "%s",
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
                    "id": "%s",
                    "name": "Manchester United",
                    "shortName": "Man Utd"
                },
                "awayTeam": {
                    "id": "%s",
                    "name": "Liverpool",
                    "shortName": "Liverpool"
                },
                "competition": {
                    "id": "%s",
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
                "id": "%s",
                "kickoffAt": %d,
                "updatedAt": %d,
                "status": {
                    "id": 2,
                    "type": "scheduled",
                    "name": "Scheduled",
                    "code": "NS"
                },
                "homeTeam": {
                    "id": "%s",
                    "name": "Bayern Munich",
                    "shortName": "Bayern"
                },
                "awayTeam": {
                    "id": "%s",
                    "name": "Borussia Dortmund",
                    "shortName": "BVB"
                },
                "competition": {
                    "id": "%s",
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

        // Create SimpleMatch objects and wrap them before indexing
        SimpleMatch match1 = objectMapper.readValue(
            String.format(matchData1, match1Id, barcelonaId, realMadridId, laLigaId, yesterday, yesterday, now, yesterday),
            SimpleMatch.class);
        SimpleMatch match2 = objectMapper.readValue(
            String.format(matchData2, match2Id, manUtdId, liverpoolId, premierLeagueId, twoDaysAgo, twoDaysAgo, now, twoDaysAgo),
            SimpleMatch.class);
        SimpleMatch match3 = objectMapper.readValue(
            String.format(matchData3, match3Id, bayernId, dortmundId, bundesligaId, tomorrow, now),
            SimpleMatch.class);

        // Create wrapper objects and index them
        SimpleMatchWrapper wrapper1 = SimpleMatchWrapper.of(match1);
        SimpleMatchWrapper wrapper2 = SimpleMatchWrapper.of(match2);
        SimpleMatchWrapper wrapper3 = SimpleMatchWrapper.of(match3);

        indexDocument("football_matches", match1Id, wrapper1);
        indexDocument("football_matches", match2Id, wrapper2);
        indexDocument("football_matches", match3Id, wrapper3);
    }

    private void createSamplePredictions() throws Exception {
        // Generate random match IDs (assuming they exist from previous matches)
        Long matchId1 = 1000L + random.nextInt(100);
        Long matchId2 = 1000L + random.nextInt(100);
        Long matchId3 = 1000L + random.nextInt(100);

        List<GameInstance> predictions = List.of(
            new GameInstance(generateGameInstanceId(), matchId1, "user1", 2, 1, "HOME_WIN",
                LocalDateTime.now().minusHours(2), 85, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
            new GameInstance(generateGameInstanceId(), matchId2, "user2", 0, 2, "AWAY_WIN",
                LocalDateTime.now().minusHours(3), 70, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
            new GameInstance(generateGameInstanceId(), matchId3, "user1", 3, 1, "HOME_WIN",
                LocalDateTime.now().minusMinutes(30), 90, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
        );

        for (GameInstance prediction : predictions) {
            GameInstanceWrapper wrapper = GameInstanceWrapper.of(prediction);
            indexDocument("predictions", prediction.id().toString(), wrapper);
        }
    }

    private void createSampleQuizGames() throws Exception {
        List<ClassicQuizPublicDto> quizGames = List.of(
            new ClassicQuizPublicDto(generateQuizId(), "Football Trivia", "Test your football knowledge",
                List.of("Who won the 2022 World Cup?", "Which team has won the most Champions League titles?"),
                List.of("Argentina", "Real Madrid"),
                "Sports", 3, 300, LocalDateTime.now().minusDays(1), "admin", true, null, null, 0, 0, null, null, null, 0, null, null, 0.0f, 0, null, null, false, null, null, 0, 0, null),
            new ClassicQuizPublicDto(generateQuizId(), "Premier League Quiz", "All about English football",
                List.of("Which team won the first Premier League title?", "Who is the top scorer in Premier League history?"),
                List.of("Manchester United", "Alan Shearer"),
                "Sports", 4, 600, LocalDateTime.now().minusDays(2), "admin", true, null, null, 0, 0, null, null, null, 0, null, null, 0.0f, 0, null, null, false, null, null, 0, 0, null)
        );

        for (ClassicQuizPublicDto quiz : quizGames) {
            ClassicQuizWrapper wrapper = ClassicQuizWrapper.of(quiz);
            indexDocument("quiz_games", quiz.id().toString(), wrapper);
        }
    }

    private void createSamplePlayerGames() throws Exception {
        // Generate random match IDs (assuming they exist from previous matches)
        Long matchId1 = 1000L + random.nextInt(100);
        Long matchId2 = 1000L + random.nextInt(100);

        List<PlayerOfTheMatch> playerGames = List.of(
            new PlayerOfTheMatch(generatePlayerGameId(), matchId1, "El Clasico Player of the Match",
                List.of("Lionel Messi", "Karim Benzema", "Pedri", "Vinicius Jr."),
                "Lionel Messi", "user1", "Lionel Messi", 10,
                LocalDateTime.now().minusHours(1), true, "COMPLETED", matchId1.toString(),
                Map.of("Lionel Messi", 150, "Karim Benzema", 75, "Pedri", 45, "Vinicius Jr.", 30)),
            new PlayerOfTheMatch(generatePlayerGameId(), matchId2, "Premier League POTM",
                List.of("Mohamed Salah", "Bruno Fernandes", "Virgil van Dijk", "Marcus Rashford"),
                "Mohamed Salah", "user2", "Bruno Fernandes", 0,
                LocalDateTime.now().minusHours(2), false, "COMPLETED", matchId2.toString(),
                Map.of("Mohamed Salah", 200, "Bruno Fernandes", 120, "Virgil van Dijk", 80, "Marcus Rashford", 60))
        );

        for (PlayerOfTheMatch game : playerGames) {
            PlayerOfTheMatchWrapper wrapper = PlayerOfTheMatchWrapper.of(game);
            indexDocument("player_games", game.id().toString(), wrapper);
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

    // Helper methods to generate IDs
    private String generateMatchId() {
        return "fb:m:" + matchIdGenerator.getAndIncrement();
    }

    private String generateTeamId() {
        return "fb:t:" + teamIdGenerator.getAndIncrement();
    }

    private String generateCompetitionId() {
        return "fb:c:" + competitionIdGenerator.getAndIncrement();
    }

    private Long generateGameInstanceId() {
        return gameInstanceIdGenerator.getAndIncrement();
    }

    private Long generateQuizId() {
        return quizIdGenerator.getAndIncrement();
    }

    private Long generatePlayerGameId() {
        return playerGameIdGenerator.getAndIncrement();
    }
}
