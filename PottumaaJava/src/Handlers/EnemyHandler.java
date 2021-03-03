package Handlers;

import Entity.Enemies.Enemy;
import Entity.Explosion;
import Entity.Player.Player;
import Entity.Quests.KillQuest;
import Entity.Quests.Quest;
import Entity.Quests.QuestLog;

import java.awt.*;
import java.util.ArrayList;

public class EnemyHandler extends BaseHandler {

    private ArrayList<Enemy> enemies;
    private Player player;
    private ExplosionHandler explosionHandler;
    private QuestLog questLog;

    public EnemyHandler(Player player, ExplosionHandler explosionHandler, QuestLog questLog) {
        this.player = player;
        this.explosionHandler = explosionHandler;
        this.questLog = questLog;
        enemies = new ArrayList<>();
    }

    public void add(Enemy enemy) {
        enemies.add(enemy);
    }

    public void draw(Graphics2D g) {
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
    }

    public void update() {
        updateEnemies();

        checkPlayerAttackingEnemies();
    }

    private void updateEnemies() {
        for(int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if(e.isDead()) {
                killOneEnemy(e.getEnemyType());
                givePlayerRewardForKillingEnemy(e);
                enemies.remove(i);
                i--;
                explosionHandler.addExplosion(new Explosion(e.getX(), e.getY()));
            }
        }
    }

    private void givePlayerRewardForKillingEnemy(Enemy e) {
        player.addExperience(e.getExperienceGainedWhenKilled());
        player.addMoney(e.getMoneyGainedWhenKilled());
    }

    private void killOneEnemy(int EnemyType) {
        for(Quest quest : questLog.getKillQuests()) {
            if(quest instanceof KillQuest) {
                var killQuest = (KillQuest)quest;
                killQuest.killOneEnemy(EnemyType, player);
            }
        }
    }

    private void checkPlayerAttackingEnemies() {
        player.checkAttack(enemies);
    }
}
