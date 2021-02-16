package Entity.Quests;

public class QuestFactory {

    private String title;
    private String description;
    private int howManyNeedsToBeKilled;
    private int enemyType;

    public QuestFactory() {

    }

    public QuestFactory(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public QuestFactory(String title, String description, int howManyNeedsToBeKilled, int enemyType) {
        this.title = title;
        this.description = description;
        this.howManyNeedsToBeKilled = howManyNeedsToBeKilled;
        this.enemyType = enemyType;
    }

    public Quest getQuest(int questType) {
        if(questType <= 0)
            return null;

        if(questType == QuestSettings.QUEST_TYPE_KILLQUEST) {
            return new KillQuest(title, description, howManyNeedsToBeKilled, enemyType);
        }

        return null;
    }

}
