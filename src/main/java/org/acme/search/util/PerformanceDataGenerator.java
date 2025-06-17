package org.acme.search.util;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.jboss.logging.Logger;

/**
 * Utility class for generating performance test data
 */
public class PerformanceDataGenerator {
    
    private static final Logger LOG = Logger.getLogger(PerformanceDataGenerator.class);
    
    private final RestClient restClient;
    
    public PerformanceDataGenerator(RestClient restClient) {
        this.restClient = restClient;
    }
    
    /**
     * Generate and insert bulk performance data
     */
    public void generatePerformanceData(int recordsPerType) throws Exception {
        LOG.infof("Generating performance data with %d records per type...", recordsPerType);
        
        // Insert matches
        LOG.infof("Inserting %d football matches...", recordsPerType);
        long matchStartTime = System.currentTimeMillis();
        insertBulkMatches(recordsPerType);
        long matchEndTime = System.currentTimeMillis();
        LOG.infof("✓ Matches inserted in: %d ms", matchEndTime - matchStartTime);
        
        // Insert predictions
        LOG.infof("Inserting %d predictions...", recordsPerType);
        long predictionStartTime = System.currentTimeMillis();
        insertBulkPredictions(recordsPerType);
        long predictionEndTime = System.currentTimeMillis();
        LOG.infof("✓ Predictions inserted in: %d ms", predictionEndTime - predictionStartTime);
        
        // Insert quiz games
        LOG.infof("Inserting %d quiz games...", recordsPerType);
        long quizStartTime = System.currentTimeMillis();
        insertBulkQuizGames(recordsPerType);
        long quizEndTime = System.currentTimeMillis();
        LOG.infof("✓ Quiz games inserted in: %d ms", quizEndTime - quizStartTime);
        
        // Insert player games
        LOG.infof("Inserting %d player games...", recordsPerType);
        long playerStartTime = System.currentTimeMillis();
        insertBulkPlayerGames(recordsPerType);
        long playerEndTime = System.currentTimeMillis();
        LOG.infof("✓ Player games inserted in: %d ms", playerEndTime - playerStartTime);
        
        // Refresh indices
        refreshIndices();
        
        LOG.info("Performance data generation completed successfully");
    }
    
    private void insertBulkMatches(int count) throws Exception {
        String[] teamNames = {"Barcelona", "Real Madrid", "Manchester United", "Liverpool", "Bayern Munich",
            "Borussia Dortmund", "PSG", "Manchester City", "Arsenal", "Chelsea", "Juventus", "AC Milan",
            "Inter Milan", "Atletico Madrid", "Valencia", "Sevilla", "Napoli", "Roma", "Lazio", "Atalanta"};
        String[] venues = {"Camp Nou", "Santiago Bernabeu", "Old Trafford", "Anfield", "Allianz Arena",
            "Signal Iduna Park", "Parc des Princes", "Etihad Stadium", "Emirates Stadium", "Stamford Bridge"};
        String[] competitionNames = {"La Liga", "Premier League", "Bundesliga", "Ligue 1", "Serie A", "Champions League"};
        String[] referees = {"Carlos del Cerro Grande", "Michael Oliver", "Felix Brych", "Daniele Orsato", "Clement Turpin"};

        StringBuilder bulkBody = new StringBuilder();
        int batchSize = 1000; // Process in batches of 1000

        for (int i = 0; i < count; i++) {
            String id = String.valueOf(1000000L + i); // Start from 1M to avoid conflicts
            String homeTeamName = teamNames[i % teamNames.length];
            String awayTeamName = teamNames[(i + 1) % teamNames.length];
            String venue = venues[i % venues.length];
            String competitionName = competitionNames[i % competitionNames.length];
            String referee = referees[i % referees.length];

            boolean isFinished = i % 4 != 3; // 75% finished, 25% scheduled
            long kickoffTime = System.currentTimeMillis() - (i % 7) * 24 * 60 * 60 * 1000L; // Spread over last week

            // Create index action
            bulkBody.append(String.format("{\"index\":{\"_index\":\"football_matches\",\"_id\":\"%s\"}}\n", id));

            // Create simplified document structure for performance
            String matchData = String.format(
                "{\"id\":\"%s\",\"kickoffAt\":%d,\"finishedAt\":%s,\"updatedAt\":%d," +
                "\"status\":{\"id\":%d,\"type\":\"%s\",\"name\":\"%s\",\"code\":\"%s\"}," +
                "\"homeTeam\":{\"id\":\"team-%d\",\"name\":\"%s\",\"shortName\":\"%s\"}," +
                "\"awayTeam\":{\"id\":\"team-%d\",\"name\":\"%s\",\"shortName\":\"%s\"}," +
                "\"competition\":{\"id\":\"comp-%d\",\"name\":\"%s\"}," +
                "\"goalsFullTimeHome\":%s,\"goalsFullTimeAway\":%s," +
                "\"goalsHalfTimeHome\":%s,\"goalsHalfTimeAway\":%s," +
                "\"venue\":\"%s\",\"referee\":\"%s\",\"lineupsConfirmed\":%s," +
                "\"startedAt\":%s,\"minute\":\"%s\",\"isDeleted\":false,\"undecided\":false}\n",
                id, kickoffTime,
                isFinished ? String.valueOf(kickoffTime + 90 * 60 * 1000) : "null",
                System.currentTimeMillis(),
                isFinished ? 1 : 2, isFinished ? "finished" : "scheduled",
                isFinished ? "Finished" : "Scheduled", isFinished ? "FT" : "NS",
                i % teamNames.length, homeTeamName, homeTeamName.split(" ")[0],
                (i + 1) % teamNames.length, awayTeamName, awayTeamName.split(" ")[0],
                i % competitionNames.length, competitionName,
                isFinished ? String.valueOf(i % 5) : "null",
                isFinished ? String.valueOf((i + 1) % 5) : "null",
                isFinished ? String.valueOf(i % 3) : "null",
                isFinished ? String.valueOf((i + 1) % 3) : "null",
                venue, referee, isFinished ? "true" : "false",
                isFinished ? String.valueOf(kickoffTime) : "null",
                isFinished ? "90" : "null"
            );
            bulkBody.append(matchData);

            // Send batch when we reach batch size or at the end
            if ((i + 1) % batchSize == 0 || i == count - 1) {
                sendBulkRequest(bulkBody.toString());
                bulkBody.setLength(0); // Clear the buffer

                if ((i + 1) % 10000 == 0) {
                    LOG.infof("  Processed %d/%d matches...", i + 1, count);
                }
            }
        }
    }
    
