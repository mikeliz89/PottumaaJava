package GameState;

import Main.GameOptions;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;

	public static final int NUMGAMESTATES = 6;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int LEVEL2STATE = 2;
	public static final int HELPSTATE = 3;
	public static final int OPTIONSSTATE = 4;
	public static final int MAPEDITORSTATE = 5;
	
	public GameStateManager() {
		
		gameStates = new GameState[NUMGAMESTATES];

		//Ensin menu
		currentState = GameOptions.FIRSTSTATE;
		
		loadState(currentState);
	}

	private void loadState(int state) {
		loadState(state, -1);
	}
	
	private void loadState(int state, int previousState) {

		switch (state) {
			case MENUSTATE -> {
				gameStates[state] = new MenuState(this);
			}
			case LEVEL1STATE -> {

				var playerStartingPointX = 0;
				var playerStartingPointY = 0;
				if(previousState == MENUSTATE) {
					playerStartingPointX = 40;
					playerStartingPointY = 100;
				}
				if(previousState == LEVEL2STATE) {
					playerStartingPointX = 880;
					playerStartingPointY = 585;
				}
				gameStates[state] = new Level1State(this, playerStartingPointX, playerStartingPointY);
			}
			case LEVEL2STATE -> gameStates[state] = new Level2State(this);
			case HELPSTATE -> gameStates[state] = new HelpState(this);
			case OPTIONSSTATE -> gameStates[state] = new OptionsState(this);
			case MAPEDITORSTATE -> gameStates[state] = new MapEditorState(this);
			default -> throw new IllegalStateException("Unexpected value: " + state);
		}
	}
	
	private void unloadState(int state) {
		gameStates[state].stopBackGroundMusic();
		gameStates[state] = null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		int previousState = currentState;
		currentState = state;
		loadState(currentState, previousState);
	}
	
	public void update() {
		try {
			if(gameStates[currentState] == null) return;
			
			gameStates[currentState].update();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		try {
			if(gameStates[currentState] == null) return;
			
			gameStates[currentState].draw(g);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void keyPressed(int k) {
		if(gameStates[currentState] == null) return;
		
		gameStates[currentState].keyPressed(k);
		
		if(k == KeyEvent.VK_ESCAPE) setState(MENUSTATE);

		if(k == KeyEvent.VK_D) GameOptions.ToggleDebugMode();
		if(k == KeyEvent.VK_T) GameOptions.ToggleMusicOnOff();
	}
	
	public void keyReleased(int k) {
		if(gameStates[currentState] == null) return;
		
		gameStates[currentState].keyReleased(k);
	}
	
}









