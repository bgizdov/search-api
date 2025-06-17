package org.acme.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.acme.search.dto.football.Match;
import org.acme.search.dto.PlayerOfTheMatchGame;
import org.acme.search.dto.predictor.GameInstance;
import org.acme.search.dto.classicquiz.ClassicQuizPublicDto;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service to initialize sample data in Elasticsearch on startup
 */
@ApplicationScoped
public class DataInitializationService {

    private static final Logger LOG = Logger.getLogger(DataInitializationService.class);

    @Inject
    RestClient restClient;

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
                LOG.info("Initializing sample data in Elasticsearch...");
                initializeSampleData();
                LOG.info("Sample data initialization completed.");
            } catch (Exception e) {
                LOG.warn("Failed to initialize sample data: " + e.getMessage());
                LOG.info("Application will continue without sample data. Elasticsearch might not be available.");
            }
        }).start();
    }

    private void initializeSampleData() throws Exception {
        // Create sample football matches
        createSampleMatches();
        
        // Create sample predictions
        createSamplePredictions();
        
        // Create sample quiz games
        createSampleQuizGames();
        
        // Create sample player games
        createSamplePlayerGames();
    }

    private void createSampleMatches() throws Exception {
        List<Match> matches = List.of(
            new Match(1L, "Barcelona", "Real Madrid", 2, 1,
                LocalDateTime.now().minusDays(1), "Camp Nou", "La Liga", "FINISHED"),
            new Match(2L, "Manchester United", "Liverpool", 1, 3,
                LocalDateTime.now().minusDays(2), "Old Trafford", "Premier League", "FINISHED"),
            new Match(3L, "Bayern Munich", "Borussia Dortmund", null, null,
                LocalDateTime.now().plusDays(1), "Allianz Arena", "Bundesliga", "SCHEDULED")
        );

        for (Match match : matches) {
            indexDocument("football_matches", match.id().toString(), match);
        }
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
        List<PlayerOfTheMatchGame> playerGames = List.of(
            new PlayerOfTheMatchGame(1L, 1L, "El Clasico Player of the Match", 
                List.of("Lionel Messi", "Karim Benzema", "Pedri", "Vinicius Jr."),
                "Lionel Messi", "user1", "Lionel Messi", 10, 
                LocalDateTime.now().minusHours(1), true, "COMPLETED"),
            new PlayerOfTheMatchGame(2L, 2L, "Premier League POTM", 
                List.of("Mohamed Salah", "Bruno Fernandes", "Virgil van Dijk", "Marcus Rashford"),
                "Mohamed Salah", "user2", "Bruno Fernandes", 0, 
                LocalDateTime.now().minusHours(2), false, "COMPLETED")
        );

        for (PlayerOfTheMatchGame game : playerGames) {
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
}
