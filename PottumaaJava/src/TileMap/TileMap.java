package TileMap;

import java.awt.*;
import java.awt.image.*;

import java.io.*;
import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap {
	
	// position
	private double x;
	private double y;
	
	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween;
	
	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	private int type;
	
	// tileset
	private BufferedImage tileset;
	private int numTilesX;
	private int numTilesY;
	private Tile[][] tiles;
	
	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public static final int GROUND = 0;
	public static final int OBSTACLE = 1;
	
	public TileMap(int tileSize) {
		
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}
	
	public void loadTiles(String s, boolean allBlocked) {
		
		try {

			tileset = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			numTilesX = tileset.getWidth() / tileSize;
			numTilesY = tileset.getHeight() / tileSize;
			
			tiles = new Tile[numTilesY][numTilesX];
			
			int count = 0;
			BufferedImage subimage;
			for(int row = 0; row < numTilesY; row++) {
				for(int col = 0; col < numTilesX; col++) {
					subimage = 
						tileset.getSubimage(
							col * tileSize,
							row * tileSize,
							tileSize,
							tileSize
						);
					int tileFrictionType = Tile.NORMAL;
					int type = Tile.NORMAL;
					
					if(allBlocked == true) {
						if(count != 0) {
							type = Tile.BLOCKED;
						}
					} 
					else {
						if(
							count == 32 ||
							count == 33 ||
							count == 34) {
							tileFrictionType = Tile.ICE;
						}
						if (count == 20 || count == 21 || count == 22 || count == 29 || count == 13) {
							type = Tile.BLOCKED;
						}
					}
					Tile newTile = new Tile(subimage, type, tileFrictionType);
					tiles[row][col] = newTile;
					
					count ++;
				}
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadMap(String s) {
		
		try {
			
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(
						new InputStreamReader(in)
					);
			
//			String delims = "\\t";
//			String delims = "\\s+";
			String delims = ";";
			String firstRowValue = br.readLine();
			String secondRowValue = br.readLine();
			
			numCols = Integer.parseInt(firstRowValue.substring(0, firstRowValue.indexOf(';'))); 
			numRows = Integer.parseInt(secondRowValue.substring(0, secondRowValue.indexOf(';')));
			
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims, -1);
				for(int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public double getx() { return x; }
	public double gety() { return y; }
	public double getTween() { return tween; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getTileSize() { return tileSize; }
	public int getType() { return this.type; }
	
	public BufferedImage getTileSet() { return tileset; }
	public Tile[][] getTiles() { return tiles; }
	
	public int getTileType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesX;
		int c = rc % numTilesX;
		return tiles[r][c].getType();
	}
	
	public int getTileFrictionType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesX;
		int c = rc % numTilesX;
		return tiles[r][c].getFrictionType();
	}
	
	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}
	
	public void setTween(double d) { tween = d; }
	
	public void setType(int type) { this.type = type; }
	
	public void setPosition(double x, double y) {
		
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixBounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
		
	}
	
	private void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	public void draw(Graphics2D g) {
		
		for(
			int row = rowOffset;
			row < rowOffset + numRowsToDraw;
			row++) {
			
			if(row >= numRows) break;
			
			for(
				int col = colOffset;
				col < colOffset + numColsToDraw;
				col++) {
				
				if(col >= numCols) break;
				
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesX;
				int c = rc % numTilesX;
				
				g.drawImage(
					tiles[r][c].getImage(),
					(int)x + col * tileSize,
					(int)y + row * tileSize,
					null
				);
				
			}
			
		}
		
	}
	
}



















