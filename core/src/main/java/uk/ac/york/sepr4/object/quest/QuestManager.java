package uk.ac.york.sepr4.object.quest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Data;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.entity.EntityManager;

import java.util.ArrayList;

@Data
public class QuestManager {

    private Quest currentQuest, lastQuest;
    private Array<Quest> questList;
    private EntityManager entityManager;
    private Boolean allQuestsCompleted;
    private ArrayList<College> capturedColleges;

    public QuestManager(EntityManager entityManager) {
        this.entityManager = entityManager;

        Json json = new Json();
        this.questList= json.fromJson(Array.class, Quest.class, Gdx.files.internal("quests.json"));
        this.questList.reverse();
        allQuestsCompleted = false;
        this.chooseQuest();
        capturedColleges = new ArrayList<>();

    }
    /**
     * Amended for Assessment 4: Popping from reversed array to give queue implementation.
     * @return Random un-completed Quest
     */
    public Quest chooseQuest(){
        if (this.questList.size !=0) {
            this.currentQuest = this.questList.pop();
            this.currentQuest.setIsStarted(true);
            Gdx.app.log("QuestManager", "Quest selected: " + this.getCurrentQuest().getName());
            return this.currentQuest;
        }
        return null;
    }
    /**
     * Checks through the current questList to find the most recently started and not completed quest.
     * @return Quest in progress
     */
    public void finishCurrentQuest() {
        this.currentQuest.setIsCompleted(true);
        this.lastQuest = this.currentQuest;
        this.questList.removeValue(this.currentQuest,true);
        Gdx.app.log("QuestManager","Quest '" + getLastQuest().getName() + "' Completed");
        if (this.chooseQuest() == null){
            allQuestsCompleted = true;
        }else{
            allQuestsCompleted = false;
        }
    }

    public Quest getCurrentQuest(){
        if (allQuestsCompleted == false){
            return this.currentQuest;
        }else{
            return null;
        }

    }

    /**
     * Returns the current quest if there is one, otherwise states no quest active.
     * @return String representation of quest status.
     **/
    public String getQuestStatus() {
        if (this.questList.size == 0) {
            return "No quests active";
        }
        else{
            return this.getCurrentQuest().getName();
        }
    }


}
