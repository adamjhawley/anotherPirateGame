package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.projectile.Projectile;
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
    private ArrayList<Float> waterTrialsX;
    private ArrayList<Float> waterTrialsY;
    private ArrayList<Float> waterTrialsX2;
    private ArrayList<Float> waterTrialsY2;

    //Added by harry
    public Array<Float> xForCollision;
    public Array<Float> yForCollision;

    private ProjectileType selectedProjectileType;
    private float currentCooldown;

    private HealthBar healthBar;

    public LivingEntity(Integer id, Texture texture) {
        this(id, texture, 0f, 0f, 100f, 20.0, 20.0, 1, false, new ArrayList<ProjectileType>());
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
        this.waterTrialsX = new ArrayList<Float>();
        this.waterTrialsY = new ArrayList<Float>();
        this.waterTrialsX2 = new ArrayList<Float>();
        this.waterTrialsY2 = new ArrayList<Float>();

        for (int i = 0; i < 60; i++) {
            this.waterTrialsX.add(getCentre().x);
            this.waterTrialsY.add(getCentre().y);

            this.waterTrialsX2.add(getCentre().x);
            this.waterTrialsY2.add(getCentre().y);
        }

        this.xForCollision = new Array<Float>();
        this.yForCollision = new Array<Float>();

        this.healthBar = new HealthBar(this);
        this.isDying = false;
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
        this.waterTrialsX.set(0, getXwithAngleandDistance(getCentre().x, (float) (getAngle() - 7 * Math.PI / 8), 50f));
        this.waterTrialsY.set(0, getYwithAngleandDistance(getCentre().y, (float) (getAngle() - 7 * Math.PI / 8), 45f));

        this.waterTrialsX2.set(0, getXwithAngleandDistance(getCentre().x, (float) (getAngle() + 7 * Math.PI / 8), 50f));
        this.waterTrialsY2.set(0, getYwithAngleandDistance(getCentre().y, (float) (getAngle() + 7 * Math.PI / 8), 45f));

        for (int i = 0; i < this.waterTrialsX.size() - 1; i++) {
            float xM = getXmidPoint(this.waterTrialsX.get(i), this.waterTrialsX.get(i + 1));
            float yM = getYmidPoint(this.waterTrialsY.get(i), this.waterTrialsY.get(i + 1));
            float angleP = getAngleToPoint(this.waterTrialsX.get(i), this.waterTrialsY.get(i), this.waterTrialsX.get(i + 1), this.waterTrialsY.get(i + 1)) + (float)Math.PI/2;
            float distance = getDistanceToPoint(this.waterTrialsX.get(i), this.waterTrialsY.get(i), this.waterTrialsX.get(i + 1), this.waterTrialsY.get(i + 1));

            float xM2 = getXmidPoint(this.waterTrialsX2.get(i), this.waterTrialsX2.get(i + 1));
            float yM2 = getYmidPoint(this.waterTrialsY2.get(i), this.waterTrialsY2.get(i + 1));
            float angleP2 = getAngleToPoint(this.waterTrialsX2.get(i), this.waterTrialsY2.get(i), this.waterTrialsX2.get(i + 1), this.waterTrialsY2.get(i + 1)) + (float)Math.PI/2;
            float distance2 = getDistanceToPoint(this.waterTrialsX2.get(i), this.waterTrialsY2.get(i), this.waterTrialsX2.get(i + 1), this.waterTrialsY2.get(i + 1));


            if (distance > 0.1) {
                if (i < this.waterTrialsX.size() / 4) {
                    animationManager.addEffect(xM, yM, angleP, 0f, TextureManager.MIDDLEBOATTRAIL1, (int)(distance + 5), 10,0.5f);
                    animationManager.addEffect(xM2, yM2, angleP2, 0f, TextureManager.MIDDLEBOATTRAIL1, (int)(distance2 + 5), 10,0.5f);
                } else if (i < this.waterTrialsX.size() / 2) {
                    animationManager.addEffect(xM, yM, angleP, 0f, TextureManager.MIDDLEBOATTRAIL1, (int)(distance + 5), 10,0.3f);
                    animationManager.addEffect(xM2, yM2, angleP2, 0f, TextureManager.MIDDLEBOATTRAIL1, (int)(distance2 + 5), 10,0.3f);
                } else if (i < 3 * this.waterTrialsX.size() / 4) {
                    animationManager.addEffect(xM, yM, angleP, 0f, TextureManager.MIDDLEBOATTRAIL1, (int)(distance + 5), 10,0.2f);
                    animationManager.addEffect(xM2, yM2, angleP2, 0f, TextureManager.MIDDLEBOATTRAIL1, (int)(distance2 + 5), 10,0.2f);
                } else {
                    animationManager.addEffect(xM, yM, angleP, 0f, TextureManager.MIDDLEBOATTRAIL1, (int)(distance + 5), 10,0.1f);
                    animationManager.addEffect(xM2, yM2, angleP2, 0f, TextureManager.MIDDLEBOATTRAIL1, (int)(distance2 + 5), 10,0.1f);
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
        if (projectileType != null) {
            if (projectileType.getCooldown() <= getCurrentCooldown()) {
                setCurrentCooldown(0f);
                GameScreen.getInstance().getEntityManager().getProjectileManager().spawnProjectile(projectileType, this, getSpeed(), angle);
                return true;
            }
        }
        return false;
    }

    //TODO:Make this function more accurate
    public boolean goingToCollide(Entity object) {
        double pred_X = getX() + getSpeed() * Math.sin(getAngle());
        double pred_Y = getY() - getSpeed() * Math.cos(getAngle());
        Rectangle pred_Bounds = new Rectangle((float) pred_X, (float) pred_Y, getWidth(), getHeight());
        if (object.getRectBounds().overlaps(pred_Bounds)) {
            return true;
        }
        return false;
    }

    public void arrayShiftForBoatTrails() {
        for (int i = this.waterTrialsY.size() - 1; i > 0; i--) {
            this.waterTrialsY.set(i, this.waterTrialsY.get(i - 1));
            this.waterTrialsX.set(i, this.waterTrialsX.get(i - 1));

            this.waterTrialsY2.set(i, this.waterTrialsY2.get(i - 1));
            this.waterTrialsX2.set(i, this.waterTrialsX2.get(i - 1));
        }
    }

    public void addX(float x){
        this.xForCollision.add(x);
    }

    public void addY(float y){
        this.yForCollision.add(y);
    }

    public void deleteX(){
        this.xForCollision = new Array<Float>();
    }

    public void deleteY(){
        this.yForCollision = new Array<Float>();
    }
}
