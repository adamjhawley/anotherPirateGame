package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.projectile.Projectile;
import uk.ac.york.sepr4.screen.GameScreen;
import uk.ac.york.sepr4.utils.AIUtil;
import java.util.Optional;
import java.util.Random;

@Data
public class NPCBoat extends LivingEntity {

    //NPCBoat-specific variables
    private float range = 500f;
    private float accuracy = 0.5f;

    private float idealDistFromTarget = 250f;
    private float gradientForNormalDist = 50f;

    private Optional<College> allied = Optional.empty();
    private Optional<LivingEntity> lastTarget = Optional.empty();

    private boolean prevoiusTurn = true;
    private boolean turning = false;

    private int dodging = 0;
    private int randomForceAppl = 0;
    private float randomForce = 0f;
    private float randomAngle = 0f;

    private boolean died = false;
    private int peopleoverboardanimationtime = 0;
    private int swimminganimation;
    private double alphaswimming;

    private Random r = new Random();

    private float targetCheck = 3f;

    private boolean isBoss;

    public NPCBoat(Texture texture, Vector2 pos) {
        super(texture, pos);
    }

    /***
     *
     * @param deltaTime
     */
    public void act(float deltaTime) {
        Array<Float> forces = new Array<>();
        Array<Float> angles = new Array<>();

        if (!this.isDying()) {
            //TARGET CHECK***************
            // timer to check for new target (expensive if done every tick)
            if (targetCheck < 4f) {
                targetCheck += deltaTime;
            }
            Optional<LivingEntity> optionalTarget = getTarget();
            if (optionalTarget.isPresent()) {
                LivingEntity target = optionalTarget.get();
                this.lastTarget = optionalTarget;
            //***************************


                //FORCES WANTED TO BE COMPUTED***************
                float f = AIUtil.normalDistFromMean((float) this.distanceFrom(target), this.gradientForNormalDist, this.idealDistFromTarget);

                //Forces due to the target**
                if ((float) this.distanceFrom(target) < this.idealDistFromTarget) {
                    forces.add(1 - f);
                    angles.add(AIUtil.convertToRealAngle(this.getAngleTowardsEntity(target)));
                } else {
                    forces.add(1 - f);
                    angles.add(AIUtil.convertToRealAngle(this.getAngleTowardsEntity(target) - (float) Math.PI));
                }
                forces.add(f);
                angles.add(AIUtil.convertToRealAngle(target.getAngle() - (float) Math.PI));
                //**

                //Forces due to the other living entitys**
                for (LivingEntity livingentity : getLivingEntitesInRangeMinusTarget(target)){
                    float n = AIUtil.normalDistFromMean((float) this.distanceFrom(livingentity), 50, 200);
                    if ((float) this.distanceFrom(livingentity) < 200) {
                        forces.add((1 - n)/2);
                        angles.add(AIUtil.convertToRealAngle(this.getAngleTowardsEntity(livingentity)- (float) Math.PI));
                    } else {
                        forces.add((1 - n)/2);
                        angles.add(AIUtil.convertToRealAngle(this.getAngleTowardsEntity(livingentity)));
                    }
                }
                //**

                //********************************************


                //RESULTANT ANGLE*****************
                float ang = AIUtil.resultantForce(angles, forces).get(1);
                //********************************


                //NO DUMB MOVE CHECK**************
                float wantedAngle = ang; //change
                //********************************


                //SPEED STUFF*****************
                if (this.dodging == 0) {
                    float NormalFactor = Math.min(AIUtil.normalDistFromMean(AIUtil.angleDiffrenceBetweenTwoAngles(AIUtil.convertToRealAngle(target.getAngle()), getAngleTowardsEntity(target)), (float) Math.PI / 8, (float) Math.PI / 2) * 100, 1f);
                    float A;
                    if (target.getSpeed() > getMaxSpeed()) {
                        A = getMaxSpeed();
                    } else {
                        A = target.getSpeed();
                    }
                    float idealSpeed = (1 - f) * getMaxSpeed() + ((f + NormalFactor) / 2) * A;
                    if (idealSpeed > getSpeed()) {
                        setAccelerating(true);
                        setBraking(false);
                    } else {
                        if (getSpeed() / 5 > idealSpeed) {
                            setBraking(true);
                            setAccelerating(false);
                        } else {
                            setAccelerating(false);
                            setBraking(false);
                        }
                    }
                } else {
                    setAccelerating(false);
                    setBraking(true);
                    this.dodging -= 1;
                }
                //****************************


                //DODGE STARTER*****************
                if (getProjectilesToDodge(getProjectilesInRange()).size > 0){
                    float prob = 1f*getProjectilesToDodge(getProjectilesInRange()).size;
                    float random = r.nextFloat() * 100f;
                    if (random < prob){
                        setDodging(100);
                        Gdx.app.log("Doging","");
                    }
                } else {
                    if (getDodging() > 0){
                        setDodging(10);
                    }
                }
                //******************************


                //TURN ACTION*******************
                if (AIUtil.angleDiffrenceBetweenTwoAngles(getAngle(), wantedAngle) < Math.PI/16){
                    this.turning = false;
                } else {
                    this.turning = true;
                }
                turnRight(AIUtil.rightForAngleDiffrenceBetweenTwoAngles(getAngle(), wantedAngle));
                this.prevoiusTurn = AIUtil.rightForAngleDiffrenceBetweenTwoAngles(getAngle(), wantedAngle);
                setAngle(getAngle() + (getAngularSpeed() * deltaTime) * (getSpeed() / getMaxSpeed()) % (float)(2*Math.PI));
                //******************************

                
                //FIRING************************
                if (target.getSpeed() <  target.getMaxSpeed()/5){
//                    boolean NotHitting = true;
//                    for (LivingEntity livingentity : getLivingEntitesInRangeMinusTarget(target)){
//                        if (AIUtil.angleDiffrenceBetweenTwoAngles(getAngleTowardsEntity(target), AIUtil.perfectAngleToCollide(this, target, 100)) < Math.PI/16){
//                            NotHitting = false;
//                        }
//                    }
//                    if (NotHitting == false){
//                        fire(getAngleTowardsEntity(target));
//                    }
                    fire(getAngleTowardsEntity(target));
                } else {
                    if (AIUtil.timeForPerfectAngleToCollide(this, target, AIUtil.thetaForAngleDiffrence(AIUtil.convertToRealAngle(target.getAngle()), getAngleTowardsEntity(target)) , 100) < 3){
//                        boolean NotHitting = true;
//                        for (LivingEntity livingentity : getLivingEntitesInRangeMinusTarget(target)){
//                            if (AIUtil.angleDiffrenceBetweenTwoAngles(getAngleTowardsEntity(target), AIUtil.perfectAngleToCollide(this, target, 100)) < Math.PI/16){
//                                NotHitting = false;
//                            }
//                        }
//                        if (NotHitting == false){
//                            fire(AIUtil.perfectAngleToCollide(this, target, 100));
//                        }
                        fire(AIUtil.perfectAngleToCollide(this, target, 100));
                    }
                }
                //******************************
            } else {
                //PATROL**********************
                setAccelerating(false);
                this.dodging = 0;
                //TODO: Pursue for a bit if had a previous target, then stop moving
                //****************************
            }
        }
        super.act(deltaTime);
    }

