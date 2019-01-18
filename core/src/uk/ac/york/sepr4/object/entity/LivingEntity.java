package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.screen.GameScreen;
import uk.ac.york.sepr4.screen.hud.HealthBar;

@Data
public abstract class LivingEntity extends Entity {

    private Double health = 20.0, maxHealth = 20.0, damage = 5.0;
    private boolean isAccelerating, isBraking, isDead, isDying;
    private Integer turningSpeed = 1, collided = 0;
    private float currentCooldown = 0f, reqCooldown = 0.5f, maxSpeed = 100f;
    private float angularSpeed = 0f;

    private float firingangle;

    private HealthBar healthBar;

    public LivingEntity(Texture texture, Vector2 pos) {
        super(texture, pos);

        this.healthBar = new HealthBar(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void kill(boolean silent) {
        //if not silent, act method will explode
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

        if (!this.isDying) {
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
            if (currentCooldown >= reqCooldown) {
                setCurrentCooldown(0f);
                GameScreen.getInstance().getEntityManager().getProjectileManager().spawnProjectile( this, getSpeed(), angle);
                setFiringangle(angle - (float)Math.PI/2);
                GameScreen.getInstance().getEntityManager().getAnimationManager().addFiringAnimation(this);
                return true;
            }

        return false;
    }
}
