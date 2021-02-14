package Entity;

import Entity.Player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class HUD {
	
	private Player player;
	
	private BufferedImage image;
	private Font font;
	
	public HUD(Player p) {
		player = p;
		try {
			image = ImageIO.read(
				getClass().getResourceAsStream(
					"/HUD/hud.gif"
				)
			);
			font = new Font("Arial", Font.PLAIN, 14);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		
		g.drawImage(image, 0, 10, null);
		g.setFont(font);
		g.setColor(Color.WHITE);
		DrawHealthPoints(g);
		DrawManaPoints(g);
	}

	private void DrawManaPoints(Graphics2D g) {
		g.drawString(
			player.getFire() / 100 + "/" + player.getMaxFire() / 100,
			30,
			45
		);
	}

	private void DrawHealthPoints(Graphics2D g) {
		g.drawString(
			player.getHealth() + "/" + player.getMaxHealth(),
			30,
			25
		);
	}

}