    private void insertBulkPredictions(int count) throws Exception {
        String[] userIds = {"user1", "user2", "user3", "user4", "user5", "user6", "user7", "user8", "user9", "user10"};
        String[] outcomes = {"HOME_WIN", "AWAY_WIN", "DRAW"};

        StringBuilder bulkBody = new StringBuilder();
        int batchSize = 1000;

        for (int i = 0; i < count; i++) {
            long id = 2000000L + i; // Start from 2M
            long matchId = 1000000L + (i % count); // Reference match IDs
            String userId = userIds[i % userIds.length];
            String outcome = outcomes[i % outcomes.length];

            bulkBody.append(String.format("{\"index\":{\"_index\":\"predictions\",\"_id\":\"%d\"}}\n", id));

            String predictionData = String.format("{\"id\":%d,\"matchId\":%d,\"userId\":\"%s\",\"predictedHomeScore\":%d,\"predictedAwayScore\":%d,\"predictedOutcome\":\"%s\",\"predictionTime\":\"2024-01-15T19:00:00\",\"confidence\":%d,\"isCorrect\":%s}\n",
                id, matchId, userId, i % 4, (i + 1) % 4, outcome, 50 + (i % 50), (i % 2 == 0));
            bulkBody.append(predictionData);

            if ((i + 1) % batchSize == 0 || i == count - 1) {
                sendBulkRequest(bulkBody.toString());
                bulkBody.setLength(0);

                if ((i + 1) % 10000 == 0) {
                    LOG.infof("  Processed %d/%d predictions...", i + 1, count);
                }
            }
        }
    }

