package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.projectile.ProjectileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Data
public class NPCBuilder {

    private float angle = 0f, speed, angularSpeed = 0f, maxSpeed = 100f, range = 500f, accuracy = 0.5f;
    private Double health = 20.0, maxHealth = 20.0;
    private List<ProjectileType> projectileTypes = new ArrayList<ProjectileType>();
    private boolean isDead = false, onFire = false, isAccelerating, isBraking = false;
    private Integer turningSpeed = 10;
    private Texture texture = TextureManager.ENEMY;
    private ProjectileType selectedProjectile;
    private College allied = null;

    public NPCBuilder(){}

    public NPCBoat buildEnemy(Integer id, Vector2 pos) {
        NPCBoat NPCBoat;
        if(allied  == null){
            NPCBoat = new NPCBoat(id, texture, angle, speed, maxSpeed, health, maxHealth, turningSpeed, onFire, projectileTypes, range, accuracy , true, Optional.empty());
        } else {
            NPCBoat = new NPCBoat(id, texture, angle, speed, maxSpeed, health, maxHealth, turningSpeed, onFire, projectileTypes, range, accuracy , true, Optional.of(allied));
        }
        NPCBoat.setX(pos.x);
        NPCBoat.setY(pos.y);

        if(selectedProjectile != null) {
            NPCBoat.setSelectedProjectileType(this.selectedProjectile);
            NPCBoat.addProjectileType(this.selectedProjectile);
        }
        return NPCBoat;
    }

    public NPCBoat buildNPC(Integer id, Vector2 pos) {
        NPCBoat NPCBoat = new NPCBoat(id, texture, angle, speed, maxSpeed, health, maxHealth, turningSpeed, onFire, projectileTypes, range, accuracy , false, Optional.empty());
        NPCBoat.setX(pos.x);
        NPCBoat.setY(pos.y);

        if(selectedProjectile != null) {
            NPCBoat.setSelectedProjectileType(this.selectedProjectile);
            NPCBoat.addProjectileType(this.selectedProjectile);
        }
        return NPCBoat;
    }

    public NPCBuilder allied(College allied) {
        this.allied = allied;
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

    public NPCBuilder selectedProjectile(ProjectileType projectileType) {
        this.selectedProjectile = projectileType;
        return this;
    }

    public NPCBuilder projectileTypes(List<ProjectileType> projectileTypes) {
        this.projectileTypes = projectileTypes;
        return this;
    }

    public NPCBuilder range(float range) {
        this.range =  range;
        return this;
    }

    public NPCBuilder accuracy(float accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public NPCBuilder health(Double health) {
        this.health = health;
        this.maxHealth = health;
        return this;
    }

    public NPCBoat generateRandomEnemy(Integer id, Vector2 pos, Double difficulty, List<ProjectileType> projectileTypes) {
        NPCBuilder builder = new NPCBuilder();
        Random random = new Random();
        builder.projectileTypes = projectileTypes;

        builder.maxSpeed((float)(difficulty*random.nextDouble())+maxSpeed);
        builder.turningSpeed((int)Math.round(difficulty*random.nextDouble())+turningSpeed);
        builder.health((float)(difficulty*random.nextDouble())+maxHealth);

        return builder.buildEnemy(id, pos);
    }

}
