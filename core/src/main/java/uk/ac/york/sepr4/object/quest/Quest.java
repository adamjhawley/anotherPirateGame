package uk.ac.york.sepr4.object.quest;

import lombok.Data;
import lombok.Getter;
import uk.ac.york.sepr4.object.item.Reward;

import java.util.List;

@Data
public class Quest {

    private String name, startMessage, endMessage;
    private List<Quest> requires, gives;
    private Reward reward;
    private String targetEntityName;
    private boolean isKillQuest, isStarted, isCompleted;
    @Getter
    private long timeCompleted;


    public Quest(){
        // Empty constructor for JSON DAO.
    }

    //Getters and Setters
    public String getTargetEntityName(){return this.targetEntityName;}
    public boolean getIsKillQuest(){return this.isKillQuest;}
    public void setIsStarted(boolean val){this.isStarted = val;}
    public void setIsCompleted(boolean complete){
        this.isCompleted = complete;
        this.timeCompleted = System.currentTimeMillis();
    }
}
