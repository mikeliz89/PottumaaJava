package GameState;

import Audio.AudioPlayer;
import Main.GameOptions;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	private int previousState;
	public static final int NUMBER_OF_GAMESTATES = 8;

	//States
	public static final int STATE_MAIN_MENU = 0;
	public static final int STATE_LEVEL_1 = 1;
	public static final int STATE_LEVEL_2 = 2;
	public static final int STATE_HELP = 3;
	public static final int STATE_OPTIONS = 4;
	public static final int STATE_MAP_EDITOR = 5;
	public static final int STATE_PLAYER_HOME = 6;
	public static final int STATE_NEW_GAME = 7;

	private AudioPlayer bgMusic;
	//https://www.bensound.com/royalty-free-music/track/ukulele
	private String bgMusicFileName = "/Music/happymusic.wav";
	private String songToPlay;
	
	public GameStateManager() {
		gameStates = new GameState[NUMBER_OF_GAMESTATES];
		currentState = STATE_MAIN_MENU;
		bgMusic = new AudioPlayer(bgMusicFileName);
		loadState();
		playMusic();
	}
	
	private void loadState() {
		var gameStateFactory = new GameStateFactory();
		var gameState = gameStateFactory.getGameState(currentState, previousState, this);
		gameStates[currentState] = gameState;

		if(gameStates[currentState].backgroundMusicShouldChange)
			changeSong();
	}
	
	private void unloadState() {
		gameStates[currentState] = null;
	}
	
	public void setState(int state) {
		unloadState();
		previousState = currentState;
		currentState = state;
		loadState();
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

		//Jos tarvii piirtää kaikkien ruutujen päälle jotain, se onnistuu tässä
		/*
		Font font1 = new Font("Courier", Font.BOLD, 24);
		FontRenderContext frc = g.getFontRenderContext();
		TextLayout tl = new TextLayout("test", font1, frc);
		g.setColor(Color.GRAY);
		tl.draw(g, 70, 150);
		*/
	}

	KeyEvent lastKeyEvent;
	public void keyEventHappens(KeyEvent k) {
		lastKeyEvent = k;
	}

	public KeyEvent getLastKeyEvent() {
		return lastKeyEvent;
	}
	
	public void keyPressed(int k) {
		if(gameStates[currentState] == null) return;
		
		gameStates[currentState].keyPressed(k);
		
		if(k == KeyEvent.VK_ESCAPE) setState(STATE_MAIN_MENU);

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

	private void changeSong() {
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









