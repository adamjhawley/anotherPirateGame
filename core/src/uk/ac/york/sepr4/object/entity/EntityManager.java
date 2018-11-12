package uk.ac.york.sepr4.object.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EntityManager {

    private Player player;
    private Enemy enemy;

    List<Enemy> enemyList;

    public EntityManager() {
        this.enemyList = new ArrayList<Enemy>();
    }

    public Integer getNextEnemyID() {
        return this.enemyList.size();
    }

    public Player getOrCreatePlayer() {
        if(player == null) {
            player = new Player();
        }
        return player;
    }

    public Enemy getEnemy(Integer id) throws IllegalArgumentException { //Will need to be changed
        Enemy enemy = enemyList.get(id);
        if(enemy != null) {
            return enemy;
        }
        throw new IllegalArgumentException("No enemy found with given ID.");
    }


}
