package uk.ac.york.sepr4.object.entity;

import lombok.Data;

@Data
public class EntityManager {

    private Player player;

    public EntityManager() {

    }

    public Player getOrCreatePlayer() {
        if(player == null) {
            player = new Player();
        }
        return player;
    }

}
