package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.screen.GameScreen;
import uk.ac.york.sepr4.screen.hud.HealthBar;
import uk.ac.york.sepr4.object.projectile.ProjectileType;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class LivingEntity extends Entity {

    private List<ProjectileType> projectileTypes;
    private Double health, maxHealth;
    private boolean isDead, onFire, isDying;
    private float maxSpeed;
    private boolean isAccelerating, isBraking;
    private Integer turningSpeed;

    //Added by harry
    private Integer collided;


    private ProjectileType selectedProjectileType;
    private float currentCooldown;

    private HealthBar healthBar;

    private float deathTimer = -1f;

    public LivingEntity(Integer id, Texture texture) {
        this(id, texture, 0f, 0f, 100f,20.0, 20.0,   1, false, new ArrayList<ProjectileType>());
    }

    //Todo: Make a better collision detection

    public LivingEntity(Integer id, Texture texture, float angle, float speed, float maxSpeed, Double health, Double maxHealth, Integer turningSpeed, boolean onFire, List<ProjectileType> projectileTypes) {
        super(id, texture, angle, speed);

        this.onFire = onFire;
        this.projectileTypes = projectileTypes;
        this.health = health;
        this.maxHealth = maxHealth;
        this.isDead = false;
        this.currentCooldown = 0f;
        this.maxSpeed = maxSpeed;
        this.turningSpeed = turningSpeed;

        this.collided = 0;

        this.healthBar = new HealthBar(this);
        this.isDying = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        //TODO: Draw bow wave?
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
            setTexture(TextureManager.DEADENEMY);
            if (deathTimer < 1/6f) {
                GameScreen.getInstance().getEntityManager().addEffect(getCentre().x, getCentre().y, getAngle(), 0f, TextureManager.EXPLOSION1, 40, 40);
            } else if (deathTimer < 2/6f) {
                GameScreen.getInstance().getEntityManager().addEffect(getCentre().x, getCentre().y, getAngle(), 0f, TextureManager.EXPLOSION2, 40, 40);
            } else if (deathTimer < 1/2f){
                GameScreen.getInstance().getEntityManager().addEffect(getCentre().x, getCentre().y, getAngle(), 0f, TextureManager.EXPLOSION3, 40, 40);
            }
            if (deathTimer > 5){
                this.isDead = true;
                this.isDying = false;
            }

            deathTimer += deltaTime;
            if (getSpeed() > 0) {
                setSpeed(getSpeed() -  40f * deltaTime);
            }
            super.act(deltaTime);

        } else {

            float speed = getSpeed();

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
            setSpeed(speed);

            super.act(deltaTime);
        }

    }

    public float getAngleTowardsLE(LivingEntity livingEntity) {
        double d_angle = Math.atan(((livingEntity.getCentre().y - getCentre().y) / (livingEntity.getCentre().x - getCentre().x)));
        if(livingEntity.getCentre().x < getCentre().x){
            d_angle += Math.PI;
        }
        float angle = (float)d_angle + (float)Math.PI/2;
        return angle;
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

    public boolean fire(float angle) {
        ProjectileType projectileType = this.getSelectedProjectileType();
        if(projectileType != null) {
            if (projectileType.getCooldown() <= getCurrentCooldown()) {
                setCurrentCooldown(0f);
                GameScreen.getInstance().getProjectileManager().spawnProjectile(projectileType, this, getSpeed(), angle);
                return true;
            }
        }
        return false;
    }

    //ToDo:Make this function more accurate
    public boolean goingToCollide(Entity object){
        double pred_X = getX() + getSpeed()*Math.sin(getAngle());
        double pred_Y = getY() - getSpeed()*Math.cos(getAngle());
        Rectangle pred_Bounds = new Rectangle((float)pred_X, (float)pred_Y, getWidth()/2, getHeight()/2);
        if(object.getBounds().overlaps(pred_Bounds)){
            return true;
        }
        return false;
    }

}
