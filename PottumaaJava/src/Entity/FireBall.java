package Entity;

import Entity.Player.PlayerSettings;
import TileMap.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class FireBall extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	
	public FireBall(ArrayList<TileMap> tileMaps, boolean right) {
		
		this.tileMaps = tileMaps;
		
		facingRight = right;
		
		moveSpeed = PlayerSettings.MAGIC_FIREBALL_MOVE_SPEED;

		if(right)
			setVector(getVectorX() + moveSpeed,
					getVectorY());
		else
			setVector(getVectorX() - moveSpeed,
					getVectorY());
		
		setWidth(30);
		setHeight(30);
		collisionBoxWidth = 14;
		collisionBoxHeight = 14;

		loadSprites();
	}

	private void loadSprites() {
		var width = getWidth();
		var height = getHeight();
		try {

			BufferedImage spriteSheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/fireball.gif"
				)
			);

			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}

			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spriteSheet.getSubimage(
					i * width,
					height,
					width,
					height
				);
			}

			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		if(right)
			stopGoingRight();
		else
			stopGoingLeft();
	}
	
	public boolean shouldRemove() { return remove; }

	@Override
	public void update() {

		super.update();
		
		if(getVectorX() == 0 && !hit) {
			setHit();
		}

		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}

	@Override
	protected void updatePosition() {

	}
	
}