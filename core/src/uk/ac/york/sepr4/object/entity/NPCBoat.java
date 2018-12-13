package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import lombok.Getter;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.projectile.Projectile;
import uk.ac.york.sepr4.object.projectile.ProjectileType;
import uk.ac.york.sepr4.screen.GameScreen;
import uk.ac.york.sepr4.utils.AIUtil;

import java.util.List;
import java.util.Optional;
import java.util.zip.DeflaterOutputStream;

@Data
public class NPCBoat extends LivingEntity {

    //NPCBoat-specific variables
    private float range;
    private float accuracy;

    private boolean hostile;
    private College allied;
    private Optional<LivingEntity> lastTarget;

    private float targetCheck;

    //public NPCBoat(Integer id, Texture texture) {
    //    super(id, texture);
    //}

    public NPCBoat(Integer id, Texture texture, float angle, float speed, float maxSpeed, Double health, Double maxHealth, Integer turningSpeed, boolean onFire, List<ProjectileType> projectileTypes, float range, float accuracy, boolean hostile, College allied) {
        super(id, texture, angle, speed, maxSpeed, health, maxHealth, turningSpeed, onFire, projectileTypes);
        this.range = range;
        this.accuracy = accuracy;
        this.hostile = hostile;
        this.allied = allied;

        this.lastTarget = Optional.empty();
        this.targetCheck = 4f;
    }

    /**
     * Overriding Actor's 'act' function.
     * Calculates the direction of travel for the enemy depending on the location of the player.
     *
     * @param deltaTime
     */

    //Todo: Make a hostility thing using specficIdsHostileTo HostileToFactions sort of thing

    //Todo: Make a better collision protocal for enemy specfic

    //Todo: Create something that can give a array of all entitys within a certain distance to the living entity - Can also be used in player to check for collsions
    //Todo: The samething but with projectiles this will only be used for within enemy i think though
    //Todo: Add a dodge function that takes a single projectile and checks if it will hit and how much time it will take to hit

    //Todo: Use all the functions to create a better act function to actually give the AI a good feel to the game
    public void act(float deltaTime) {

//        this.deleteX();
//        this.deleteY();
//        if (getProjectilesToDodge(getProjectilesInRange()).size == 0){
//        }
        //When callng for dodges make sure to remeber to clean up the array in living entity because otherwise it will keep them because they wont be deleted
        //possible make it more efficent so only test projectiles that have already been checked

        if (getProjectilesToDodge(getProjectilesInRange()).size > 0) {
            Gdx.app.log("hit", "");
        }
        if(!this.isDying()) {
            if (this.hostile) {
                if (targetCheck < 5f) {
                    targetCheck += deltaTime;
                }
                Optional<LivingEntity> optionalTarget = getTarget();
                if (optionalTarget.isPresent()) {
                    LivingEntity target = optionalTarget.get();
                    this.lastTarget = optionalTarget;
                    //Todo: Finish the control logic of enemy to make it better
                    //******************Think about during coding of AI fire logic
                    //Weapon ready
                    //Fired how long ago
                    //How long the shot would take
                    //always fire at target
                    //Dont fire if going to hit allie

                    //********************Think about in AI's movement
                    //*****
                    //On collsion course with target
                    //on collsion course with island
                    //need to dodge
                    //*****
                    //smoothen the turns
                    //try to keep moving
                    //keep away from hostiles except for target so if can stay away from them
                    //stay sort of close towards allies

                    //******************Special conditions
                    //If going to collide with object try to hit side on with less speed (diffrence between them)
                    //If colldided try to get away from the collider
                    //Stunned
                    //Force action - From like items

                    //******************Things to make AI winnable
                    //Accuracy
                    //Chance to do actions like above
                    //special conditions have to do no matter what - possibilty of fireing when in special conditions





//                    float resAngle = AIUtil.resultantAngle(this, target);
//                    resAngle = AIUtil.convertToRealAngle(resAngle);
//                    setAngle(resAngle);
//
//                    float f = AIUtil.f(this, target);

//                    if ((1 - f) - f > 0.2) {
//                        setAccelerating(true);
//                        setBraking(false);
//                    } else if (((1 - f) - f > -0.2)) {
//                        setAccelerating(false);
//                        setBraking(false);
//                    } else {
//                        setBraking(false);
//                        setAccelerating(true);
//                    }
//                    if (getSpeed() < getMaxSpeed() / 5) {
//                        setAccelerating(true);
//                        setBraking(false);
//                    }
//                    if (goingToCollide(target)) {
//                        setCollided(100);
//                    }
//                    if (getCollided() > 0) {
//                        setCollided(getCollided() - 1);
//                        setAngle(getAngleTowardsEntity(target) - (float) Math.PI);
//                        setAccelerating(true);
//                        setBraking(false);
//                    }
////                    setAccelerating(true);
                }

            } else {
                // move in area?
            }
        }
        super.act(deltaTime);
    }

