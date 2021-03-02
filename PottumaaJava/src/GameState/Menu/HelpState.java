package GameState.Menu;

import java.awt.*;
import GameState.*;

public class HelpState extends BaseMenuState {

	public HelpState(GameStateManager gsm) {
		super(gsm, new String[]{
				"Go back"
		}, "/Backgrounds/menubg.png");
		this.gsm = gsm;
		setTitleText("Help");
	}

	@Override
	protected void drawMenuOptions(Graphics2D g) {
		super.drawMenuOptions(g);

		var x = 175;
		g.setColor(Color.BLACK);
		g.drawString("Press D to toggle DebugMode", x, 200);
		g.drawString("Press T to toggle music on/off", x, 220);
		g.drawString("Press R to use claw attack", x, 240);
		g.drawString("Press F to shoot fireballs", x, 260);
		g.drawString("Press Left Shift to run", x, 280);
		g.drawString("Press I to open inventory", x, 300);
		g.drawString("Press M to open map", x, 320);
		g.drawString("Press Q to open quest log", x, 340);
		g.drawString("Press J to open dialog box", x, 360);
		g.drawString("Press F5 to save game", x, 380);
		g.drawString("Press Esc to exit to menu", x, 400);
	}

	protected void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.STATE_MAIN_MENU);
		}
	}

	@Override
	public void keyReleased(int k) {

	}
}












