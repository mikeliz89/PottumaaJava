package Entity;

import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class MapObject {
	
	// tile stuff
	protected ArrayList<TileMap> tileMaps;
	protected double xmap;
	protected double ymap;
	
	// position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	// dimensions
	protected int width;
	protected int height;
	
	// collision box
	protected int collisionBoxWidth;
	protected int collisionBoxHeight;
	
	// collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	protected int bottomTile;
	protected int rightTile;
	protected int leftTile;
	protected int topTile;
	
	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	private boolean charging;
	
	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double originalMaxSpeed = maxSpeed;
	protected double stopSpeed;
	protected double originalStopSpeed = stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	protected double chargeSpeed;
	
	// constructor
	public MapObject() {
		tileMaps = new ArrayList<>();
	}
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(
				(int)x - collisionBoxWidth,
				(int)y - collisionBoxHeight,
				collisionBoxWidth,
				collisionBoxHeight
		);
	}
	
	public void calculateCorners(double x, double y, TileMap tileMap) {
		
		 leftTile = (int)(x - collisionBoxWidth / 2) / tileMap.getTileSize();
		 rightTile = (int)(x + collisionBoxWidth / 2 - 1) / tileMap.getTileSize();
		 topTile = (int)(y - collisionBoxHeight / 2) / tileMap.getTileSize();
		 bottomTile = (int)(y + collisionBoxHeight / 2 - 1) / tileMap.getTileSize();
		 
		int topleft = tileMap.getTileType(topTile, leftTile);
		int topright = tileMap.getTileType(topTile, rightTile);
		int bottomleft = tileMap.getTileType(bottomTile, leftTile);
		int bottomright = tileMap.getTileType(bottomTile, rightTile);
		
		topLeft = topleft == Tile.BLOCKED;
		topRight = topright == Tile.BLOCKED;
		bottomLeft = bottomleft == Tile.BLOCKED;
		bottomRight = bottomright == Tile.BLOCKED;
		
	}
	
	public void checkTileMapCollision(TileMap tm) {
		
		currCol = (int)x / tm.getTileSize();
		currRow = (int)y / tm.getTileSize();
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;

		for (TileMap tileMap : tileMaps) {
			calculateCorners(x, ydest, tileMap);
			if (dy < 0) {
				if (topLeft || topRight) {
					dy = 0;
					ytemp = currRow * tm.getTileSize() + collisionBoxHeight / 2;
				} else {
					ytemp += dy;
				}
			}
			if (dy > 0) {
				if (bottomLeft || bottomRight) {

					dy = 0;
					falling = false;
					ytemp = (currRow + 1) * tm.getTileSize() - collisionBoxHeight / 2;
				} else {
					ytemp += dy;
				}
			}

			calculateCorners(xdest, y, tileMap);
			if (dx < 0) {
				if (topLeft || bottomLeft) {
					dx = 0;
					xtemp = currCol * tm.getTileSize() + collisionBoxWidth / 2;
				} else {
					xtemp += dx;
				}
			}
			if (dx > 0) {
				if (topRight || bottomRight) {
					dx = 0;
					xtemp = (currCol + 1) * tm.getTileSize() - collisionBoxWidth / 2;
				} else {
					xtemp += dx;
				}
			}

			if (!falling) {
				calculateCorners(x, ydest + 1, tileMap);
				//if (!bottomLeft && !bottomRight) {
					//				falling = true;
				//}
			}
		}
		
	}
	
	public int getX() { return (int)x; }
	public int getY() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCollisionBoxWidth() { return collisionBoxWidth; }
	public int getCollisionbBoxHeight() { return collisionBoxHeight; }
	
	protected void checkCharging() {
		if(charging) {
			this.setStopSpeed(this.stopSpeed / 3);
			this.setMaxSpeed(this.maxSpeed + chargeSpeed);
		} 
		else {
			this.setMaxSpeed(this.originalMaxSpeed);
//			this.setStopSpeed(originalStopSpeed);
		}
	}

	protected boolean getBottomLeft() { return bottomLeft; }
	protected boolean getBottomRight() { return bottomRight; }
	
	public void setCharging(boolean b) {
		charging = b;
	}
	
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public void setStopSpeed(double stopSpeed) {
		this.stopSpeed = stopSpeed;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition() {

		for (TileMap tileMap : tileMaps) {
			xmap = tileMap.getx();
			ymap = tileMap.gety();
		}
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }
	
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
			x + xmap - width > GamePanel.WIDTH ||
			y + ymap + height < 0 ||
			y + ymap - height > GamePanel.HEIGHT;
	}
	
	public void draw(java.awt.Graphics2D g) {

		//todo: onko tämä turha tarkistus?
		if(!animation.hasFrames()) {
			return;
		}

		if(facingRight) {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2),
				null
			);
		}
		else {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2 + width),
				(int)(y + ymap - height / 2),
				-width,
				height,
				null
			);
		}
	}
	
}
















