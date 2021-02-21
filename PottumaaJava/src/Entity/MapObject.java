package Entity;

import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

import java.awt.*;
import java.util.ArrayList;

public abstract class MapObject {
	
	// tile stuff
	protected ArrayList<TileMap> tileMaps;
	protected double xMap;
	protected double yMap;
	
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
	protected int currentRow;
	protected int currentColumn;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;

	//tiles
	protected int bottomTile;
	protected int rightTile;
	protected int leftTile;
	protected int topTile;

	// animation
	protected Animation animation;
	protected boolean facingRight;
	protected boolean facingUp;
	
	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;

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
		
		topLeft = topleft == Tile.TILE_TYPE_BLOCKED;
		topRight = topright == Tile.TILE_TYPE_BLOCKED;
		bottomLeft = bottomleft == Tile.TILE_TYPE_BLOCKED;
		bottomRight = bottomright == Tile.TILE_TYPE_BLOCKED;
		
	}
	
	public void checkTileMapCollision(TileMap tm) {
		
		currentColumn = (int)x / tm.getTileSize();
		currentRow = (int)y / tm.getTileSize();
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;

		for (TileMap tileMap : tileMaps) {
			calculateCorners(x, ydest, tileMap);
			if (dy < 0) {
				if (topLeft || topRight) {
					dy = 0;
					ytemp = currentRow * tm.getTileSize() + collisionBoxHeight / 2;
				} else {
					ytemp += dy;
				}
			}
			if (dy > 0) {
				if (bottomLeft || bottomRight) {

					dy = 0;
					falling = false;
					ytemp = (currentRow + 1) * tm.getTileSize() - collisionBoxHeight / 2;
				} else {
					ytemp += dy;
				}
			}

			calculateCorners(xdest, y, tileMap);
			if (dx < 0) {
				if (topLeft || bottomLeft) {
					dx = 0;
					xtemp = currentColumn * tm.getTileSize() + collisionBoxWidth / 2;
				} else {
					xtemp += dx;
				}
			}
			if (dx > 0) {
				if (topRight || bottomRight) {
					dx = 0;
					xtemp = (currentColumn + 1) * tm.getTileSize() - collisionBoxWidth / 2;
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
	public int getCollisionBoxHeight() { return collisionBoxHeight; }

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public double getStopSpeed() {
		return stopSpeed;
	}

	protected boolean getBottomLeft() { return bottomLeft; }
	protected boolean getBottomRight() { return bottomRight; }
	
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
			xMap = tileMap.getX();
			yMap = tileMap.getY();
		}
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }
	
	public boolean notOnScreen() {
		return x + xMap + width < 0 ||
			x + xMap - width > GamePanel.WIDTH ||
			y + yMap + height < 0 ||
			y + yMap - height > GamePanel.HEIGHT;
	}

	public double getXMap() {
		return xMap;
	}

	public double getYMap() {
		return yMap;
	}

	public void update() {
		animation.update();
	}
	
	public void draw(Graphics2D g) {

		setMapPosition();

		if(notOnScreen()) return;

		//todo: onko tämä turha tarkistus?
		if(!animation.hasFrames()) {
			return;
		}

		if(facingRight) {
			g.drawImage(
				animation.getImage(),
				(int)(x + xMap - width / 2),
				(int)(y + yMap - height / 2),
				null
			);
		}
		else {
			//todo: tämä tarkoitti ennen vasemmalle menoa, mutta nykyään tarkoittaa vasemalle, ylös ja alas menoa.
			//todo: koodaa else iffit eri suuntiin menoille.
			g.drawImage(
				animation.getImage(),
				(int)(x + xMap - width / 2 + width),
				(int)(y + yMap - height / 2),
				-width,
				height,
				null
			);
		}
	}
	
}
















