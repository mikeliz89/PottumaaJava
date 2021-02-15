package Entity;

import Audio.AudioPlayer;
import Entity.Enemies.EnemySettings;
import Entity.Player.Player;
import Entity.Quests.KillQuest;
import Entity.Quests.Quest;
import Entity.Quests.QuestLog;
import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class HUD {
	
	private Player player;
	private HashMap<String, AudioPlayer> sfx;
	private BufferedImage image;
	private Font font;
	private QuestLog questLog;
	private boolean showMap;
	private boolean showInventory;
	private boolean showDialogBox;
	private boolean showQuestLog;
	
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
		setSoundEffects();
	}

	private void createQuestLog() {
		questLog = new QuestLog();
		Quest killSluggersQuest = new KillQuest("Kill 5 Sluggers",
				"Sluggers are those little slimy \n" +
				"spiky snails that keep terrorizing your farm.\n" +
				"Fellow villagers area scared of them.\n" +
				"Get rid of them", 5, EnemySettings.ENEMY_TYPES_SLUGGER);
		questLog.addQuest(killSluggersQuest);
	}

	public void KillOneEnemy (int EnemyType) {
		for(Quest quest : questLog.getKillQuests()) {
			if(quest instanceof  KillQuest) {
				quest.KillOneEnemy(EnemyType, player);
			}
		}
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

		DrawImage(g);

		DrawTexts(g);

		if(showDialogBox)
			DrawDialogBox(g);

		if(showMap)
			DrawMap(g);

		if(showInventory)
			DrawInventory(g);

		if(showQuestLog)
			DrawQuestLog(g);
	}

	private void DrawQuestLog(Graphics2D g) {
		questLog.draw(g);
	}

	private void DrawMap(Graphics2D g) {
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

	private void DrawDialogBox(Graphics2D g) {
		var x = 100;
		var y = GamePanel.HEIGHT - 210;
		var width = GamePanel.WIDTH - 200;
		var height = 200;
		var shape = new Rectangle(x, y, width, height);
		int alpha = 127; // 50% transparent
		Color semiTransParentColor = new Color(180, 150, 150, alpha);
		g.setColor(semiTransParentColor);
		g.fill(shape);

		//Piirrä ääriviivat
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);

		//Piirrä tekstiä
		g.setColor(Color.BLACK);
		g.drawString("Tähän tulee paljon tekstiä blaa blaa blaa", shape.x + 20, shape.y + 20);
	}

	private void DrawInventory(Graphics2D g) {

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

		DrawInventoryTitle(g, innerBox);

		DrawGold(g, innerBox);

		DrawCharacterStatsTitle(g, innerBox);

		DrawExperiencePoints(g, innerBox);

		DrawAttackStats(g, innerBox);

		DrawHealthPoints(g, innerBox);
	}

	private final int inventoryXPosition = 10;
	private final int characterStatsXPosition = 480;

	private void DrawHealthPoints(Graphics2D g, Rectangle innerBox) {
		g.setColor(Color.WHITE);
		g.drawString("HP : "+ player.getHealth() + " / " + player.getMaxHealth(), innerBox.x + characterStatsXPosition, innerBox.y + 100);
	}

	private void DrawCharacterStatsTitle(Graphics2D g, Rectangle innerBox) {
		g.setColor(Color.WHITE);
		g.drawString("Character stats", innerBox.x + characterStatsXPosition, innerBox.y + 20);
	}

	private void DrawExperiencePoints(Graphics2D g, Rectangle innerBox) {
		g.setColor(Color.CYAN);
		g.drawString("Exp: " + player.getExperiencePointsAmount(), innerBox.x + characterStatsXPosition, innerBox.y + 40 );
	}

	private void DrawGold(Graphics2D g, Rectangle innerBox) {
		g.setColor(Color.ORANGE);
		g.drawString("Gold: " + player.getMoneyInWallet(), innerBox.x + inventoryXPosition, innerBox.y + 40);
	}

	private void DrawInventoryTitle(Graphics2D g, Rectangle innerBox) {
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Inventory", innerBox.x + inventoryXPosition, innerBox.y + 20);
	}

	private void DrawAttackStats(Graphics2D g, Rectangle innerBox) {
		g.setColor(Color.WHITE);
		g.drawString("Scratch dmg: " + player.getScratchDamage(), innerBox.x + characterStatsXPosition, innerBox.y + 60);
		g.drawString("Fireball dmg: " + player.getFireBallDamage(), innerBox.x + characterStatsXPosition, innerBox.y + 80);
	}

	private void DrawImage(Graphics2D g) {
		g.drawImage(image, 0, 10, null);
	}
	private final int textsXCoordinate = 25;

	private void DrawTexts(Graphics2D g) {
		g.setFont(font);
		g.setColor(Color.WHITE);

		DrawMoneyAmount(g);
		DrawHealthPointsToHUD(g);
		DrawManaPoints(g);
	}

	private void DrawMoneyAmount(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawString(player.getMoneyInWallet() + " g",
				textsXCoordinate,
				65
		);
	}

	private void DrawManaPoints(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawString(
			player.getFire() / 100 + "/" + player.getMaxFire() / 100,
				textsXCoordinate,
			45
		);
	}

	private void DrawHealthPointsToHUD(Graphics2D g) {
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













