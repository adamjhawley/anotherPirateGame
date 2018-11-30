package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import uk.ac.york.sepr4.screen.GameScreen;

@Data
public class EntityManager {

    //ToDo: Add a function that takes a square or circle and returns all entitys within it
    private Player player;

    Array<Enemy> enemyList;

    private GameScreen gameScreen;

    public EntityManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.enemyList = new Array<Enemy>();
    }

    public Integer getNextEnemyID() {
        return this.enemyList.size;
    }

    public Player getOrCreatePlayer() {
        if(player == null) {
            player = new Player(gameScreen.getSpawnPoint());
        }
        return player;
    }

    public void addEnemy(Enemy enemy){
        this.enemyList.add(enemy);
    }

    public Array<LivingEntity> getEnemiesInArea(Rectangle rectangle) {
        Array<LivingEntity> entities = new Array<>();
        for(Enemy enemy : enemyList) {
            if(enemy.getBounds().overlaps(rectangle)){
                entities.add(enemy);
            }
        }
        if(player.getBounds().overlaps(rectangle)) {
            entities.add(player);
        }
        return entities;
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
