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
	
	public TileMap(int tileSize) {
		
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}
	
	public void loadTiles(String s) {
		
		try {

			tileset = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			numTilesX = tileset.getWidth() / tileSize;
			numTilesY = tileset.getHeight() / tileSize;
			
//			System.out.println(numTilesX);
//			System.out.println(numTilesY);
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
					if(
						count == 32 ||
						count == 33 ||
						count == 34) {
						tileFrictionType = Tile.ICE;
					}
					if (count == 21) {
						type = Tile.BLOCKED;
					}
					Tile newTile = new Tile(subimage, type, tileFrictionType);
					tiles[row][col] = newTile;
					System.out.println(newTile);
					System.out.println(count);
					
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
//			System.out.println(numCols);
//			System.out.println(numRows);
			
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
	
	public int getTileSize() { return tileSize; }
	public double getx() { return x; }
	public double gety() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public BufferedImage getTileSet() { return tileset; }
	public Tile[][] getTiles() { return tiles; }
	
	public int getType(int row, int col) {
//		System.out.println("row " + row);
//		System.out.println("col " + col);
		int rc = map[row][col];
		int r = rc / numTilesX;
		int c = rc % numTilesX;
		return tiles[r][c].getType();
	}
	
	public int getFrictionType(int row, int col) {
//		System.out.println("row " + row);
//		System.out.println("col " + col);
		int rc = map[row][col];
		int r = rc / numTilesX;
		int c = rc % numTilesX;
		return tiles[r][c].getFrictionType();
	}
	
	public void setTween(double d) { tween = d; }
	
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



