    public Optional<College> getAllied() {
        if (this.allied == null) {
            return Optional.empty();
        }
        return Optional.of(allied);
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
            //Gdx.app.log("Target", "Last");
            return this.lastTarget;
        } else {
            this.lastTarget = Optional.empty();
            if (targetCheck > 5f) {
                //Gdx.app.log("Target", "Nearest");
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
            //will need to add future simulation somehow
            float thetaToThisInFuture = AIUtil.perfectAngleToCollide(projectile, this);
            float thetaActual = AIUtil.convertToRealAngle(projectile.getAngle());
            float dist = (float)projectile.distanceFrom(this);
            boolean isTriangle = true;
            float theta;
            if(thetaToThisInFuture < thetaActual && thetaActual-thetaToThisInFuture < Math.PI/2){
                theta = thetaActual-thetaToThisInFuture;
            } else if(thetaActual < thetaToThisInFuture && thetaToThisInFuture-thetaActual < Math.PI/2){
                theta = thetaToThisInFuture-thetaActual;
            } else if(thetaActual < thetaToThisInFuture && (2*Math.PI - thetaToThisInFuture) + thetaActual < Math.PI/2){
                theta = (float)(2*Math.PI - thetaToThisInFuture) + thetaActual;
            } else if(thetaToThisInFuture < thetaActual && (2*Math.PI - thetaActual) + thetaToThisInFuture < Math.PI/2){
                theta = (float)(2*Math.PI - thetaActual) + thetaToThisInFuture;
            } else {
                theta = 0;
                isTriangle = false;
            }

            if (isTriangle == true){
                float opp = (float)Math.tan(theta)*dist;
                if(opp < 0){
                    opp = -opp;
                }
                if (opp < Math.max(3*this.getRectBounds().height/4, 3*this.getRectBounds().width/4)){
                    projectilesToDodge.add(projectile);
                }
            }

        }
        return projectilesToDodge;
    }


//    //This is the main call for projectiles to dodge
//    private Array<Entity> getProjectilesToDodge(Array<Projectile> projectiles){
//        Array<Entity> projectilesToDodge = new Array<Entity>();
//        Array<Entity> projectilesEntity = new Array<Entity>();
//        for (Projectile projectile : projectiles){
//            projectilesEntity.add(projectile);
//        }
//        //Working
//        projectilesEntity = AIUtil.getEntitysPossibleCollide(this, projectilesEntity, true);
//        for(int i = 0; i<projectilesEntity.size; i++) {
//            Gdx.app.log("Entity array", projectilesEntity.get(i).__str__());
//            float timeThis = getDistanceToPoint(this.getCentre().x, this.getCentre().y, this.xForCollision.get(i), this.yForCollision.get(i)) / this.getSpeed();
//            float timeProjectile = getDistanceToPoint(projectilesEntity.get(i).getCentre().x, projectilesEntity.get(i).getCentre().y, this.xForCollision.get(i), this.yForCollision.get(i)) / projectilesEntity.get(i).getSpeed();
//            if (timeProjectile - timeThis <= 100 && timeProjectile - timeThis >= -100) {
//                projectilesToDodge.add(projectilesEntity.get(i));
//            }
//        }
//        if (projectilesEntity.size != 0){Gdx.app.log("this", this.__str__()); Gdx.app.log("sizeToDodge", projectilesToDodge.size+"");}
//        for (int i = 0; i<xForCollision.size; i++){
//            Gdx.app.log("","X:" + Float.toString(xForCollision.get(i)) + " Y:" + Float.toString(yForCollision.get(i)));
//        }
//
//        return projectilesToDodge;
//    }

    private Optional<LivingEntity> getNearestTarget() {
        Player player = GameScreen.getInstance().getEntityManager().getOrCreatePlayer();
        Array<LivingEntity> nearby = getLivingEntitiesInRange();
        if (!areAllied(player)) {
            //not allied - target player
            if (nearby.contains(player, false)) {
                //if player is in range - target
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
                        }
                    } else {
                        nearest = Optional.of(livingEntity);
                    }
                }
            }
            return nearest;
        }

        return Optional.empty();
    }

    private Rectangle getRangeArea() {
        Rectangle radius = getRectBounds();
        radius.set(radius.x - range, radius.y - range, radius.width + 2 * range, radius.height + 2 * range);
        return radius;
    }


}
