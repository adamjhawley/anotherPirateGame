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
    private ArrayList<Float> waterTrialsX;
    private ArrayList<Float> waterTrialsY;
    private ArrayList<Float> waterTrailsAngle;
    private Integer delag;

    private ProjectileType selectedProjectileType;
    private float currentCooldown;

    private HealthBar healthBar;

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
        this.waterTrialsX = new ArrayList<Float>();
        this.waterTrialsY = new ArrayList<Float>();
        this.waterTrailsAngle = new ArrayList<Float>();
        this.delag = 0;

        for(int i = 0; i<10; i++) {
            this.waterTrialsX.add(getCentre().x);
            this.waterTrialsY.add(getCentre().y);
            this.waterTrailsAngle.add(getAngle());
        }


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

        //TODO: Very unsure about this logic
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

            //Water trails behind things in the water that move
            //need to add collisions with islands
            if(delag == 0) {
                arrayShiftForBoatTrails();
                this.waterTrialsY.set(0, getCentre().y);
                this.waterTrialsX.set(0, getCentre().x);
                this.waterTrailsAngle.set(0, getAngle() - (float)Math.PI);
                delag = 20;
            }
            delag -= 1;


            for(int i = 0; i<this.waterTrialsX.size()-1; i++){
                if (!getRectBounds().contains(this.waterTrialsX.get(i), this.waterTrialsY.get(i))){
                    animationManager.addEffect(this.waterTrialsX.get(i) + 240*i*deltaTime*(float)Math.sin(this.waterTrailsAngle.get(i) - Math.PI/2), this.waterTrialsY.get(i) - 240*i*deltaTime*(float)Math.cos(this.waterTrailsAngle.get(i) - Math.PI/2), this.waterTrailsAngle.get(i), 0, TextureManager.MIDDLEBOATTRAIL, 20, 50);
                    animationManager.addEffect(this.waterTrialsX.get(i) + 240*i*deltaTime*(float)Math.sin(this.waterTrailsAngle.get(i) + Math.PI/2), this.waterTrialsY.get(i) - 240*i*deltaTime*(float)Math.cos(this.waterTrailsAngle.get(i) + Math.PI/2), this.waterTrailsAngle.get(i), 0, TextureManager.MIDDLEBOATTRAIL, 20, 50);
                }
            }
            int endI = this.waterTrialsX.size()-1;
            if (!getRectBounds().contains(this.waterTrialsX.get(endI), this.waterTrialsY.get(endI))) {
                animationManager.addEffect(this.waterTrialsX.get(endI) + +240 * (endI - 1) * deltaTime * (float) Math.sin(this.waterTrailsAngle.get(endI) - Math.PI / 2), this.waterTrialsY.get(endI) - 240 * (endI - 1) * deltaTime * (float) Math.cos(this.waterTrailsAngle.get(endI) - Math.PI / 2), this.waterTrailsAngle.get(endI), 0, TextureManager.ENDBOATTRAIL, 20, 50);
                animationManager.addEffect(this.waterTrialsX.get(endI) + +240 * (endI - 1) * deltaTime * (float) Math.sin(this.waterTrailsAngle.get(endI) + Math.PI / 2), this.waterTrialsY.get(endI) - 240 * (endI - 1) * deltaTime * (float) Math.cos(this.waterTrailsAngle.get(endI) + Math.PI / 2), this.waterTrailsAngle.get(endI), 0, TextureManager.ENDBOATTRAIL, 20, 50);
            }
        }
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

    public boolean fire(float angle) {
        ProjectileType projectileType = this.getSelectedProjectileType();
        if(projectileType != null) {
            if (projectileType.getCooldown() <= getCurrentCooldown()) {
                setCurrentCooldown(0f);
                GameScreen.getInstance().getEntityManager().getProjectileManager().spawnProjectile(projectileType, this, getSpeed(), angle);
                return true;
            }
        }
        return false;
    }

    //ToDo:Make this function more accurate
    public boolean goingToCollide(Entity object){
        double pred_X = getX() + getSpeed()*Math.sin(getAngle());
        double pred_Y = getY() - getSpeed()*Math.cos(getAngle());
        Rectangle pred_Bounds = new Rectangle((float)pred_X, (float)pred_Y, getWidth(), getHeight());
        if(object.getRectBounds().overlaps(pred_Bounds)){
            return true;
        }
        return false;
    }

    public void arrayShiftForBoatTrails(){
        for(int i = this.waterTrialsY.size() - 1; i>0; i--){
            this.waterTrialsY.set(i, this.waterTrialsY.get(i-1));
            this.waterTrialsX.set(i, this.waterTrialsX.get(i-1));
            this.waterTrailsAngle.set(i, this.waterTrailsAngle.get(i - 1));
        }
    }

}
