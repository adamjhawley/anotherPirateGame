package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import uk.ac.york.sepr4.object.projectile.ProjectileType;
import uk.ac.york.sepr4.screen.GameScreen;

import java.util.List;

@Data
public class Enemy extends LivingEntity {

    //Weightings for AI behaviour
    private float standardDeviation = 50f;
    private float mean = 250f;

    //Enemy-specific variables
    private float range;
    private float accuracy;

    public Enemy(Integer id, Texture texture){
        super(id, texture);
    }

    public Enemy(Integer id, Texture texture, float angle, float speed, float maxSpeed, Double health, Double maxHealth, Integer turningSpeed, boolean onFire, List<ProjectileType> projectileTypes, float range, float accuracy) {
        super(id, texture, angle, speed, maxSpeed, health, maxHealth, turningSpeed, onFire, projectileTypes);
        this.range = range;
        this.accuracy = accuracy;
    }

    /**
     * Overriding Actor's 'act' function.
     * Calculates the direction of travel for the enemy depending on the location of the player.
     *
     * @param deltaTime
     */
    public void act(float deltaTime) {
        Player player = GameScreen.getInstance().getEntityManager().getOrCreatePlayer();
        float resAngle = resultantAngle(player);
        resAngle = resAngle % (float)(2*Math.PI);
        setAngle(resAngle);

        float f = f(player);

        if((1-f) - f > 0.2){
            setAccelerating(true);
            setBraking(false);
        } else if(((1-f) - f > -0.2)){
            setAccelerating(false);
            setBraking(false);
        } else {
                setBraking(false);
                setAccelerating(true);
        }
        if(getSpeed() < getMaxSpeed()/5){
            setAccelerating(true);
            setBraking(false);
        }

        fire(perfectShotAngle(player));
        super.act(deltaTime);
    }

    private float perfectShotAngle(Player player){
        double thetaP;
        double b = timeForPerfectShotToHit(player);
        double time = getDistanceToPlayer(player)/(getSpeed()+getSelectedProjectileType().getBaseSpeed());

        if(getAngleTowardsLE(player) > (player.getAngle() % 2*Math.PI)){
            thetaP = getAngleTowardsLE(player) - (player.getAngle()% 2*Math.PI);
            if(thetaP > Math.PI){
                thetaP = 2*Math.PI - thetaP;
            }
        } else {
            thetaP = (player.getAngle() % 2*Math.PI) - getAngleTowardsLE(player);
            if(thetaP > Math.PI){
                thetaP = 2*Math.PI - thetaP;
            }
        }

        double shotAngle = getAngleTowardsLE(player) + (Math.PI - Math.acos(1-(Math.pow(time, 2)/(2*Math.pow(b, 2)))) - thetaP);
        return (float)shotAngle;
    }

    private float timeForPerfectShotToHit(Player player){
//        double a = Math.PI - 2;
//        Gdx.app.log("a", a+"");
        double time = getDistanceToPlayer(player)/(getSpeed()+getSelectedProjectileType().getBaseSpeed());
        Gdx.app.log("time", time+"");
//        double thetaP;
//
//        if(getAngleTowardsLE(player) > (player.getAngle() % 2*Math.PI)){
//            thetaP = getAngleTowardsLE(player) - (player.getAngle()% 2*Math.PI);
//            if(thetaP > Math.PI){
//                thetaP = 2*Math.PI - thetaP;
//            }
//        } else {
//            thetaP = (player.getAngle() % 2*Math.PI) - getAngleTowardsLE(player);
//            if(thetaP > Math.PI){
//                thetaP = 2*Math.PI - thetaP;
//            }
//        }
//        Gdx.app.log("ntp", (Math.PI - getAngleTowardsLE(player))+"");
//        Gdx.app.log("player", (player.getAngle()% 2*Math.PI)+"");
//        Gdx.app.log("thetap", thetaP+"");
//        double b = 2*Math.sin(thetaP)*time;
//        Gdx.app.log("b", b+"");
//        double c = Math.pow(time, 2);
//        Gdx.app.log("c", c+"");
//
//        double BtimeMinus = (-b-Math.sqrt(Math.pow(b, 2) - 4*a*c))/(2*a);
//        double BtimePlus = (-b+Math.sqrt(Math.pow(b, 2) - 4*a*c))/(2*a);
//
//        Gdx.app.log("minus", BtimeMinus+"");
//        Gdx.app.log("plus", BtimePlus+"");
        double bPlus = (Math.pow(time, 2) + Math.sqrt(9*Math.pow(time, 4)))/(4*time);
        double bMinus = (Math.pow(time, 2) - Math.sqrt(9*Math.pow(time, 4)))/(4*time);
        return (float)bPlus;
    }

    private float getDistanceToPlayer(Player player) {
        float dist = (float)Math.sqrt(Math.pow((player.getCentre().x - getCentre().x), 2) + Math.pow((player.getCentre().y - getCentre().y), 2));
        return dist;
    }

    private float f(Player player){
        double sigma = (double)this.standardDeviation;
        double fx = (1/(Math.sqrt(2*Math.PI)*sigma))*Math.pow(Math.E, -(Math.pow(((double)getDistanceToPlayer(player) - (double)this.mean), 2)/(2*Math.pow(sigma, 2))));
        return (float)fx*160f;
    }

    private float resultantAngle(Player player){
        float f = f(player);
        float sigma = getAngleTowardsLE(player) - (player.getAngle() % (float)(2*Math.PI));

        float tp;
        float rp;

        if(sigma <= Math.PI/2){
            tp = (float)(f*Math.cos(sigma) + (1-f));
            rp = (float)(-f*Math.sin(sigma));
        } else if(sigma <= Math.PI){
            tp = (float)((1-f) - f*Math.sin(sigma - Math.PI/2));
            rp = (float)(-f*Math.cos(sigma - Math.PI/2));
        } else if(sigma <= (3*Math.PI)/2){
            tp = (float)((1-f) - f*Math.cos(sigma - Math.PI));
            rp = (float)(f*Math.sin(sigma - Math.PI));
        } else {
            tp = (float)((1-f) + f*Math.sin(sigma - (3*Math.PI)/2));
            rp = (float)(f*Math.cos(sigma - (3*Math.PI)/2));
        }

        if(tp >= 0 && rp <= 0){
            sigma = (float)Math.atan(-rp/tp);
        } else if(tp <= 0 && rp <= 0){
            sigma = (float)(Math.PI/2 + Math.atan(-tp/-rp));
        } else if(tp <= 0 && rp >= 0){
            sigma = (float)(Math.PI + Math.atan(rp/-tp));
        } else {
            sigma = (float)((3*Math.PI)/2 + Math.atan(tp/rp));
        }
        float rsigma = sigma + getAngleTowardsLE(player);
        rsigma += f*(Math.PI);
        return  rsigma;
    }


}