    private void insertBulkQuizGames(int count) throws Exception {
        String[] titles = {"Football Trivia", "Premier League Quiz", "Champions League Facts", "World Cup History",
            "La Liga Knowledge", "Bundesliga Quiz", "Serie A Test", "Ligue 1 Facts", "European Football",
            "International Football", "Club History", "Player Stats", "Manager Quiz", "Stadium Facts"};
        String[] categories = {"Sports", "Football", "History", "Statistics", "Trivia"};
        String[] creators = {"admin", "quiz_master", "football_expert", "trivia_king", "sports_guru"};

        StringBuilder bulkBody = new StringBuilder();
        int batchSize = 1000;

        for (int i = 0; i < count; i++) {
            long id = 3000000L + i; // Start from 3M
            String title = titles[i % titles.length] + " " + (i + 1);
            String category = categories[i % categories.length];
            String creator = creators[i % creators.length];

            bulkBody.append(String.format("{\"index\":{\"_index\":\"quiz_games\",\"_id\":\"%d\"}}\n", id));

            String quizData = String.format("{\"id\":%d,\"title\":\"%s\",\"description\":\"Test your football knowledge with this quiz\",\"questions\":[\"Question 1?\",\"Question 2?\"],\"correctAnswers\":[\"Answer 1\",\"Answer 2\"],\"category\":\"%s\",\"difficulty\":%d,\"timeLimit\":%d,\"createdAt\":\"2024-01-15T10:00:00\",\"createdBy\":\"%s\",\"isActive\":true}\n",
                id, title, category, 1 + (i % 5), 300 + (i % 300), creator);
            bulkBody.append(quizData);

            if ((i + 1) % batchSize == 0 || i == count - 1) {
                sendBulkRequest(bulkBody.toString());
                bulkBody.setLength(0);

                if ((i + 1) % 10000 == 0) {
                    LOG.infof("  Processed %d/%d quiz games...", i + 1, count);
                }
            }
        }
    }

    private void insertBulkPlayerGames(int count) throws Exception {
        String[] players = {"Lionel Messi", "Cristiano Ronaldo", "Kylian Mbappe", "Erling Haaland", "Neymar Jr",
            "Kevin De Bruyne", "Mohamed Salah", "Robert Lewandowski", "Karim Benzema", "Luka Modric",
            "Virgil van Dijk", "Sadio Mane", "Bruno Fernandes", "Harry Kane", "Son Heung-min"};
        String[] userIds = {"user1", "user2", "user3", "user4", "user5", "user6", "user7", "user8", "user9", "user10"};
        String[] statuses = {"COMPLETED", "ACTIVE", "EXPIRED"};

        StringBuilder bulkBody = new StringBuilder();
        int batchSize = 1000;

        for (int i = 0; i < count; i++) {
            long id = 4000000L + i; // Start from 4M
            long matchId = 1000000L + (i % count); // Reference match IDs
            String correctPlayer = players[i % players.length];
            String selectedPlayer = players[(i + 1) % players.length];
            String userId = userIds[i % userIds.length];
            String status = statuses[i % statuses.length];

            bulkBody.append(String.format("{\"index\":{\"_index\":\"player_games\",\"_id\":\"%d\"}}\n", id));

            String playerData = String.format("{\"id\":%d,\"matchId\":%d,\"gameTitle\":\"Player of the Match Game %d\",\"playerOptions\":[\"%s\",\"%s\",\"%s\",\"%s\"],\"correctPlayer\":\"%s\",\"userId\":\"%s\",\"selectedPlayer\":\"%s\",\"points\":%d,\"submissionTime\":\"2024-01-15T21:00:00\",\"isCorrect\":%s,\"gameStatus\":\"%s\"}\n",
                id, matchId, i + 1,
                players[i % players.length], players[(i + 1) % players.length],
                players[(i + 2) % players.length], players[(i + 3) % players.length],
                correctPlayer, userId, selectedPlayer, i % 20,
                correctPlayer.equals(selectedPlayer), status);
            bulkBody.append(playerData);

            if ((i + 1) % batchSize == 0 || i == count - 1) {
                sendBulkRequest(bulkBody.toString());
                bulkBody.setLength(0);

                if ((i + 1) % 10000 == 0) {
                    LOG.infof("  Processed %d/%d player games...", i + 1, count);
                }
            }
        }
    }

    private void sendBulkRequest(String bulkBody) throws Exception {
        Request request = new Request("POST", "/_bulk");
        request.setJsonEntity(bulkBody);
        request.addParameter("refresh", "false"); // Don't refresh after each batch

        var response = restClient.performRequest(request);

        // Check for errors in the response
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Bulk request failed with status: " + response.getStatusLine().getStatusCode());
        }
    }

    private void refreshIndices() throws Exception {
        LOG.info("Refreshing Elasticsearch indices...");

        Request request = new Request("POST", "/_refresh");
        var response = restClient.performRequest(request);

        if (response.getStatusLine().getStatusCode() != 200) {
            LOG.warn("Failed to refresh indices: " + response.getStatusLine().getStatusCode());
        } else {
            LOG.info("Indices refreshed successfully");
        }

        // Wait a bit more for indexing to complete
        Thread.sleep(5000);
    }
}
