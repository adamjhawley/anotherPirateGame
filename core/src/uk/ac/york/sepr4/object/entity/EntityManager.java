package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import lombok.Data;

@Data
public class EntityManager {

    private Player player;

    Array<Enemy> enemyList;

    public EntityManager() {
        this.enemyList = new Array<Enemy>();
    }

    public Integer getNextEnemyID() {
        return this.enemyList.size;
    }

    public Player getOrCreatePlayer() {
        if(player == null) {
            player = new Player();
        }
        return player;
    }

    public void addEnemy(Enemy enemy){
        this.enemyList.add(enemy);
    }

    public Enemy getEnemy(Integer id) throws IllegalArgumentException { //Will need to be changed
        Enemy enemy = enemyList.get(id);
        if(enemy != null) {
            return enemy;
        }
        throw new IllegalArgumentException("No enemy found with given ID.");
    }

    public Array<Enemy> removeDeadEnemies() {
        Array<Enemy> toRemove = new Array<Enemy>();
        for(Enemy enemy : enemyList) {
            if(enemy.isDead()){
                Gdx.app.log("Test", "3");

                toRemove.add(enemy);
            }
        }
        enemyList.removeAll(toRemove, true);
        return toRemove;
    }


}
