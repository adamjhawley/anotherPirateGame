package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import uk.ac.york.sepr4.object.projectile.ProjectileType;
import uk.ac.york.sepr4.screen.GameScreen;

import javax.swing.text.MutableAttributeSet;
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
//        float resAngle = resultantAngle(player);
//        resAngle = resAngle % (float)(2*Math.PI);
//        setAngle(resAngle);
//
//        float f = f(player);
//
//        if((1-f) - f > 0.2){
//            setAccelerating(true);
//            setBraking(false);
//        } else if(((1-f) - f > -0.2)){
//            setAccelerating(false);
//            setBraking(false);
//        } else {
//                setBraking(false);
//                setAccelerating(true);
//        }
//        if(getSpeed() < getMaxSpeed()/5){
//            setAccelerating(true);
//            setBraking(false);
//        }

        //Gdx.app.log("fire", Float.toString(perfectShotAngle(player)));
        Gdx.app.log("angle", Float.toString(player.getAngle()));
        Gdx.app.log("anglewithmod", Float.toString((float)(player.getAngle() % 2*Math.PI)));
        ////////Error with angle mod might be the reason for everything going wrong so fix
        ////////B is also coming out as minus which shouldnt happen
        super.act(deltaTime);
    }

    private float perfectShotAngle(Player player){
        boolean right;
        double theta;
        double thetaP = player.getAngle() % 2*Math.PI;
        double thetaTP = getAngleTowardsLE(player);
        Gdx.app.log("thetaP", thetaP+"");
        Gdx.app.log("thetaTP", thetaTP+"");
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
        Gdx.app.log("thetaP", theta+"");
        if(right == true){
            Gdx.app.log("right", "");
        } else {
            Gdx.app.log("left", "");
        }

        double b = timeForPerfectShotToHit(player, (float)theta);
        Gdx.app.log("b", b+"");
        double time = getDistanceToPlayer(player)/(getSpeed()+getSelectedProjectileType().getBaseSpeed());
        Gdx.app.log("time", time+"");



//        double shotAngle = getAngleTowardsLE(player) + (Math.PI - Math.acos(1-(Math.pow(time, 2)/(2*Math.pow(b, 2)))) - thetaP);
//        double shotAngle = (Math.PI - Math.acos(1-(Math.pow(time, 2)/(2*Math.pow(b, 2)))) - theta);
        double shotAngle = Math.acos(Math.pow(time, 2) / (2*b*time));
        Gdx.app.log("shotangle", shotAngle+"");

        if(right == true){
            shotAngle = thetaTP - shotAngle;
        } else {
            shotAngle = thetaTP + shotAngle;
        }
        return (float)shotAngle;
    }

    private float timeForPerfectShotToHit(Player player, float theta){
//        double a = Math.PI - 2;
//        Gdx.app.log("a", a+"");
        double time = getDistanceToPlayer(player)/(getSpeed()+getSelectedProjectileType().getBaseSpeed());
//        double test = 2*Math.cos(theta);
//        if(test < 0){
//            test = -test;
//        }
//
       double b = Math.pow(time, 2) / 2*Math.cos(theta);
//        double b = Math.pow(time, 2) / test;
        return (float)b;





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
//        double bPlus = (Math.pow(time, 2) + Math.sqrt(9*Math.pow(time, 4)))/(4*time);
//        double bMinus = (Math.pow(time, 2) - Math.sqrt(9*Math.pow(time, 4)))/(4*time);
//        return (float)bPlus;

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
