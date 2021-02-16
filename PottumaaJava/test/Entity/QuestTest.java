package Entity;

import Entity.Quests.Quest;
import Entity.Quests.QuestFactory;
import Entity.Quests.QuestSettings;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuestTest {

    private final Quest myQuest;
    private final String questTitle = "Welcome to hell";
    private final String questDescription = "This is it";

    public QuestTest() {
        var questFactory = new QuestFactory(questTitle, questDescription);
        myQuest = questFactory.getQuest(QuestSettings.QUEST_TYPE_KILLQUEST);
    }

    @Test
    public void getTitleShouldReturnTitle() {
        var title = myQuest.getTitle();
        assertEquals("Welcome to hell", title);
    }

    @Test
    public void getDescriptionShouldReturnDescription() {
        var description = myQuest.getDescription();
        assertEquals("This is it", description);
    }

}