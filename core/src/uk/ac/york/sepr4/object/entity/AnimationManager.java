package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
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

    //Death Animations
    private Array<Entity> effects = new Array<>();
    //For cleanup
    private Array<Entity> lastFrameEffects = new Array<>(); //Needed for clean up
    private HashMap<LivingEntity, Float> deathAnimations = new HashMap<>();

    //Water Trails
    private HashMap<LivingEntity, Vector2> waterTrails1 = new HashMap<>(), waterTrails2 = new HashMap<>();


    public AnimationManager(GameScreen gameScreen, EntityManager entityManager) {
        this.gameScreen = gameScreen;
        this.entityManager = entityManager;
    }


    //Takes the centre x,y of where you want the image to be
    public void addEffect(float x, float y, float angle, Texture texture, int width, int height, float alpha){
        Entity effect = new Entity(texture, new Vector2(x,y)) {};
        effect.setY(y - height/2);
        effect.setX(x - width/2);
        effect.setWidth(width);
        effect.setHeight(height);
        effect.setAlpha(alpha);
        effect.setAngle(angle);
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
            livingEntity.setAlpha(1-(deathTimer/5));
            if (deathTimer < 1/6f) {
                addEffect(livingEntity.getCentre().x, livingEntity.getCentre().y, livingEntity.getAngle(), TextureManager.EXPLOSION1, 40, 40,1);
            } else if (deathTimer < 2/6f) {
                addEffect(livingEntity.getCentre().x, livingEntity.getCentre().y, livingEntity.getAngle(), TextureManager.EXPLOSION2, 40, 40, 1);
            } else if (deathTimer < 1/2f){
                addEffect(livingEntity.getCentre().x, livingEntity.getCentre().y, livingEntity.getAngle(), TextureManager.EXPLOSION3, 40, 40,1);
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
