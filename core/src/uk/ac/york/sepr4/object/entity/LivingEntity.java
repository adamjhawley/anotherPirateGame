package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import lombok.Data;
import uk.ac.york.sepr4.object.entity.projectile.Projectile;
import uk.ac.york.sepr4.object.entity.projectile.ProjectileType;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class LivingEntity extends Entity {

    private List<ProjectileType> projectileTypes;
    private Double health, maxHealth;
    private boolean isDead, onFire;
    private float angularSpeed, maxSpeed;
    private boolean isAccelerating, isBraking;
    private Integer turningSpeed;

    private ProjectileType selectedProjectileType;
    private float currentCooldown;

    public LivingEntity(Integer id, Texture texture){
        this(id, 0f, 0f, 20.0, 20.0, texture, 100f, 1, false, new ArrayList<ProjectileType>());
    }

    public LivingEntity(Integer id, float angle, float speed, Double health, Double maxHealth, Texture texture, float maxSpeed, Integer turningSpeed, boolean onFire, List<ProjectileType> projectileTypes) {
        super(id, angle, speed, texture);

        this.onFire = onFire;
        this.projectileTypes = projectileTypes;
        this.health = health;
        this.maxHealth = maxHealth;
        this.isDead = false;
        this.currentCooldown = 0f;
        this.maxSpeed = maxSpeed;
        this.turningSpeed = turningSpeed;
    }

    @Override
    public void act(float deltaTime) {
        setCurrentCooldown(getCurrentCooldown()+deltaTime);

        float speed = getSpeed();
        float angle = getAngle();

        if(isAccelerating) {
            if (speed > maxSpeed) {
                speed = maxSpeed;
            } else {
                speed += 40f * deltaTime;
            }
        } else if(isBraking) {
            if(speed > 0) {
                speed -= 80f * deltaTime;
            }
        } else {
            if(speed > 0) {
                speed -= 20f * deltaTime;
            }
        }


        angle += (angularSpeed * deltaTime) * (speed/maxSpeed);

        setSpeed(speed);
        setAngle(angle);


        super.act(deltaTime);

    }

    public void addProjectileType(ProjectileType projectileType) {
        this.projectileTypes.add(projectileType);
    }

    public abstract void fire(float angle);
}
