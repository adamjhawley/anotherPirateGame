package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.screen.hud.HealthBar;
import uk.ac.york.sepr4.object.projectile.ProjectileType;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class LivingEntity extends Entity {

    private List<ProjectileType> projectileTypes;
    private Double health, maxHealth;
    private boolean isDead, onFire, isDying;
    private float angularSpeed, maxSpeed;
    private boolean isAccelerating, isBraking;
    private Integer turningSpeed;

    private ProjectileType selectedProjectileType;
    private float currentCooldown;

    private HealthBar healthBar;

    private float deathTimer = -1f;

    public LivingEntity(Integer id, Texture texture) {
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

        this.healthBar = new HealthBar(this);
        this.isDying = false;
    }

    public void kill(boolean silent) {
        //if not silent, act method will explode
        this.deathTimer = 0f;
        this.isDying = !silent;
        this.isDead = silent;
    }

    public HealthBar getHealthBar() {
        this.healthBar.update();
        return this.healthBar;
    }

    @Override
    public void act(float deltaTime) {
        setCurrentCooldown(getCurrentCooldown() + deltaTime);

        //TODO: Very unsure about this logic
        if (this.isDying) {
            //death animation started
            if (deathTimer < 1/6f) {
                //for some reason the centre here is incorrect (explosion flies away)
                //setX(getCentre().x);
                //setY(getCentre().y);
                setTexture(TextureManager.EXPLOSION1);
                setWidth(40);
                setHeight(40);

            } else if (deathTimer < 2/6f) {
                setTexture(TextureManager.EXPLOSION2);
            } else {
                setTexture(TextureManager.EXPLOSION3);
            }
            if (deathTimer > 1/2f) {
                this.isDead = true;
                this.isDying = false;
            }
            deathTimer += deltaTime;
        }

        float speed = getSpeed();
        float angle = getAngle();

        if (isAccelerating) {
            if (speed > maxSpeed) {
                speed = maxSpeed;
            } else {
                speed += 40f * deltaTime;
            }
        } else if (isBraking) {
            if (speed > 0) {
                speed -= 80f * deltaTime;
            }
        } else {
            if (speed > 0) {
                speed -= 20f * deltaTime;
            }
        }


        angle += (angularSpeed * deltaTime) * (speed / maxSpeed);

        setSpeed(speed);
        setAngle(angle);


        super.act(deltaTime);

    }

    public void addProjectileType(ProjectileType projectileType) {
        this.projectileTypes.add(projectileType);
    }

    //return true if still alive
    public boolean damage(Double damage) {
        this.health = this.health - damage;
        if (this.health <= 0) {
            kill(false);
            return false;
        }
        return true;
    }

    public abstract void fire(float angle);
}
