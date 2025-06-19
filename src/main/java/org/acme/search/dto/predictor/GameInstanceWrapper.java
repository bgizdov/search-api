package org.acme.search.dto.predictor;

import java.util.List;

/**
 * Wrapper DTO for GameInstance with additional search metadata
 */
public record GameInstanceWrapper(
    String id,
    String searchTitle,
    String searchDescription,
    List<String> tags,
    List<String> flags,
    List<String> entityIds,
    GameInstance data
) {

    /**
     * Create a wrapper with search metadata for a GameInstance
     */
    public static GameInstanceWrapper of(GameInstance gameInstance, String searchTitle, String searchDescription, 
                                        List<String> tags, List<String> flags, List<String> entityIds) {
        return new GameInstanceWrapper(
            gameInstance.id() != null ? gameInstance.id().toString() : null,
            searchTitle,
            searchDescription,
            tags,
            flags,
            entityIds,
            gameInstance
        );
    }

    /**
     * Create a simple wrapper with minimal metadata
     */
    public static GameInstanceWrapper of(GameInstance gameInstance) {
        String title = gameInstance.title() != null ? gameInstance.title() : "Game Instance";
        String description = gameInstance.description() != null ? gameInstance.description() : "Prediction game";
        
        return new GameInstanceWrapper(
            gameInstance.id() != null ? gameInstance.id().toString() : null,
            title,
            description,
            List.of("game", "prediction"),
            gameInstance.flags() != null ? gameInstance.flags() : List.of(),
            List.of(gameInstance.id() != null ? gameInstance.id().toString() : ""),
            gameInstance
        );
    }
}
