package GameState;

import Audio.AudioPlayer;
import GameState.Levels.Level1State;
import GameState.Levels.Level2State;
import GameState.Levels.PlayerHomeState;
import GameState.Menu.HelpState;
import GameState.Menu.MainMenuState;
import GameState.Menu.OptionsState;
import Main.GameOptions;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	public static final int NUMGAMESTATES = 7;
	//States
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int LEVEL2STATE = 2;
	public static final int HELPSTATE = 3;
	public static final int OPTIONSSTATE = 4;
	public static final int MAPEDITORSTATE = 5;
	//public static final int LEVEL3STATE = 6;
	public static final int INSIDEHOUSE = 6;

	private AudioPlayer bgMusic;
	//https://www.bensound.com/royalty-free-music/track/ukulele
	private String bgMusicFileName = "/Music/happymusic.wav";
	private String songToPlay;
	
	public GameStateManager() {
		
		gameStates = new GameState[NUMGAMESTATES];

		//Ensin menu
		currentState = GameOptions.FIRSTSTATE;
		bgMusic = new AudioPlayer(bgMusicFileName);
		playMusic();
		
		loadState(currentState);
	}

	private void loadState(int state) {
		loadState(state, -1);
	}
	
	private void loadState(int state, int previousState) {

		switch (state) {
			case MENUSTATE -> {
				gameStates[state] = new MainMenuState(this);
				changeSong(songToPlay);
			}
			case LEVEL1STATE -> {

				var playerStartingPointX = 0;
				var playerStartingPointY = 0;
				//Quick and dirty
				if(previousState == MENUSTATE ||
						previousState == LEVEL1STATE) {
					playerStartingPointX = 40;
					playerStartingPointY = 100;
				}
				if(previousState == LEVEL2STATE) {
					playerStartingPointX = 880;
					playerStartingPointY = 585;
				}
				if(previousState == INSIDEHOUSE) {
					playerStartingPointX = 345;
					playerStartingPointY = 200;
				}
				gameStates[state] = new Level1State(this, playerStartingPointX, playerStartingPointY);
				changeSong(songToPlay);
			}
			case LEVEL2STATE -> gameStates[state] = new Level2State(this, 80, 575);
			case HELPSTATE -> gameStates[state] = new HelpState(this);
			case OPTIONSSTATE -> gameStates[state] = new OptionsState(this);
			case MAPEDITORSTATE -> gameStates[state] = new MapEditorState(this);
			//case LEVEL3STATE -> gameStates[state] = new Level3State(this);
			case INSIDEHOUSE -> gameStates[state] = new PlayerHomeState( this, 300, 300);
			default -> throw new IllegalStateException("Unexpected value: " + state);
		}
	}
	
	private void unloadState(int state) {
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
		if(k == KeyEvent.VK_T) {
			ToggleMusicOnOff();
		}
	}
	
	public void keyReleased(int k) {
		if(gameStates[currentState] == null) return;
		
		gameStates[currentState].keyReleased(k);
	}

	private void ToggleMusicOnOff() {
		GameOptions.ToggleMusicOnOff();

		if(GameOptions.IS_PLAY_MUSIC_ON) {
			playMusic();
		} else {
			stopMusic();
		}
	}
	public void setSongToPlay(String songToPlay) {
		this.songToPlay = songToPlay;
	}

	private void changeSong(String songToPlay) {
		stopMusic();
		createNewAudioPlayer(songToPlay);
		playMusic();
	}

	private void createNewAudioPlayer(String songToPlay) {
		bgMusic = null;
		bgMusic = new AudioPlayer(songToPlay);
	}

	private void playMusic() {
		if(!GameOptions.IS_PLAY_MUSIC_ON)
			return;
		bgMusic.play();
	}

	private void stopMusic() {
		if(bgMusic == null)
			return;
		bgMusic.stop();
	}

	private final float volumeStep = 0.1f;

	public void increaseVolume() {
		if(bgMusic == null)
			return;
		var currentVolume = bgMusic.getVolume();
		bgMusic.setVolume(currentVolume + volumeStep);
	}

	public void decreaseVolume() {
		if(bgMusic == null)
			return;
		var currentVolume = bgMusic.getVolume();
		bgMusic.setVolume(currentVolume - volumeStep);
	}

	public float getBackgroundMusicVolume() {
		return bgMusic.getVolume();
	}
	
}









