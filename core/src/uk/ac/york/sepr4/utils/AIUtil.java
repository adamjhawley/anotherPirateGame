package uk.ac.york.sepr4.utils;


import uk.ac.york.sepr4.object.entity.Entity;
import uk.ac.york.sepr4.object.entity.LivingEntity;
import uk.ac.york.sepr4.object.projectile.Projectile;

import com.badlogic.gdx.utils.Array;

public class AIUtil {

    //Weightings for AI behaviour
    //Todo: Move these to the NPC so can be adjusted per boat
    private static float standardDeviation = 50f;
    private static float mean = 250f;

    public static float convertToRealAngle(float angle) {
        if (angle >= 0) {
            while (angle >= 0) {
                angle -= 2 * Math.PI;
            }
            angle += 2 * Math.PI;
        } else {
            while (angle < 0) {
                angle += 2 * Math.PI;
            }
        }
        return angle;
    }

    //Todo: Create one function that does the same thing just uses speeds rather than using getProjectileSpeed
    public static float perfectAngleToCollide(Entity source, Entity target) {
        boolean right;
        double theta;
        double thetaP = convertToRealAngle(target.getAngle());
        double thetaTP = source.getAngleTowardsEntity(target);

        if (thetaP <= thetaTP && thetaTP <= Math.PI) {
            theta = thetaP + (Math.PI - thetaTP);
            right = true;
        } else if ((2 * Math.PI - thetaP) <= (2 * Math.PI - thetaTP) && (2 * Math.PI - thetaTP) <= Math.PI) {
            theta = (2 * Math.PI - thetaP) + (Math.PI - (2 * Math.PI - thetaTP));
            right = false;
        } else if (thetaTP <= Math.PI && thetaP > thetaTP && (2 * Math.PI - thetaP) >= (Math.PI - thetaTP)) {
            theta = (2 * Math.PI - thetaP) - (Math.PI - thetaTP);
            right = false;
        } else if ((2 * Math.PI - thetaTP) <= Math.PI && thetaTP > thetaP && thetaP >= (Math.PI - (2 * Math.PI - thetaTP))) {
            theta = thetaP - (Math.PI - (2 * Math.PI - thetaTP));
            right = true;
        } else if (thetaTP <= Math.PI && thetaP > thetaTP && (2 * Math.PI - thetaP) <= (Math.PI - thetaTP)) {
            theta = (Math.PI - thetaTP) - (2 * Math.PI - thetaP);
            right = true;
        } else if ((2 * Math.PI - thetaTP) <= Math.PI && thetaTP > thetaP && (Math.PI - (2 * Math.PI - thetaTP)) >= thetaP) {
            theta = (Math.PI - (2 * Math.PI - thetaTP)) - thetaP;
            right = false;
        } else {
            theta = 0;
            right = true;
        }

        double b = timeForPerfectAngleToCollide(source, target, (float) theta);
        double time = source.distanceFrom(target) / (source.getSpeed() + source.getSpeed());


        double SE = (source.getSpeed() + source.getSpeed());
        double SP = target.getSpeed();

        double PM = b * SP;
        double EP = time * SE;
        double ME = b * SE;

        double shotAngle = Math.acos(((ME * ME) + (EP * EP) - (PM * PM)) / (2 * ME * EP));

        if (right == true) {
            shotAngle = thetaTP - shotAngle;
        } else {
            shotAngle = thetaTP + shotAngle;
        }
        if (((ME * ME) + (EP * EP) - (PM * PM)) / (2 * ME * EP) > 1 || ((ME * ME) + (EP * EP) - (PM * PM)) / (2 * ME * EP) < -1) {
            shotAngle = thetaTP;
        }

        return convertToRealAngle((float) shotAngle);
    }
    public static float timeForPerfectAngleToCollide(Entity source, Entity target, float theta) {
        double time = source.distanceFrom(target) / (source.getSpeed() + source.getSpeed());

        double b = time / (2 * Math.cos(theta));
        if (b < 0) {
            b = -b;
        }
        return (float) b;
    }

