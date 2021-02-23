package GameState.Menu;

import GameState.*;

public class MainMenuState extends BaseMenuState {

	int loadedLevel;
	
	public MainMenuState(GameStateManager gsm) {
		super(gsm, new String[] {
				"Start",
				"Load Game",
				"Options",
				"Help",
				"MapEditor",
				"Quit"
		}, "/Backgrounds/menubg.png");
		setTitleText("Pottumaa");
	}

	@Override
	protected void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.STATE_NEW_GAME);
		}
		if(currentChoice == 1 ) {
			loadGame();
			gsm.setState(loadedLevel);
		}
		if(currentChoice == 2) {
			gsm.setState(GameStateManager.STATE_OPTIONS);
		}
		if(currentChoice == 3) {
			gsm.setState(GameStateManager.STATE_HELP);
		}
		if(currentChoice == 4) {
			gsm.setState(GameStateManager.STATE_MAP_EDITOR);
		}
		if(currentChoice == 5) {
			System.exit(0);
		}
	}

	private void loadGame() {
		try {
			SaveData data = (SaveData) ResourceManager.load("1.save");
			loadedLevel = data.level;
			System.out.println("Loaded player. Name: " + data.name + " health " + data.health);
		} catch (Exception e) {
			System.out.println("Couldn't load save data: " + e.getMessage());
		}
	}

}










