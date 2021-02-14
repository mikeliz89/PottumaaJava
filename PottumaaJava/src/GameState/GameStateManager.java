package GameState;

import Main.GameOptions;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;

	public static final int NUMGAMESTATES = 5;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int LEVEL2STATE = 2;
	public static final int HELPSTATE = 3;
	public static final int OPTIONSSTATE = 4;
	
	public GameStateManager() {
		
		gameStates = new GameState[NUMGAMESTATES];

		//Ensin menu
		currentState = MENUSTATE;
		
		loadState(currentState);
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE) {
			gameStates[state] = new MenuState(this);
		}
		if(state == LEVEL1STATE) {
			gameStates[state] = new Level1State(this);
		}
		if(state == LEVEL2STATE) { 
			gameStates[state] = new Level2State(this);
		}
		if(state == HELPSTATE) {
			gameStates[state] = new HelpState(this);
		}
		if(state == OPTIONSSTATE) {
			gameStates[state] = new OptionsState(this);
		}
	}
	
	private void unloadState(int state) {
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
		
		if(k == KeyEvent.VK_ESCAPE) System.exit(0);

		if(k == KeyEvent.VK_D) GameOptions.ToggleDebugMode();
	}
	
	public void keyReleased(int k) {
		if(gameStates[currentState] == null) return;
		
		gameStates[currentState].keyReleased(k);
	}
	
}









