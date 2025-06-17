package org.acme.search.dto.predictor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The enum Game Status.
 */
public enum GameStatus {
  /**
   * the game has been manually canceled by a staff member.
   */
  CANCELED,
    /**
   * does not accept entries, matches are over, resolving is done.
   */
  SETTLED,
  /**
   * does not accept entries, matches are over, resolving is in progress.
   */
  CLOSED(SETTLED),
  /**
   * does not accept entries.
   */
  LIVE(CLOSED, SETTLED, CANCELED),
  /**
   * accepts entries.
   */
  OPEN(LIVE, CANCELED),
  /**
   * game is still not opened for users.
   */
  PENDING(OPEN, CANCELED);

  public static final GameStatus DEFAULT = OPEN;

  GameStatus() {
    this.next = new ArrayList<>();
  }

  GameStatus(GameStatus... next) {
    this.next = Arrays.asList(next);
  }

  private final List<GameStatus> next;

  public List<GameStatus> nextStatuses() {
    return next;
  }

  public boolean canUpdate(GameStatus status) {
    return next.contains(status);
  }

  public static List<GameStatus> commaSeparatedToList(String statuses) {
    return Arrays.stream(statuses.split(","))
        .map(strStatus -> GameStatus
            .valueOf(strStatus.toUpperCase().trim()))
        .collect(Collectors.toList());
  }

  public static class Documentation {
    public static final String CANCELED = "CANCELED";
    public static final String SETTLED = "SETTLED";
    public static final String CLOSED = "CLOSED";
    public static final String LIVE = "LIVE";
    public static final String OPEN = "OPEN";
    public static final String PENDING = "PENDING";

    private Documentation() {
    }
  }
}
