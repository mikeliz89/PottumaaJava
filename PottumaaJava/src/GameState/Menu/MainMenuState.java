package GameState.Menu;

import GameState.*;

public class MainMenuState extends BaseMenuState {
	
	public MainMenuState(GameStateManager gsm) {
		super(gsm, new String[] {
				"Start",
				"Options",
				"Help",
				"MapEditor",
				"Quit"
		}, "/Backgrounds/menubg.png");
		setTitleText("Pottumaa");

		saveGame();
		loadGame();
	}

	private void saveGame() {
		SaveData data = new SaveData();
		data.name = "Miika";
		data.hp = 50;
		try {
			ResourceManager.save(data, "1.save");
			System.out.println("Save successful");
		} catch(Exception e) {
			System.out.println("Couldn't save: " + e.getMessage());
		}
	}

	private void loadGame() {
		try {
			SaveData data = (SaveData) ResourceManager.load("1.save");
			System.out.println("Loaded player. Name: " + data.name + " hp " + data.hp);
		} catch (Exception e) {
			System.out.println("Couldn't load save data: " + e.getMessage());
		}
	}

	@Override
	protected void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.OPTIONSSTATE);
		}
		if(currentChoice == 2) {
			gsm.setState(GameStateManager.HELPSTATE);
		}
		if(currentChoice == 3) {
			gsm.setState(GameStateManager.MAPEDITORSTATE);
		}
		if(currentChoice == 4) {
			System.exit(0);
		}
	}
}