    private boolean validTarget(Optional<LivingEntity> optionalLivingEntity) {
        if (optionalLivingEntity.isPresent()) {
            LivingEntity livingEntity = optionalLivingEntity.get();
            if (!(livingEntity.isDying() || livingEntity.isDead())) {
                if (livingEntity.distanceFrom(this) <= range) {
                    //if last target exists, not dead and is still in range
                    return true;
                }
            }
        }
        return false;
    }

    private Optional<LivingEntity> getTarget() {
        if (validTarget(this.lastTarget)) {
            //Gdx.app.debug("Target", "Last");
            return this.lastTarget;
        } else {
            this.lastTarget = Optional.empty();
            if (targetCheck > 4f) {
                //Gdx.app.debug("Target", "Nearest");
                targetCheck = 0f;
                return getNearestTarget();
            } else {
                return Optional.empty();
            }
        }
    }

    private boolean areAllied(LivingEntity livingEntity) {
        if (livingEntity instanceof Player) {
            Player player = (Player) livingEntity;
            if (getAllied().isPresent()) {
                if (getHealth() < 3*getMaxHealth()/4){
                    return false;
                }
                return player.getCaptured().contains(getAllied().get());
            }
        } else {
            //must be an NPCBoat
            NPCBoat npcBoat = (NPCBoat) livingEntity;
            if (npcBoat.getAllied().isPresent() && getAllied().isPresent()) {
                return (npcBoat.getAllied().get().equals(getAllied().get()));
            }
        }

        return false;
    }

