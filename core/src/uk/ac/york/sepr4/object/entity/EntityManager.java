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

@Data
public class EntityManager {

    private Player player;

    //NPCBoat ID should also be location in list.
    Array<NPCBoat> NPCList;

    private GameScreen gameScreen;

    @Getter
    private AnimationManager animationManager;
    @Getter
    private ProjectileManager projectileManager;

    public EntityManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.NPCList = new Array<>();

        this.projectileManager = new ProjectileManager(gameScreen, this);
        this.animationManager = new AnimationManager(gameScreen, this);
    }

    public Integer getNextEnemyID() {
        return this.NPCList.size;
    }

    public Player getOrCreatePlayer() {
        if(player == null) {
            player = new Player(gameScreen.getPirateMap().getSpawnPoint());
        }
        return player;
    }

    public void addNPC(NPCBoat npcBoat){
        if(!NPCList.contains(npcBoat, false)) {
            this.NPCList.add(npcBoat);
        } else {
            Gdx.app.error("EntityManager", "Tried to add an NPC with ID that already exists!");
        }
    }

    public Array<LivingEntity> getLivingEntitiesInArea(Rectangle rectangle) {
        Array<LivingEntity> entities = new Array<>();
        for(NPCBoat NPCBoat : NPCList) {
            if(NPCBoat.getRectBounds().overlaps(rectangle)){
                entities.add(NPCBoat);
            }
        }
        if(player.getRectBounds().overlaps(rectangle)) {
            entities.add(player);
        }
        return entities;
    }

    public NPCBoat getNPC(Integer id) throws IllegalArgumentException { //Will need to be changed
        NPCBoat NPCBoat = NPCList.get(id);
        if(NPCBoat != null) {
            return NPCBoat;
        }
        throw new IllegalArgumentException("No NPCBoat found with given ID.");
    }

    public Array<NPCBoat> removeDeadNPCs() {
        Array<NPCBoat> toRemove = new Array<NPCBoat>();
        for(NPCBoat NPCBoat : NPCList) {
            if(NPCBoat.isDead()){
                toRemove.add(NPCBoat);
            }
        }
        NPCList.removeAll(toRemove, true);
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

        for (NPCBoat NPCBoat : getNPCList()) {
            if (!stage.getActors().contains(NPCBoat, true)) {
                Gdx.app.log("Test Log", "Adding new NPCBoat to actors list.");
                stage.addActor(NPCBoat);
            }
        }
    }




}
