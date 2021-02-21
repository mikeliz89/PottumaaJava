package GameState;

import Audio.AudioPlayer;
import Main.GameOptions;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameStateManager {
	GameState currentGameState;
	private int currentState;
	private int previousState;

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
		currentState = STATE_MAIN_MENU;
		bgMusic = new AudioPlayer(bgMusicFileName);
		loadState();
		playMusic();
	}
	
	private void loadState() {
		var gameStateFactory = new GameStateFactory();
		currentGameState = gameStateFactory.getGameState(currentState, previousState, this);
		if(currentGameState.backgroundMusicShouldChange)
			changeSong();
	}
	
	private void unloadState() {
		currentGameState = null;
	}
	
	public void setState(int state) {
		unloadState();
		previousState = currentState;
		currentState = state;
		loadState();
	}
	
	public void update() {
		try {
			if(currentGameState == null) return;
			
			currentGameState.update();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		try {
			if(currentGameState == null) return;
			
			currentGameState.draw(g);
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
		if(currentGameState == null) return;
		
		currentGameState.keyPressed(k);
		
		if(k == KeyEvent.VK_ESCAPE) setState(STATE_MAIN_MENU);

		if(k == KeyEvent.VK_D) GameOptions.ToggleDebugMode();
		if(k == KeyEvent.VK_T) {
			ToggleMusicOnOff();
		}
	}
	
	public void keyReleased(int k) {
		if(currentGameState == null) return;
		
        currentGameState.keyReleased(k);
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

	public int getCurrentState() {
		return currentState;
	}
	
}









