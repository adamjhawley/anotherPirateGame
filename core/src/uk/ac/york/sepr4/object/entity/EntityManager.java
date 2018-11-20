package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import uk.ac.york.sepr4.object.projectile.Projectile;
import uk.ac.york.sepr4.screen.GameScreen;

@Data
public class EntityManager {

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
            player = new Player(gameScreen.getPirateMap().getSpawnPoint());
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

    public void handleStageEntities(Stage stage){
        handleProjectiles(stage);
        handleEnemies(stage);
    }

    /**
     * Adds and removes projectiles as actors from the stage.
     */
    private void handleProjectiles(Stage stage) {
        stage.getActors().removeAll(gameScreen.getProjectileManager().removeNonActiveProjectiles(), true);

        for (Projectile projectile : gameScreen.getProjectileManager().getProjectileList()) {
            if (!stage.getActors().contains(projectile, true)) {
                Gdx.app.log("Test Log", "Adding new projectile to actors list.");
                stage.addActor(projectile);
            }
        }
    }

    /**
     * Adds and removes enemies as actors from the stage.
     */
    private void handleEnemies(Stage stage) {
        stage.getActors().removeAll(removeDeadEnemies(), true);

        for (Enemy enemy : getEnemyList()) {
            if (!stage.getActors().contains(enemy, true)) {
                Gdx.app.log("Test Log", "Adding new enemy to actors list.");
                stage.addActor(enemy);
            }
        }
    }


}
