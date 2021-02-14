package GameState;

import Audio.AudioPlayer;
import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {
	
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {
		"Start",
		"Options",
		"Help",
		"MapEditor",
		"Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
//	private AudioPlayer bgMusic;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
//		bgMusic = new AudioPlayer("/Music/menu-joesatriani.mp3");
//		bgMusic.play();
		try {
			//Background image
			bg = new Background("/Backgrounds/menubg.png", 0.1);
			bg.setVector(-0.1, 0);

			titleColor = new Color(220, 220, 220);
			titleFont = new Font(
					"Century Gothic",
					Font.PLAIN,
					28);
			font = new Font("Arial", Font.PLAIN, 12);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {

		DrawBackground(g);

		DrawTitle(g);

		DrawMenuOptions(g);
	}

	private void DrawBackground(Graphics2D g) {
		bg.draw(g);
	}

	private void DrawTitle(Graphics2D g) {
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Pottumaa", GamePanel.WIDTH / 3, GamePanel.HEIGHT / 3);
	}

	private void DrawMenuOptions(Graphics2D g) {
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.WHITE);
			}
			else {
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 175, 140 + i * 15);
		}
	}

	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
//			bgMusic.stop();
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
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {}
	
}










