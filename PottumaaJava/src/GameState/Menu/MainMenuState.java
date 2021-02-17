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










