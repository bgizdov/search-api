package org.acme.search.dto.potm;

import java.util.List;

/**
 * Wrapper DTO for PlayerOfTheMatch with additional search metadata
 */
public record PlayerOfTheMatchWrapper(
    String id,
    String searchTitle,
    String searchDescription,
    List<String> tags,
    List<String> flags,
    List<String> entityIds,
    PlayerOfTheMatch data
) {

    /**
     * Create a wrapper with search metadata for a PlayerOfTheMatch
     */
    public static PlayerOfTheMatchWrapper of(PlayerOfTheMatch potm, String searchTitle, String searchDescription, 
                                            List<String> tags, List<String> flags, List<String> entityIds) {
        return new PlayerOfTheMatchWrapper(
            potm.id() != null ? potm.id().toString() : null,
            searchTitle,
            searchDescription,
            tags,
            flags,
            entityIds,
            potm
        );
    }

    /**
     * Create a simple wrapper with minimal metadata
     */
    public static PlayerOfTheMatchWrapper of(PlayerOfTheMatch potm) {
        String title = potm.gameTitle() != null ? potm.gameTitle() : "Player of the Match";
        String description = String.format("Player of the match game with %d options", 
            potm.playerOptions() != null ? potm.playerOptions().size() : 0);
        
        return new PlayerOfTheMatchWrapper(
            potm.id() != null ? potm.id().toString() : null,
            title,
            description,
            List.of("player", "match", "game"),
            List.of(),
            List.of(
                potm.id() != null ? potm.id().toString() : "",
                potm.matchId() != null ? potm.matchId().toString() : ""
            ),
            potm
        );
    }
}
