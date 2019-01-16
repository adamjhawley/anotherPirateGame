package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import lombok.Getter;
import uk.ac.york.sepr4.object.projectile.Projectile;
import uk.ac.york.sepr4.object.projectile.ProjectileManager;
import uk.ac.york.sepr4.screen.GameScreen;


public class EntityManager {

    private Player player;

    @Getter
    private Array<NPCBoat> npcList = new Array<>();

    private GameScreen gameScreen;

    @Getter
    private AnimationManager animationManager;
    @Getter
    private ProjectileManager projectileManager;

    public EntityManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        this.projectileManager = new ProjectileManager(gameScreen, this);
        this.animationManager = new AnimationManager(gameScreen, this);
    }

    public Integer getNextEnemyID() {
        return npcList.size;
    }

    public Player getOrCreatePlayer() {
        if(player == null) {
            player = new Player(gameScreen.getPirateMap().getSpawnPoint());
            animationManager.createWaterTrail(player);
        }
        return player;
    }

    public void addNPC(NPCBoat npcBoat){
        if(!npcList.contains(npcBoat, false)) {
            this.npcList.add(npcBoat);
            animationManager.createWaterTrail(npcBoat);
        } else {
            Gdx.app.error("EntityManager", "Tried to add an NPC with ID that already exists!");
        }
    }

    public Array<LivingEntity> getLivingEntitiesInArea(Rectangle rectangle) {
        Array<LivingEntity> entities = new Array<>();
        for(NPCBoat NPCBoat : npcList) {
            if(NPCBoat.getRectBounds().overlaps(rectangle)){
                entities.add(NPCBoat);
            }
        }
        if(player.getRectBounds().overlaps(rectangle)) {
            entities.add(player);
        }
        return entities;
    }


    public Array<NPCBoat> removeDeadNPCs() {
        Array<NPCBoat> toRemove = new Array<NPCBoat>();
        for(NPCBoat NPCBoat : npcList) {
            if(NPCBoat.isDead()){
                toRemove.add(NPCBoat);
            }
        }
        npcList.removeAll(toRemove, true);
        return toRemove;
    }

    public void handleStageEntities(Stage stage, float delta){
        projectileManager.handleProjectiles(stage);
        handleNPCs(stage);
        animationManager.handleEffects(stage, delta);
    }



    /**
     * Adds and removes NPCs as actors from the stage.
     */
    private void handleNPCs(Stage stage) {
        stage.getActors().removeAll(removeDeadNPCs(), true);

        for (NPCBoat NPCBoat : npcList) {
            if (!stage.getActors().contains(NPCBoat, true)) {
                Gdx.app.log("Test Log", "Adding new NPCBoat to actors list.");
                stage.addActor(NPCBoat);
            }
        }
    }




}
