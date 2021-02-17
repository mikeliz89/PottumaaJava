package GameState.Menu;

import GameState.*;
import Main.MathHelper;

import java.awt.*;
import java.awt.event.KeyEvent;

public class OptionsState extends BaseMenuState {

	public OptionsState(GameStateManager gsm) {
		super(gsm, new String[] {
				"Go back",
				//"Volume"
		}, "/Backgrounds/menubg.png");
		setTitleText("Options");
	}

	@Override
	public void keyPressed(int k) {
		super.keyPressed(k);
		if(k == KeyEvent.VK_F1) {
			gsm.decreaseVolume();
		}
		if(k == KeyEvent.VK_F2) {
			gsm.increaseVolume();
		}
	}

	protected void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		var musicValueRounded = MathHelper.roundFloat(gsm.getBackgroundMusicVolume(), 1);
		g.drawString("music volume: " + musicValueRounded, 175, 250);
	}
	
}












