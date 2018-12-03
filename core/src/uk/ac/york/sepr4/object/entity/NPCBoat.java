package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.projectile.Projectile;
import uk.ac.york.sepr4.object.projectile.ProjectileType;
import uk.ac.york.sepr4.screen.GameScreen;
import uk.ac.york.sepr4.utils.AIUtil;

import java.util.List;
import java.util.Optional;

@Data
public class NPCBoat extends LivingEntity {

    //NPCBoat-specific variables
    private float range;
    private float accuracy;

    private boolean hostile;
    private Optional<College> allied;

    public NPCBoat(Integer id, Texture texture) {
        super(id, texture);
    }

    public NPCBoat(Integer id, Texture texture, float angle, float speed, float maxSpeed, Double health, Double maxHealth, Integer turningSpeed, boolean onFire, List<ProjectileType> projectileTypes, float range, float accuracy, boolean hostile, Optional<College> allied) {
        super(id, texture, angle, speed, maxSpeed, health, maxHealth, turningSpeed, onFire, projectileTypes);
        this.range = range;
        this.accuracy = accuracy;
        this.hostile = hostile;
        this.allied = allied;
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

        if(hostile) {

            Optional<LivingEntity> optionalTarget = getNearestTarget();
            if(optionalTarget.isPresent()) {
                LivingEntity target = optionalTarget.get();
                float resAngle = AIUtil.resultantAngle(this, target);
                resAngle = AIUtil.convertToRealAngle(resAngle);
                setAngle(resAngle);

                float f = AIUtil.f(this, target);

                if ((1 - f) - f > 0.2) {
                    setAccelerating(true);
                    setBraking(false);
                } else if (((1 - f) - f > -0.2)) {
                    setAccelerating(false);
                    setBraking(false);
                } else {
                    setBraking(false);
                    setAccelerating(true);
                }
                if (getSpeed() < getMaxSpeed() / 5) {
                    setAccelerating(true);
                    setBraking(false);
                }
                if (goingToCollide(target)) {
                    setCollided(100);
                }
                if (getCollided() > 0) {
                    setCollided(getCollided() - 1);
                    setAngle(getAngleTowardsEntity(target) - (float) Math.PI);
                    setAccelerating(true);
                    setBraking(false);
                }
                setAccelerating(true);
            }

        } else {
            // move in area?
        }

        super.act(deltaTime);
    }

    private Optional<LivingEntity> getNearestTarget() {
        Player player = GameScreen.getInstance().getEntityManager().getOrCreatePlayer();
        Array<LivingEntity> nearby = GameScreen.getInstance().getEntityManager().getNPCInArea(getRangeArea());

        if(nearby.contains(player, false)){
            //if player is in range - target
            return Optional.of(player);
        } else {
            if(nearby.size > 0){
                Optional<LivingEntity> nearest = Optional.empty();
                for(LivingEntity livingEntity : nearby) {
                    if(livingEntity instanceof NPCBoat) {
                        Optional<College> targeAlliance = ((NPCBoat) livingEntity).getAllied();
                        if(targeAlliance.isPresent()) {
                            if(allied.isPresent()) {
                                if(!targeAlliance.get().equals(allied.get())){
                                    if(nearest.isPresent()){
                                        if(nearest.get().distanceFrom(this) > livingEntity.distanceFrom(this)){
                                            //closest enemy
                                            nearest = Optional.of(livingEntity);
                                        }
                                    } else {
                                        nearest = Optional.of(livingEntity);
                                    }
                                }
                            }
                        }
                    }
                }
                return nearest;
            }
        }
        return Optional.empty();
    }

    private Rectangle getRangeArea() {
        Rectangle radius = getRectBounds();
        radius.set(radius.x-range, radius.y-range, radius.width+2*range, radius.height+2*range);
        return radius;
    }



}
