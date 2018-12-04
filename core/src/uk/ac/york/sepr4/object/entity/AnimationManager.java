package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.screen.GameScreen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AnimationManager {

    private GameScreen gameScreen;
    private EntityManager entityManager;


    //Added by harry for the death animation
    private Array<Entity> effects;
    private Array<Entity> lastFrameEffects; //Needed for clean up

    private HashMap<LivingEntity, Float> deathAnimations;


    public AnimationManager(GameScreen gameScreen, EntityManager entityManager) {
        this.gameScreen = gameScreen;
        this.entityManager = entityManager;

        this.effects = new Array<>();
        this.lastFrameEffects = new Array<>();

        this.deathAnimations = new HashMap<>();
    }

    public Integer getNextEffectID(){return this.effects.size;}

    //Takes the centre x,y of where you want the image to be
    public void addEffect(float x, float y, float angle, float speed, Texture texture, int width, int height){
        Entity effect = new Entity(getNextEffectID(), texture, angle, speed) {};
        effect.setY(y - height/2);
        effect.setX(x - width/2);
        effect.setWidth(width);
        effect.setHeight(height);
        this.effects.add(effect);
    }

    private void handleDeathAnimations(float delta) {
        //add dying npcs if they arent already in there
        for(LivingEntity livingEntity : entityManager.getNPCList()) {
            if(livingEntity.isDying() && !deathAnimations.containsKey(livingEntity)) {
                deathAnimations.put(livingEntity, 0f);
            }
        }
        for(Iterator<Map.Entry<LivingEntity, Float>> it = deathAnimations.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<LivingEntity, Float> entry = it.next();
            LivingEntity livingEntity = entry.getKey();
            Float deathTimer = entry.getValue();
            livingEntity.setTexture(TextureManager.DEADENEMY);
            if (deathTimer < 1/6f) {
                addEffect(livingEntity.getCentre().x, livingEntity.getCentre().y, livingEntity.getAngle(), 0f, TextureManager.EXPLOSION1, 40, 40);
            } else if (deathTimer < 2/6f) {
                addEffect(livingEntity.getCentre().x, livingEntity.getCentre().y, livingEntity.getAngle(), 0f, TextureManager.EXPLOSION2, 40, 40);
            } else if (deathTimer < 1/2f){
                addEffect(livingEntity.getCentre().x, livingEntity.getCentre().y, livingEntity.getAngle(), 0f, TextureManager.EXPLOSION3, 40, 40);
            }
            if (deathTimer > 5){
                livingEntity.setDead(true);
                livingEntity.setDying(false);
                it.remove();
            } else {
                entry.setValue(entry.getValue()+delta);
            }
        }
    }


    /**
     * Removes all effects then adds all new effects
     * Effects work on a frame by frame basis so need to be spawned in every frame
     */
    public void handleEffects(Stage stage, float delta) {
        handleDeathAnimations(delta);

        stage.getActors().removeAll(this.lastFrameEffects, true);

        for (Entity effect : effects) {
            if (!stage.getActors().contains(effect, true)) {
                //Gdx.app.log("Test Log", "Adding new effect to actors list.");
                stage.addActor(effect);
            }
        }

        this.lastFrameEffects = this.effects;
        this.effects = new Array<>();
    }
}
