package Entity.HUD;

import Audio.AudioPlayer;
import Entity.Enemies.EnemySettings;
import Entity.Player.Player;
import Entity.Quests.*;
import Main.GameOptions;
import Main.GamePanel;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class HUD {
	
	private Player player;
	private HashMap<String, AudioPlayer> sfx;
	private BufferedImage image;
	private Font font;
	private QuestLog questLog;
	private DialogBox dialogBox;
	private boolean showMap;
	private boolean showInventory;
	private boolean showDialogBox;
	private boolean showQuestLog;
	private boolean showPauseMenu;
	
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
		createQuestLog();
		createDialogBox();
		setSoundEffects();
	}

	private void createQuestLog() {
		questLog = new QuestLog();
		QuestFactory questFactory = new QuestFactory("Kill 5 Sluggers",
				"Sluggers are those little slimy \n" +
				"spiky snails that keep terrorizing your farm.\n" +
				"Fellow villagers area scared of them.\n" +
				"Get rid of them", 5, EnemySettings.ENEMY_TYPES_SLUGGER);
		Quest killSluggersQuest = questFactory.getQuest(QuestSettings.QUEST_TYPE_KILLQUEST);
		questLog.addQuest(killSluggersQuest);
	}

	private void createDialogBox() {
		dialogBox = new DialogBox("Welcome " + player.getName(),
				"Mr.PotatoGuy: Welcome to the land of the pottu");
	}

	public void KillOneEnemy (int EnemyType) {
		for(Quest quest : questLog.getKillQuests()) {
			if(quest instanceof  KillQuest) {
				var killQuest = (KillQuest)quest;
				killQuest.KillOneEnemy(EnemyType, player);
			}
		}
 	}

 	public void TogglePauseMenu() {
		if(showPauseMenu == false) {
			player.saveGame();
		}
		showPauseMenu = !showPauseMenu;
	}

	public void ToggleInventory() {
		if(showInventory == false) {
			playSoundEffect("inventoryOpened");
		}
		showInventory = !showInventory;
	}

	public void ToggleDialogBox() {
		showDialogBox = !showDialogBox;
	}

	public void ToggleMap() {
		if(showMap == false) {
			playSoundEffect("takeMapFromPocket");
		}
		playSoundEffect("putMapBackToPocket");
		showMap = !showMap;
	}

	public void ToggleQuestLog() {
		showQuestLog = !showQuestLog;
	}
	
	public void draw(Graphics2D g) {

		drawImage(g);

		g.setFont(font);
		g.setColor(Color.WHITE);
		drawMoney(g);
		drawHealth(g);
		drawManaPoints(g);

		if(showDialogBox)
			dialogBox.draw(g);

		if(showMap)
			drawMap(g);

		if(showInventory)
			drawInventory(g);

		if(showQuestLog)
			drawQuestLog(g);

		drawDebugArea(g);

		if(player.isDead()) {
			drawDeathScreen(g);
		}

		if(showPauseMenu) {
			drawPauseMenu(g);
		}
	}

	private void drawDebugArea(Graphics2D g) {

		if(GameOptions.IS_DEBUG_MODE == false)
			return;

		Font titleFont = new Font(
				"Century Gothic",
				Font.PLAIN,
				12);
		g.setColor(Color.BLACK);
		g.setFont(titleFont);
		g.drawString("X: " + player.getX(), 400, 13);
		g.drawString("Y: " + player.getY(), 400, 28);
		g.drawString("MaxSpeed: " + player.getMaxSpeed(), 600, 13);
		g.drawString("StopSpeed: " + player.getStopSpeed(), 600, 28);
		g.setColor(Color.BLUE);
		g.drawRect((int) (player.getX() + player.getXMap() - player.getWidth() / 2),
				(int) (player.getY() + player.getYMap() - player.getHeight() / 2),
				player.getWidth(),
				player.getHeight());
	}

	private void drawQuestLog(Graphics2D g) {
		questLog.draw(g);
	}

	private void drawMap(Graphics2D g) {
		var x = 100;
		var y = 100;
		var width = GamePanel.WIDTH - 200;
		var height = GamePanel.HEIGHT - 200;
		var shape = new Rectangle(x, y, width, height);

		//Piirrä läpinäkyvä neliö
		int alpha = 127; // 50% transparent
		Color semiTransParentColor = new Color(180, 150, 150, alpha);
		g.setColor(semiTransParentColor);
		g.fill(shape);

		//Piirrä teksti
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Map", shape.x + 10, shape.y + 20);
	}

	private void drawInventory(Graphics2D g) {

		var x = 100;
		var y = 100;
		var width = GamePanel.WIDTH - 200;
		var height = GamePanel.HEIGHT - 200;
		var outerBox = new Rectangle(x, y, width, height);

		//Piirrä läpinäkyvä neliö
		int alpha = 127; // 50% transparent
		Color semiTransParentColor = new Color(180, 150, 150, alpha);
		g.setColor(semiTransParentColor);
		g.fill(outerBox);

		//Piirrä sisempi läpinäkyvä neliö
		alpha = 100; // vähemmän kuin 50% transparent
		semiTransParentColor = new Color(0, 0, 0, alpha);
		g.setColor(semiTransParentColor);
		var innerBox = new Rectangle(x + 10, y + 10, width-20, height-20);
		g.fill(innerBox);

		//Piirrä ääriviivat
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);

		drawInventoryTitle(g, innerBox);

		drawGold(g, innerBox);

		drawCharacterStatsTitle(g, innerBox);

		drawExperience(g, innerBox);

		drawAttackStats(g, innerBox);

		drawHealth(g, innerBox);
	}

	private final int inventoryXPosition = 10;
	private final int characterStatsXPosition = 480;

	private void drawDeathScreen(Graphics2D g) {

		var positionX = 0;
		var positionY = 100;
		var width = GamePanel.WIDTH - (2 * positionX);
		var height = GamePanel.HEIGHT - (2 * positionY);
		var box = new Rectangle(positionX, positionY, width, height);
		g.setColor(Color.BLACK);
		g.fillRect(box.x, box.y, box.width, box.height);

		//Centered teksti. Todo: tee tästä oma luokkansa esim HUD.CenteredText
		drawCenteredText(g, "You died.");
	}

	private void drawCenteredText(Graphics2D g, String s) {
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		var string = s;
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D r = fm.getStringBounds(string, g);
		int x = (GamePanel.WIDTH - (int) r.getWidth()) / 2;
		int y = (GamePanel.HEIGHT - (int) r.getHeight()) / 2 + fm.getAscent();
		g.drawString(string, x, y);
	}

	private void drawPauseMenu(Graphics2D g) {
		drawCenteredText(g, "Game saved.");
	}

	private void drawHealth(Graphics2D g, Rectangle innerBox) {
		g.setColor(Color.WHITE);
		g.drawString("HP : "+ player.getHealth() + " / " + player.getMaxHealth(), innerBox.x + characterStatsXPosition, innerBox.y + 100);
	}

	private void drawCharacterStatsTitle(Graphics2D g, Rectangle innerBox) {
		g.setColor(Color.WHITE);
		g.drawString("Character stats", innerBox.x + characterStatsXPosition, innerBox.y + 20);
	}

	private void drawExperience(Graphics2D g, Rectangle innerBox) {
		g.setColor(Color.CYAN);
		g.drawString("Exp: " + player.getExperience(), innerBox.x + characterStatsXPosition, innerBox.y + 40 );
	}

	private void drawGold(Graphics2D g, Rectangle innerBox) {
		g.setColor(Color.ORANGE);
		g.drawString("Gold: " + player.getMoneyInWallet(), innerBox.x + inventoryXPosition, innerBox.y + 40);
	}

	private void drawInventoryTitle(Graphics2D g, Rectangle innerBox) {
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Inventory", innerBox.x + inventoryXPosition, innerBox.y + 20);
	}

	private void drawAttackStats(Graphics2D g, Rectangle innerBox) {
		g.setColor(Color.WHITE);
		g.drawString("Scratch dmg: " + player.getScratchDamage(), innerBox.x + characterStatsXPosition, innerBox.y + 60);
		g.drawString("Fireball dmg: " + player.getFireBallDamage(), innerBox.x + characterStatsXPosition, innerBox.y + 80);
	}

	private void drawImage(Graphics2D g) {
		g.drawImage(image, 0, 10, null);
	}
	private final int textsXCoordinate = 25;

	private void drawMoney(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawString(player.getMoneyInWallet() + " g",
				textsXCoordinate,
				65
		);
	}

	private void drawManaPoints(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawString(
			player.getFire() / 100 + "/" + player.getMaxFire() / 100,
				textsXCoordinate,
			45
		);
	}

	private void drawHealth(Graphics2D g) {
		if(player.hasLowHealth())
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLACK);
		g.drawString(
			player.getHealth() + "/" + player.getMaxHealth(),
				textsXCoordinate,
			25
		);
	}

	private void setSoundEffects() {
		sfx = new HashMap<>();
		sfx.put("takeMapFromPocket", new AudioPlayer("/SFX/mapTakeFromPocket.wav"));
		sfx.put("putMapBackToPocket", new AudioPlayer("/SFX/mapPutBackToPocket.wav"));
		sfx.put("inventoryOpened", new AudioPlayer("/SFX/inventoryOpened.wav"));
	}

	private void playSoundEffect(String soundEffectName) {
		sfx.get(soundEffectName).play();
	}

}













