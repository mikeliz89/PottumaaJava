package Entity.Quests;

import Audio.AudioPlayer;
import Entity.Player.Player;

import java.util.HashMap;

public abstract class Quest {

    private String title;
    private String description;
    private HashMap<String, AudioPlayer> sfx;
    private boolean questIsDone;

    public Quest(String title, String description) {
        this.title = title;
        this.description = description;
        questIsDone = false;

        setSoundEffects();
    }

    private void setSoundEffects() {
        sfx = new HashMap<>();
        sfx.put("questIsDone", new AudioPlayer("/SFX/questIsDone.wav"));
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
        playSoundEffect("questIsDone");
        //reward player for finishing quest
        //todo: add rewardMoneyAmount and rewardExperienceAmount to Quest, so that every quest gives different amount of rewards
        player.addMoney(100);
        player.addExperience(100);
    }

    private void playSoundEffect(String soundEffectName) {
        sfx.get(soundEffectName).play();
    }

}
