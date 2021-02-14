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
		switch (state) {
			case MENUSTATE -> gameStates[state] = new MenuState(this);
			case LEVEL1STATE -> gameStates[state] = new Level1State(this);
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
		currentState = state;
		loadState(currentState);
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
	}
	
	public void keyReleased(int k) {
		if(gameStates[currentState] == null) return;
		
		gameStates[currentState].keyReleased(k);
	}
	
}









