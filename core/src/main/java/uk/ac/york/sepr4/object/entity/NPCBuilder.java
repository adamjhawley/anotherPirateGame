package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.building.College;
import java.util.Optional;
import java.util.Random;

public class NPCBuilder {

    private float angle = 0f, speed = 0f, maxSpeed = 100f, range = 500f,
            accuracy = 0.5f, idealDistFromTarget = 250f, gradientFromNormalDist = 50f, reqCooldown = 0.8f;
    private Double health = 20.0, maxHealth = 20.0, damage = 5.0;
    private Integer turningSpeed = 2;
    private Texture texture = TextureManager.ENEMY;
    private Optional<College> allied = Optional.empty();
    private boolean isBoss = false;

    public NPCBuilder() {}

    public NPCBoat buildNPC(Vector2 pos) {
        NPCBoat npcBoat;

        if(isBoss && texture == TextureManager.ENEMY) {
            //if boss and default texture
            npcBoat = new NPCBoat(TextureManager.BOSS, pos);
        }
        else {
            npcBoat = new NPCBoat(texture, pos);
        }
        npcBoat.setAngle(angle);
        npcBoat.setAccuracy(accuracy);
        npcBoat.setSpeed(speed);
        npcBoat.setMaxSpeed(maxSpeed);
        npcBoat.setRange(range);
        npcBoat.setIdealDistFromTarget(idealDistFromTarget);
        npcBoat.setGradientForNormalDist(gradientFromNormalDist);
        npcBoat.setHealth(health);
        npcBoat.setMaxHealth(maxHealth);
        npcBoat.setTurningSpeed(turningSpeed);
        npcBoat.setAllied(allied);
        npcBoat.setBoss(isBoss);
        npcBoat.setDamage(damage);
        npcBoat.setReqCooldown(reqCooldown);

        return npcBoat;
    }

    public NPCBuilder allied(College allied) {
        if(this.allied != null) {
            this.allied = Optional.of(allied);
        }
        return this;
    }

    public NPCBuilder boss(boolean isBoss) {
        this.isBoss = isBoss;
        return this;
    }

    public NPCBuilder reqCooldown(float reqCooldown) {
        this.reqCooldown = reqCooldown;
        return this;
    }

    public NPCBuilder damage(Double damage) {
        this.damage = damage;
        return this;
    }

    public NPCBuilder angle(float angle) {
        this.angle = angle;
        return this;
    }

    public NPCBuilder texture(Texture texture) {
        this.texture = texture;
        return this;
    }

    public NPCBuilder turningSpeed(Integer turningSpeed) {
        this.turningSpeed = turningSpeed;
        return this;
    }

    public NPCBuilder maxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public NPCBuilder range(float range) {
        this.range =  range;
        return this;
    }

//    public NPCBuilder accuracy(float accuracy) {
//        this.accuracy = accuracy;
//        return this;
//    }

    public NPCBuilder health(Double health) {
        this.health = health;
        this.maxHealth = health;
        return this;
    }

    public NPCBoat generateRandomEnemy(Vector2 pos, College allied, Double difficulty) {
        return generateRandomEnemy(pos, allied, difficulty, false);
    }

    public NPCBoat generateRandomEnemy(Vector2 pos, College allied, Double difficulty, boolean isBoss) {
        Random random = new Random();

        NPCBoat npcBoat;

        if (isBoss) {
            npcBoat = new NPCBoat(TextureManager.BOSS, pos);
        } else {
            npcBoat = new NPCBoat(TextureManager.ENEMY, pos);
        }

        npcBoat.setAngle((float) (2*Math.PI*random.nextDouble()));;
        npcBoat.setAccuracy(accuracy);
        npcBoat.setSpeed(speed);
        npcBoat.setMaxSpeed((float)(difficulty*random.nextDouble())+maxSpeed);
        npcBoat.setRange(range);
        npcBoat.setIdealDistFromTarget(idealDistFromTarget);
        npcBoat.setGradientForNormalDist(gradientFromNormalDist);
        npcBoat.setHealth((float)(difficulty*random.nextDouble())+maxHealth);
        npcBoat.setMaxHealth(maxHealth);
        npcBoat.setTurningSpeed((int)Math.round(difficulty*random.nextDouble())+turningSpeed);
        npcBoat.setAllied(Optional.of(allied));
        npcBoat.setBoss(isBoss);
        npcBoat.setDamage(damage);
        npcBoat.setReqCooldown(reqCooldown);


        return npcBoat;
    }

}
