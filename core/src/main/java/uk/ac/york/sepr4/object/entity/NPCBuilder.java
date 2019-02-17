package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.building.College;
import java.util.Optional;
import java.util.Random;

public class NPCBuilder {

    private float speed = 0f, maxSpeed = 100f, range = 500f, accuracy = 0.5f, idealDistFromTarget = 250f,
            gradientFromNormalDist = 50f, reqCooldown = 0.8f;
    private Double health = 20.0, maxHealth = 20.0, damage = 5.0;
    private Integer turningSpeed = 2;

    public NPCBuilder() {}

public NPCBoat generateRandomEnemy(Vector2 pos, College allied, float difficulty, boolean isBoss) {
        Random random = new Random();

        NPCBoat npcBoat;

        if (isBoss) {
            npcBoat = new NPCBoat(TextureManager.BOSS, pos);
        } else {
            npcBoat = new NPCBoat(TextureManager.ENEMY, pos);
        }

        npcBoat.setAngle((float) (2*Math.PI*random.nextDouble()));;
        npcBoat.setAccuracy(accuracy + difficulty/50);
        npcBoat.setMaxSpeed(difficulty+maxSpeed);
        npcBoat.setRange(range);
        npcBoat.setIdealDistFromTarget(idealDistFromTarget);
        npcBoat.setGradientForNormalDist(gradientFromNormalDist);
        npcBoat.setMaxHealth(difficulty + maxHealth);
        npcBoat.setHealth(npcBoat.getMaxHealth());
        npcBoat.setTurningSpeed(Math.round(difficulty/50)+turningSpeed);
        npcBoat.setAllied(Optional.of(allied));
        npcBoat.setBoss(isBoss);
        npcBoat.setDamage(damage + difficulty / 20);
        npcBoat.setReqCooldown(reqCooldown);


        return npcBoat;
    }

}
