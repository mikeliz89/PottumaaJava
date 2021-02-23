package Entity;

import Entity.Obstacles.Obstacle;
import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

import java.awt.*;
import java.util.ArrayList;

public abstract class MapObject {
	
	// tile stuff
	protected ArrayList<TileMap> tileMaps;
	protected ArrayList<Obstacle> obstacles;
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
	protected double xTemp;
	protected double yTemp;
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
		obstacles = new ArrayList<>();
	}
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getCollisionBoxRectangle();
		Rectangle r2 = o.getCollisionBoxRectangle();
		return r1.intersects(r2);
	}
	
	public Rectangle getCollisionBoxRectangle() {
		return new Rectangle(
				(int)x - collisionBoxWidth,
				(int)y - collisionBoxHeight,
				collisionBoxWidth,
				collisionBoxHeight
		);
	}

	public Rectangle getRectangle() {
		return new Rectangle(
				(int)(x + xMap - width / 2),
				(int)(y + yMap - height / 2),
				width,
				height);
	}
	
	public void calculateCorners(double x, double y, TileMap tileMap) {
		
		 leftTile = (int)(x - collisionBoxWidth / 2) / tileMap.getTileSize();
		 rightTile = (int)(x + collisionBoxWidth / 2 - 1) / tileMap.getTileSize();
		 topTile = (int)(y - collisionBoxHeight / 2) / tileMap.getTileSize();
		 bottomTile = (int)(y + collisionBoxHeight / 2 - 1) / tileMap.getTileSize();
		 
		int topLeftTileType = tileMap.getTileType(topTile, leftTile);
		int topRightTileType = tileMap.getTileType(topTile, rightTile);
		int bottomLeftTileType = tileMap.getTileType(bottomTile, leftTile);
		int bottomRightTileType = tileMap.getTileType(bottomTile, rightTile);
		
		topLeft = topLeftTileType == Tile.TILE_TYPE_OBSTACLE;
		topRight = topRightTileType == Tile.TILE_TYPE_OBSTACLE;
		bottomLeft = bottomLeftTileType == Tile.TILE_TYPE_OBSTACLE;
		bottomRight = bottomRightTileType == Tile.TILE_TYPE_OBSTACLE;
		
	}

	public void checkObstacleCollision(Obstacle obstacle) {
		//todo: Koodaa varsinainen collision detection
	}
	
	public void checkTileMapCollision(TileMap tm) {
		
		currentColumn = (int)x / tm.getTileSize();
		currentRow = (int)y / tm.getTileSize();

		double xDestination = x + dx;
		double yDestination = y + dy;
		
		xTemp = x;
		yTemp = y;

		for (TileMap tileMap : tileMaps) {
			calculateCorners(x, yDestination, tileMap);
			if (dy < 0) {
				if (topLeft || topRight) {
					dy = 0;
					yTemp = currentRow * tm.getTileSize() + collisionBoxHeight / 2;
				} else {
					yTemp += dy;
				}
			}
			if (dy > 0) {
				if (bottomLeft || bottomRight) {

					dy = 0;
					yTemp = (currentRow + 1) * tm.getTileSize() - collisionBoxHeight / 2;
				} else {
					yTemp += dy;
				}
			}

			calculateCorners(xDestination, y, tileMap);
			if (dx < 0) {
				if (topLeft || bottomLeft) {
					dx = 0;
					xTemp = currentColumn * tm.getTileSize() + collisionBoxWidth / 2;
				} else {
					xTemp += dx;
				}
			}
			if (dx > 0) {
				if (topRight || bottomRight) {
					dx = 0;
					xTemp = (currentColumn + 1) * tm.getTileSize() - collisionBoxWidth / 2;
				} else {
					xTemp += dx;
				}
			}

			calculateCorners(x, yDestination + 1, tileMap);

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
















