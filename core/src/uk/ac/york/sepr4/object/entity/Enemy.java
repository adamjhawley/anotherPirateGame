package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import uk.ac.york.sepr4.object.projectile.ProjectileType;
import uk.ac.york.sepr4.screen.GameScreen;

import javax.print.attribute.standard.MediaSize;
import javax.swing.text.MutableAttributeSet;
import java.util.List;

import static java.lang.Double.NaN;

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
        resAngle = convertToRealAngle(resAngle);
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
        //Need to workout some logic on whether to shoot tip(use B if over a certain value to stop shooting otherwise you get a weird line of shots)
        super.act(deltaTime);
    }

    private float convertToRealAngle(float angle){
        if(angle >= 0) {
            while (angle >= 0) {
                angle -= 2*Math.PI;
            }
            angle += 2*Math.PI;
        } else {
            while(angle < 0) {
                angle += 2*Math.PI;
            }
        }
        return angle;
    }

    private float perfectShotAngle(Player player){
        boolean right;
        double theta;
        double thetaP = convertToRealAngle(player.getAngle());
        double thetaTP = getAngleTowardsLE(player);

        if(thetaP<=thetaTP && thetaTP<=Math.PI){
            theta = thetaP + (Math.PI - thetaTP);
            right = true;
        } else if((2*Math.PI - thetaP) <= (2*Math.PI - thetaTP) && (2*Math.PI - thetaTP) <= Math.PI){
            theta = (2*Math.PI - thetaP) + (Math.PI - (2*Math.PI - thetaTP));
            right = false;
        } else if(thetaTP <= Math.PI && thetaP > thetaTP && (2*Math.PI - thetaP) >= (Math.PI - thetaTP)){
            theta = (2*Math.PI - thetaP) - (Math.PI - thetaTP);
            right = false;
        } else if((2*Math.PI - thetaTP) <= Math.PI && thetaTP > thetaP && thetaP >= (Math.PI - (2*Math.PI - thetaTP))){
            theta = thetaP - (Math.PI - (2*Math.PI - thetaTP));
            right = true;
        } else if(thetaTP <= Math.PI && thetaP > thetaTP && (2*Math.PI - thetaP) <= (Math.PI - thetaTP)){
            theta = (Math.PI - thetaTP) - (2*Math.PI - thetaP);
            right = true;
        } else if((2*Math.PI - thetaTP) <= Math.PI && thetaTP > thetaP && (Math.PI - (2*Math.PI - thetaTP)) >= thetaP){
            theta = (Math.PI - (2*Math.PI - thetaTP)) - thetaP;
            right = false;
        } else {
            theta = 0;
            right = true;
        }

        double b = timeForPerfectShotToHit(player, (float)theta);
        double time = getDistanceToPlayer(player)/(getSpeed()+getSelectedProjectileType().getBaseSpeed());


        double SE = (getSpeed()+getSelectedProjectileType().getBaseSpeed());
        double SP = player.getSpeed();

        double PM = b*SP;
        double EP = time*SE;
        double ME = b*SE;

        double shotAngle = Math.acos(((ME*ME) + (EP*EP) - (PM*PM)) / (2*ME*EP));

        if(right == true){
            shotAngle = thetaTP - shotAngle;
        } else {
            shotAngle = thetaTP + shotAngle;
        }
        if(((ME*ME) + (EP*EP) - (PM*PM)) / (2*ME*EP) > 1 || ((ME*ME) + (EP*EP) - (PM*PM)) / (2*ME*EP) < -1){
            shotAngle = thetaTP;
        }

        return convertToRealAngle((float)shotAngle);
    }

    private float timeForPerfectShotToHit(Player player, float theta){
        double time = getDistanceToPlayer(player)/(getSpeed()+getSelectedProjectileType().getBaseSpeed());

        double b = time / (2*Math.cos(theta));
        if(b < 0){
            b = -b;
        }
        return (float)b;
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
        float sigma = getAngleTowardsLE(player) - convertToRealAngle(player.getAngle());

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
