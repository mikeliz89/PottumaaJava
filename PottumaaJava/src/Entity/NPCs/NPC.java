package Entity.NPCs;

import Entity.Obstacles.Obstacle;
import TileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;

public abstract class NPC extends Entity.Character {

	protected int damage;
	protected String name;
	protected String profession;

	private final int WALKING = 0;

	public NPC(ArrayList<TileMap> tileMaps, ArrayList<Obstacle> obstacles, int maxHealth, String spriteSheetName,
			   int width, int height) {

		super(tileMaps, obstacles, maxHealth, spriteSheetName, width, height);
		init();
	}

	private void init() {
		setAnimation();
	}

	protected void setAnimation() {
		animation.setFrames(sprites.get(WALKING));
		animation.setDelay(300);
	}

	public void draw(Graphics2D g) {

		super.draw(g);
		drawName(g);
	}

	private void drawName(Graphics2D g) {
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		g.setColor(Color.BLACK);
		g.drawString(name, (int)this.x-20 + (int) xMap, (int)this.y - 25 + (int) yMap);
		g.setColor(Color.CYAN);
		g.drawString(profession, (int)this.x-20 +(int) xMap, (int)this.y -10 + (int) yMap);
	}

	@Override
	protected void setCurrentAction() {

		var currentAction = getCurrentAction();
		if(currentAction != WALKING) {
			setCurrentAction(WALKING);
			animation.setFrames(sprites.get(WALKING));
			animation.setDelay(50);
		}
	}
}