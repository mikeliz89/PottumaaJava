package Entity.Quests;

import Entity.Player.Player;

public abstract class Quest {

    private String title;
    private String description;

    private boolean questIsDone;

    public Quest(String title, String description) {
        this.title = title;
        this.description = description;
        questIsDone = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isQuestDone() {
        return questIsDone;
    }

    public void markQuestDone(Player player) {
        questIsDone = true;
        player.addMoney(100);
        player.addExperience(100);
    }

    public void KillOneEnemy(int EnemyType, Player player) {

    }
}
