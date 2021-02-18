package Entity.Quests;

import Entity.Player.Player;

public class KillQuest extends Quest {

    private int HowManyNeedsToBeKilled = 5;
    private int CurrentKillCount = 0;
    private int EnemyType;

    public KillQuest(String title, String description, int howManyNeedsToBeKilled, int enemyType) {
        super(title, description);
        this.HowManyNeedsToBeKilled = howManyNeedsToBeKilled;
        this.EnemyType = enemyType;
    }

    public int getHowManyNeedsToBeKilled() {
        return HowManyNeedsToBeKilled;
    }

    public int getCurrentKillCount() {
        return CurrentKillCount;
    }

    public void KillOneEnemy(int killedEnemyType, Player player) {

        if(EnemyType != killedEnemyType) {
          return;
        }

        CurrentKillCount++;

        if(CurrentKillCount > HowManyNeedsToBeKilled)
            CurrentKillCount = HowManyNeedsToBeKilled;

        if(CurrentKillCount == HowManyNeedsToBeKilled) {
            markQuestDone(player);
        }
    }
}
