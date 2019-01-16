package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.screen.GameScreen;
import uk.ac.york.sepr4.screen.hud.HealthBar;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class LivingEntity extends Entity {

    //REQUIRED
    private float maxSpeed = 100f;

    private Double health = 20.0, maxHealth = 20.0;
    private boolean isAccelerating, isBraking, isDead, isDying;
    private Integer turningSpeed = 1;

    private Double projectileDamage = 5.0;

    private Integer collided = 0;
    private ArrayList<Vector2> waterTrials1 = new ArrayList<>(), waterTrials2 = new ArrayList<>();

    private float currentCooldown = 0f, reqCooldown = 0.5f;

    private HealthBar healthBar;


    //Todo: Make a better collision detection

    public LivingEntity(Texture texture, Vector2 pos) {
        super(texture, pos);

        for (int i = 0; i < 60; i++) {
            this.waterTrials1.add(new Vector2(getCentre().x, getY()));
            this.waterTrials2.add(new Vector2(getCentre().x, getY()));
        }

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

        AnimationManager animationManager = GameScreen.getInstance().getEntityManager().getAnimationManager();

        if (!this.isDying) {
            float speed = getSpeed();

            if (isAccelerating) {
                Gdx.app.debug("LE", "Accel");

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

        arrayShiftForBoatTrails();
        this.waterTrials1.set(0, new Vector2(getXwithAngleandDistance(getCentre().x, (float) (getAngle() - 7 * Math.PI / 8), 50f), getYwithAngleandDistance(getCentre().y, (float) (getAngle() - 7 * Math.PI / 8), 45f)));
        this.waterTrials2.set(0, new Vector2(getXwithAngleandDistance(getCentre().x, (float) (getAngle() + 7 * Math.PI / 8), 50f), getYwithAngleandDistance(getCentre().y, (float) (getAngle() + 7 * Math.PI / 8), 45f)));

        for (int i = 0; i < this.waterTrials1.size() - 1; i++) {
            float xM = getXmidPoint(this.waterTrials1.get(i).x, this.waterTrials1.get(i + 1).x);
            float yM = getYmidPoint(this.waterTrials1.get(i).y, this.waterTrials1.get(i + 1).y);
            float angleP = getAngleToPoint(this.waterTrials1.get(i).x, this.waterTrials1.get(i).y, this.waterTrials1.get(i + 1).x, this.waterTrials1.get(i + 1).y) + (float)Math.PI/2;
            float distance = getDistanceToPoint(this.waterTrials1.get(i).x, this.waterTrials1.get(i).y, this.waterTrials1.get(i + 1).x, this.waterTrials1.get(i + 1).y);

            float xM2 = getXmidPoint(this.waterTrials2.get(i).x, this.waterTrials2.get(i + 1).x);
            float yM2 = getYmidPoint(this.waterTrials2.get(i).y, this.waterTrials2.get(i + 1).y);
            float angleP2 = getAngleToPoint(this.waterTrials2.get(i).x, this.waterTrials2.get(i).y, this.waterTrials2.get(i + 1).x, this.waterTrials2.get(i + 1).y) + (float)Math.PI/2;
            float distance2 = getDistanceToPoint(this.waterTrials2.get(i).x, this.waterTrials2.get(i).y, this.waterTrials2.get(i + 1).x, this.waterTrials2.get(i + 1).y);


            if (distance > 0.1) {
                if (i < this.waterTrials1.size() / 4) {
                    animationManager.addEffect(xM, yM, angleP, TextureManager.MIDDLEBOATTRAIL1, (int)(distance + 5), 10,0.5f);
                    animationManager.addEffect(xM2, yM2, angleP2,  TextureManager.MIDDLEBOATTRAIL1, (int)(distance2 + 5), 10,0.5f);
                } else if (i < this.waterTrials1.size() / 2) {
                    animationManager.addEffect(xM, yM, angleP,  TextureManager.MIDDLEBOATTRAIL1, (int)(distance + 5), 10,0.3f);
                    animationManager.addEffect(xM2, yM2, angleP2,  TextureManager.MIDDLEBOATTRAIL1, (int)(distance2 + 5), 10,0.3f);
                } else if (i < 3 * this.waterTrials1.size() / 4) {
                    animationManager.addEffect(xM, yM, angleP,  TextureManager.MIDDLEBOATTRAIL1, (int)(distance + 5), 10,0.2f);
                    animationManager.addEffect(xM2, yM2, angleP2,  TextureManager.MIDDLEBOATTRAIL1, (int)(distance2 + 5), 10,0.2f);
                } else {
                    animationManager.addEffect(xM, yM, angleP,  TextureManager.MIDDLEBOATTRAIL1, (int)(distance + 5), 10,0.1f);
                    animationManager.addEffect(xM2, yM2, angleP2, TextureManager.MIDDLEBOATTRAIL1, (int)(distance2 + 5), 10,0.1f);
                }
            }
        }


    }

    public float getXmidPoint(float x1, float x2) {
        if (x2 > x1){
            return (x1+(x2-x1)/2);
        } else {
            return (x1-(x2-x1)/2);
        }
    }
    public float getYmidPoint(float y1, float y2) {
        if (y2 > y1){
            return (y1+(y2-y1)/2);
        } else {
            return (y1-(y2-y1)/2);
        }
    }
    public float getAngleToPoint(float x1, float y1, float x2, float y2) {
        double d_angle = Math.atan(((y2 - y1) / (x2 - x1)));
        if(x2 < x1){
            d_angle += Math.PI;
        }
        float angle = (float)d_angle + (float)Math.PI/2;
        return angle;
    }

    public float getDistanceToPoint(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }
    public float getXwithAngleandDistance(float x1, float angle, float distance) {
        return (float)(x1 + distance*Math.sin(angle));
    }
    public float getYwithAngleandDistance(float y1, float angle, float distance) {
        return (float)(y1 - distance*Math.cos(angle));
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
                return true;
            }

        return false;
    }

    public void arrayShiftForBoatTrails() {
        for (int i = this.waterTrials1.size() - 1; i > 0; i--) {
            this.waterTrials1.set(i, this.waterTrials1.get(i-1));
            this.waterTrials2.set(i, this.waterTrials2.get(i-1));
        }
    }
}