    //Todo: Create one function that does the same thing just uses speeds rather than using getProjectileSpeed
    public static float perfectShotAngle(LivingEntity source, LivingEntity target) {
        boolean right;
        double theta;
        double thetaP = convertToRealAngle(target.getAngle());
        double thetaTP = source.getAngleTowardsEntity(target);

        if (thetaP <= thetaTP && thetaTP <= Math.PI) {
            theta = thetaP + (Math.PI - thetaTP);
            right = true;
        } else if ((2 * Math.PI - thetaP) <= (2 * Math.PI - thetaTP) && (2 * Math.PI - thetaTP) <= Math.PI) {
            theta = (2 * Math.PI - thetaP) + (Math.PI - (2 * Math.PI - thetaTP));
            right = false;
        } else if (thetaTP <= Math.PI && thetaP > thetaTP && (2 * Math.PI - thetaP) >= (Math.PI - thetaTP)) {
            theta = (2 * Math.PI - thetaP) - (Math.PI - thetaTP);
            right = false;
        } else if ((2 * Math.PI - thetaTP) <= Math.PI && thetaTP > thetaP && thetaP >= (Math.PI - (2 * Math.PI - thetaTP))) {
            theta = thetaP - (Math.PI - (2 * Math.PI - thetaTP));
            right = true;
        } else if (thetaTP <= Math.PI && thetaP > thetaTP && (2 * Math.PI - thetaP) <= (Math.PI - thetaTP)) {
            theta = (Math.PI - thetaTP) - (2 * Math.PI - thetaP);
            right = true;
        } else if ((2 * Math.PI - thetaTP) <= Math.PI && thetaTP > thetaP && (Math.PI - (2 * Math.PI - thetaTP)) >= thetaP) {
            theta = (Math.PI - (2 * Math.PI - thetaTP)) - thetaP;
            right = false;
        } else {
            theta = 0;
            right = true;
        }

        double b = timeForPerfectShotToHit(source, target, (float) theta);
        double time = source.distanceFrom(target) / (source.getSpeed() + source.getSelectedProjectileType().getBaseSpeed());


        double SE = (source.getSpeed() + source.getSelectedProjectileType().getBaseSpeed());
        double SP = target.getSpeed();

        double PM = b * SP;
        double EP = time * SE;
        double ME = b * SE;

        double shotAngle = Math.acos(((ME * ME) + (EP * EP) - (PM * PM)) / (2 * ME * EP));

        if (right == true) {
            shotAngle = thetaTP - shotAngle;
        } else {
            shotAngle = thetaTP + shotAngle;
        }
        if (((ME * ME) + (EP * EP) - (PM * PM)) / (2 * ME * EP) > 1 || ((ME * ME) + (EP * EP) - (PM * PM)) / (2 * ME * EP) < -1) {
            shotAngle = thetaTP;
        }

        return convertToRealAngle((float) shotAngle);
    }
    public static float timeForPerfectShotToHit(LivingEntity source, LivingEntity target, float theta) {
        double time = source.distanceFrom(target) / (source.getSpeed() + source.getSelectedProjectileType().getBaseSpeed());

        double b = time / (2 * Math.cos(theta));
        if (b < 0) {
            b = -b;
        }
        return (float) b;
    }


    //Functions for knowing the force due the distance
    public static float normalDistFromMean(float dist) {
        double sigma = (double) standardDeviation;
        double fx = (1 / (Math.sqrt(2 * Math.PI) * sigma)) * Math.pow(Math.E, -(Math.pow((dist - (double) mean), 2) / (2 * Math.pow(sigma, 2))));
        return (float) fx * 160f;
    }
    public static float straightLineGraphOneIfCloser(float dist) {
        if(dist <= 100f){
            return 1f;
        } else if (dist<= 200f){
            return -0.01f*(dist-100f);
        } else {
            return 0f;
        }
    }
    //********************

    public static Array<Float> resultantForce(Array<Float> angles, Array<Float> forces){
        Array<Float> force_angle = new Array<Float>();
        float N = 0, E = 0;
        double sigma;
        for (int i = 0; i<angles.size; i++){
            if (convertToRealAngle(angles.get(i)) <= Math.PI/2){
                E += forces.get(i)*Math.sin(angles.get(i));
                N -= forces.get(i)*Math.cos(angles.get(i));
            } else if (convertToRealAngle(angles.get(i)) <= Math.PI){
                E += forces.get(i)*Math.cos(angles.get(i) - Math.PI/2);
                N += forces.get(i)*Math.sin(angles.get(i) - Math.PI/2);
            } else if (convertToRealAngle(angles.get(i)) <= 3*Math.PI/2){
                E -= forces.get(i)*Math.sin(angles.get(i) - Math.PI);
                N += forces.get(i)*Math.cos(angles.get(i) - Math.PI);
            } else {
                E -= forces.get(i)*Math.cos(angles.get(i) - 3*Math.PI/2);
                N -= forces.get(i)*Math.sin(angles.get(i) - 3*Math.PI/2);
            }
        }
        if (N >= 0 && E <= 0) {
            sigma = Math.atan(-E / N);
        } else if (N <= 0 && E <= 0) {
            sigma = (Math.PI / 2 + Math.atan(-N / -E));
        } else if (N <= 0 && E >= 0) {
            sigma = (Math.PI + Math.atan(E / -N));
        } else {
            sigma = ((3 * Math.PI) / 2 + Math.atan(N / E));
        }

        force_angle.add((float)Math.sqrt(N*N + E*E));
        force_angle.add((float)sigma);
        return force_angle;
    }
}
