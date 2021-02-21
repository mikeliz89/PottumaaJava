package GameState;

import GameState.Levels.*;
import GameState.Menu.*;

public class GameStateFactory {

    public GameState getGameState(int state, int previousState, GameStateManager gameStateManager) {
        switch (state) {
            /* Menus */
            case GameStateManager.STATE_MAIN_MENU -> {
                return new MainMenuState(gameStateManager);
            }
            case GameStateManager.STATE_NEW_GAME -> {
                return new NewGameState(gameStateManager);
            }
            case GameStateManager.STATE_HELP -> {
                return new HelpState(gameStateManager);
            }
            case GameStateManager.STATE_OPTIONS -> {
                return new OptionsState(gameStateManager);
            }
            case GameStateManager.STATE_MAP_EDITOR -> {
                return new MapEditorState(gameStateManager);
            }
            /* Levels */
            case GameStateManager.STATE_LEVEL_1 -> {
                return new Level1State(gameStateManager, previousState);
            }
            case GameStateManager.STATE_LEVEL_2 -> {
                return new Level2State(gameStateManager, previousState);
            }
            case GameStateManager.STATE_PLAYER_HOME -> {
                return new PlayerHomeState(gameStateManager, previousState);
            }
            default -> throw new IllegalStateException("Unexpected value: " + state);
        }
    }
}
