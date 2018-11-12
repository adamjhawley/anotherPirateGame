package uk.ac.york.sepr4.object.entity;

import lombok.Data;

@Data
public class EntityManager {

    private Player player;
    private Enemy enemy;

    public EntityManager() {
    }

    public Player getOrCreatePlayer() {
        if(player == null) {
            player = new Player();
        }
        return player;
    }

    public Enemy getOrCreateEnemy() { //Will need to be changed
        if(enemy == null) {
            enemy = new Enemy();
        }
        return enemy;
    }


}
