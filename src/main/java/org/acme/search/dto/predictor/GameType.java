package org.acme.search.dto.predictor;

/**
 * The enum Game Type.
 */
public enum GameType {
  /**
   * Top X Game.
   */
  TOP_X,
  /**
   * Match quiz game type.
   */
  MATCH_QUIZ,
  /**
   * Single prediction without game.
   */
  SINGLE,
  /**
   * Football Fantasy type of prediction. Uses template id instead of a game id.
   */
  FANTASY,
  /**
   * Custom bracket game type.
   */
  BRACKET,
  STANDING,
  EVENT;
}
