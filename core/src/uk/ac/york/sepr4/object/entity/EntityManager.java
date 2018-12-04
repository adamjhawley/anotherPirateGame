package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import uk.ac.york.sepr4.object.projectile.Projectile;
import uk.ac.york.sepr4.screen.GameScreen;

@Data
public class EntityManager {

    //ToDo: Add a function that takes a square or circle and returns all entitys within it
    private Player player;

    Array<NPCBoat> enemyList;

    //Added by harry for the death animation
    Array<Entity> effects;
    Array<Entity> lastFrameeffects; //Needed for clean up

    private GameScreen gameScreen;

    public EntityManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.enemyList = new Array<>();
        this.effects = new Array<>();
        this.lastFrameeffects = new Array<>();
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

    public void addNPC(NPCBoat NPCBoat){
        this.enemyList.add(NPCBoat);
    }

    public Integer getNextEffectID(){return this.effects.size;};

    //Takes the centre x,y of where you want the image to be
    public void addEffect(float x, float y, float angle, float speed, Texture texture, int width, int height, float alpha){
        Entity effect = new Entity(getNextEffectID(), texture, angle, speed) {};
        effect.setY(y - height/2);
        effect.setX(x - width/2);
        effect.setWidth(width);
        effect.setHeight(height);
        effect.setAlpha(alpha);
        this.effects.add(effect);
    }

    public Array<LivingEntity> getNPCInArea(Rectangle rectangle) {
        Array<LivingEntity> entities = new Array<>();
        for(NPCBoat NPCBoat : enemyList) {
            if(NPCBoat.getRectBounds().overlaps(rectangle)){
                entities.add(NPCBoat);
            }
        }
        if(player.getRectBounds().overlaps(rectangle)) {
            entities.add(player);
        }
        return entities;
    }

    public NPCBoat getEnemy(Integer id) throws IllegalArgumentException { //Will need to be changed
        NPCBoat NPCBoat = enemyList.get(id);
        if(NPCBoat != null) {
            return NPCBoat;
        }
        throw new IllegalArgumentException("No NPCBoat found with given ID.");
    }

    public Array<NPCBoat> removeDeadEnemies() {
        Array<NPCBoat> toRemove = new Array<NPCBoat>();
        for(NPCBoat NPCBoat : enemyList) {
            if(NPCBoat.isDead()){
                Gdx.app.log("Test", "3");

                toRemove.add(NPCBoat);
            }
        }
        enemyList.removeAll(toRemove, true);
        return toRemove;
    }

    public void handleStageEntities(Stage stage){
        handleProjectiles(stage);
        handleNPCs(stage);
        handleEffects(stage);
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
     * Adds and removes NPCs as actors from the stage.
     */
    private void handleNPCs(Stage stage) {
        stage.getActors().removeAll(removeDeadEnemies(), true);

        for (NPCBoat NPCBoat : getEnemyList()) {
            if (!stage.getActors().contains(NPCBoat, true)) {
                Gdx.app.log("Test Log", "Adding new NPCBoat to actors list.");
                stage.addActor(NPCBoat);
            }
        }
    }

    /**
     * Removes all effects then adds all new effects
     * Effects work on a frame by frame basis so need to be spawned in every frame
     */
    private void handleEffects(Stage stage) {
        stage.getActors().removeAll(this.lastFrameeffects, true);

        for (Entity effect : getEffects()) {
            if (!stage.getActors().contains(effect, true)) {
                //Gdx.app.log("Test Log", "Adding new effect to actors list.");
                stage.addActor(effect);
            }
        }

        this.lastFrameeffects = this.effects;
        this.effects = new Array<Entity>();
    }



}