    private Array<LivingEntity> getLivingEntitesInRangeMinusTarget(LivingEntity target) {
        Array<LivingEntity> nearby = getLivingEntitiesInRange();
        if (nearby.contains(target, false)){
            nearby.removeValue(target, false);
        }
        return nearby;
    }

    private Array<LivingEntity> getLivingEntitiesInRange() {
        Array<LivingEntity> nearby = GameScreen.getInstance().getEntityManager().getLivingEntitiesInArea(getRangeArea());
        if (nearby.contains(this, false)) {
            nearby.removeValue(this, false);
        }
        return nearby;
    }

    private Array<Projectile> getProjectilesInRange() {
        Array<Projectile> nearby = GameScreen.getInstance().getEntityManager().getProjectileManager().getProjectileInArea(getRangeArea());
        return nearby;
    }

    private Array<Entity> getProjectilesToDodge(Array<Projectile> projectiles) {
        Array<Entity> projectilesToDodge = new Array<Entity>();
        for (Projectile projectile : projectiles) {
            float thetaToThisInFuture = AIUtil.perfectAngleToCollide(projectile, this, projectile.getSpeed());
            float thetaActual = AIUtil.convertToRealAngle(projectile.getAngle());
            float dist = (float) projectile.distanceFrom(this);
            boolean isTriangle = true;
            float theta;
            if (thetaToThisInFuture < thetaActual && thetaActual - thetaToThisInFuture < Math.PI / 2) {
                theta = thetaActual - thetaToThisInFuture;
            } else if (thetaActual < thetaToThisInFuture && thetaToThisInFuture - thetaActual < Math.PI / 2) {
                theta = thetaToThisInFuture - thetaActual;
            } else if (thetaActual < thetaToThisInFuture && (2 * Math.PI - thetaToThisInFuture) + thetaActual < Math.PI / 2) {
                theta = (float) (2 * Math.PI - thetaToThisInFuture) + thetaActual;
            } else if (thetaToThisInFuture < thetaActual && (2 * Math.PI - thetaActual) + thetaToThisInFuture < Math.PI / 2) {
                theta = (float) (2 * Math.PI - thetaActual) + thetaToThisInFuture;
            } else {
                theta = 0;
                isTriangle = false;
            }

            if (isTriangle == true) {
                float opp = (float) Math.tan(theta) * dist;
                if (opp < 0) {
                    opp = -opp;
                }
                if (opp < Math.max(3 * this.getRectBounds().height / 4, 3 * this.getRectBounds().width / 4)) {
                    projectilesToDodge.add(projectile);
                }
            }

        }
        return projectilesToDodge;
    }

    private Optional<LivingEntity> getNearestTarget() {
        Player player = GameScreen.getInstance().getEntityManager().getOrCreatePlayer();
        Array<LivingEntity> nearby = getLivingEntitiesInRange();
        if (!areAllied(player)) {
            //not allied - target player
            if (nearby.contains(player, false)) {
                //if player is in range - target
                //Gdx.app.debug("NPCBoat", "Got nearby player");

                return Optional.of(player);
            }
        }
        //player has captured this NPC's allied college
        if (nearby.size > 0) {
            Optional<LivingEntity> nearest = Optional.empty();
            for (LivingEntity livingEntity : nearby) {
                if (!areAllied(livingEntity)) {
                    if (nearest.isPresent()) {
                        if (nearest.get().distanceFrom(this) > livingEntity.distanceFrom(this)) {
                            //closest enemy
                            nearest = Optional.of(livingEntity);
                            //Gdx.app.debug("NPCBoat", "Got closer nearby enemy");

                        }
                    } else {
                        nearest = Optional.of(livingEntity);
                        //Gdx.app.debug("NPCBoat", "Got new nearby enemy");
                    }
                }
            }
            return nearest;
        }
        //Gdx.app.debug("NPCBoat", "No nearby enemy");
        return Optional.empty();
    }

    private Rectangle getRangeArea() {
        Rectangle radius = getRectBounds();
        radius.set(radius.x - range, radius.y - range, radius.width + 2 * range, radius.height + 2 * range);
        return radius;
    }

    private void turnRight(boolean right){
        if (this.prevoiusTurn == true && right == false || this.prevoiusTurn == false && right == true || this.turning == false){
            setAngularSpeed(0);
        } else {
            if(right){
                setAngularSpeed(-getTurningSpeed());
            } else {
                setAngularSpeed(getTurningSpeed());
            }
        }
    }

}
