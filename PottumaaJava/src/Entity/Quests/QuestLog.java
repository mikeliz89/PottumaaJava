package Entity.Quests;

import Entity.Player.Player;
import Entity.Quests.Quest;

import java.awt.*;
import java.util.ArrayList;

public class QuestLog {
    ArrayList<Quest> Quests;

    public QuestLog() {
        init();
    }

    private void init() {
        Quests = new ArrayList<>();
    }

    public void addQuest(Quest quest) {
        Quests.add(quest);
    }

    public void markQuestDone(Quest quest, Player player) {
        quest.markQuestDone(player);
    }

    public ArrayList<Quest> getQuests() {
        return Quests;
    }

    public ArrayList<Quest> getKillQuests() {
        var killQuests = new ArrayList<Quest>();
        for(Quest quest : Quests) {
            if(quest instanceof KillQuest) {
                killQuests.add(quest);
            }
        }
        return killQuests;
    }

    public void draw(java.awt.Graphics2D g) {

        var x = 100;
        var y = 0;
        var width = 400;
        var height = 400;
        var alpha = 100; // vähemmän kuin 50% transparent
        var semiTransParentColor = new Color(0, 0, 0, alpha);
        g.setColor(semiTransParentColor);
        var innerBox = new Rectangle(x + 10, y + 10, width-20, height-20);
        g.fill(innerBox);

        //piirrä reunat
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        g.setColor(Color.WHITE);
        g.drawString("QuestLog: ", x+100, y+20);

        var fontTitle = new Font("Arial", Font.PLAIN, 15);
        var fontDescription = new Font("Arial", Font.PLAIN, 12);

        var i = 1;
        for(Quest quest : Quests) {
            //Title
            g.setColor(Color.BLACK);
            g.setFont(fontTitle);
            g.drawString(quest.getTitle(), x+100, y+20 + (i * 15));

            if(quest instanceof KillQuest) {
                var enemiesKilled = ((KillQuest) quest).getCurrentKillCount();
                var toBeKilled = ((KillQuest) quest).getHowManyNeedsToBeKilled();
                g.setColor(Color.RED);
                g.drawString("Enemies killed " + enemiesKilled + " / " + toBeKilled, x+200, y+ 20 + (i * 15));
            }

            //Quest done
            if(quest.isQuestDone()) {
                g.setColor(Color.CYAN);
                g.drawString("DONE!", x + 200, y+20 + (i * 15));
            }

            //Description
            g.setColor(Color.ORANGE);
            g.setFont(fontDescription);
            drawDescription(g, quest.getDescription(), x+10, y+20 + (i * 15));
            i++;
        }
    }

    void drawDescription(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }

}
