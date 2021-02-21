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
	}

	@Override
	protected void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.STATE_NEW_GAME);
		}
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.STATE_OPTIONS);
		}
		if(currentChoice == 2) {
			gsm.setState(GameStateManager.STATE_HELP);
		}
		if(currentChoice == 3) {
			gsm.setState(GameStateManager.STATE_MAP_EDITOR);
		}
		if(currentChoice == 4) {
			System.exit(0);
		}
	}
}










