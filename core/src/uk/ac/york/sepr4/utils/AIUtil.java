package uk.ac.york.sepr4.utils;


import uk.ac.york.sepr4.object.entity.Entity;
import uk.ac.york.sepr4.object.entity.LivingEntity;
import uk.ac.york.sepr4.object.projectile.Projectile;

import com.badlogic.gdx.utils.Array;

public class AIUtil {

    //Weightings for AI behaviour
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
//    public static Array<Entity> getEntitysPossibleCollide(LivingEntity target, Array<Entity> entitys, boolean isProjectile){
//        Array<Entity> possibleCollisionEntitys = new Array<Entity>();
//        float x;
//        float y;
//        for (Entity entity : entitys){
//            float theta1 = (float)(target.getAngle() - Math.PI/2);
//            float theta2 = (float)(entity.getAngle() - Math.PI/2);
//            Integer boundsSelection1 = subGetEntitysPossibleBoundsSelection(theta1);
//            Integer boundsSelection2 = subGetEntitysPossibleBoundsSelection(theta2);
//            float m1 = subGetEntitysPossibleCollideMCalc(theta1);
//            float m2 = subGetEntitysPossibleCollideMCalc(theta2);
//
//            float x1 = target.getCentre().x;
//            float x2 = entity.getCentre().x;
//            float y1 = entity.getCentre().y;
//            float y2 = entity.getCentre().y;
//
//            if (!(m1 == Float.NaN && m2 == Float.NaN)){
//                x = (y2-x2*m2-y1+x1*m1)/(m1 - m2);
//                y = m1*x + (y1 - m1*x1);
//                if (subGetEntitysPossibleBoundsCollisonCheck(y1, y, x1, x, boundsSelection1) && subGetEntitysPossibleBoundsCollisonCheck(y2, y, x2, x, boundsSelection2)){
//                    possibleCollisionEntitys.add(entity);
//                    if (isProjectile){
//                        target.addX(x);
//                        target.addY(y);
//                    }
//                }
//            } else if (m1 == Float.NaN){
//                x = x1;
//                y = m2*x1 + (y2 - m2*x2);
//                if (subGetEntitysPossibleBoundsCollisonCheck(y1, y, x1, x, boundsSelection1) && subGetEntitysPossibleBoundsCollisonCheck(y2, y, x2, x, boundsSelection2)){
//                    possibleCollisionEntitys.add(entity);
//                    if (isProjectile){
//                        target.addX(x);
//                        target.addY(y);
//                    }
//                }
//            } else if (m2 == Float.NaN){
//                x = x2;
//                y = m1*x2 + (y1 - m1*x1);
//                if (subGetEntitysPossibleBoundsCollisonCheck(y1, y, x1, x, boundsSelection1) && subGetEntitysPossibleBoundsCollisonCheck(y2, y, x2, x, boundsSelection2)){
//                    possibleCollisionEntitys.add(entity);
//                    if (isProjectile){
//                        target.addX(x);
//                        target.addY(y);
//                    }
//                }
//            }
//        }
//        return possibleCollisionEntitys;
//    }
//
//    private static float subGetEntitysPossibleCollideMCalc(float theta){
//        if(theta == 0){
//            return 0;
//        } else if (theta > 0 && theta < Math.PI/2){
//            return (float)Math.tan(theta);
//        } else if (theta == Math.PI/2){
//            return Float.NaN;
//        } else if (theta > Math.PI/2 && theta < Math.PI){
//            return (float)-Math.tan(theta);
//        } else if (theta == Math.PI){
//            return 0;
//        } else if (theta > Math.PI && theta < 3*Math.PI/2){
//            return (float)Math.tan(theta);
//        } else if (theta == 3*Math.PI){
//            return Float.NaN;
//        } else {
//            return (float)-Math.tan(theta);
//        }
//    }
//
//    private static Integer subGetEntitysPossibleBoundsSelection(float theta){
//        if(theta == 0){
//            return 0;
//        } else if (theta > 0 && theta < Math.PI/2){
//            return 1;
//        } else if (theta == Math.PI/2){
//            return 2;
//        } else if (theta > Math.PI/2 && theta < Math.PI){
//            return 3;
//        } else if (theta == Math.PI){
//            return 4;
//        } else if (theta > Math.PI && theta < 3*Math.PI/2){
//            return 5;
//        } else if (theta == 3*Math.PI){
//            return 6;
//        } else {
//            return 7;
//        }
//    }
//
//    private static boolean subGetEntitysPossibleBoundsCollisonCheck(float y1, float y, float x1, float x, Integer boundsSelection){
//        if (boundsSelection == 0){
//            if (y==y1 && x>x1){
//                return true;
//            }
//        } else if (boundsSelection == 1){
//            if (y>y1 && x>x1){
//                return true;
//            }
//        } else if (boundsSelection == 2){
//            if (y>y1 && x==x1){
//                return true;
//            }
//        } else if (boundsSelection == 3){
//            if (y>y1 && x<x1){
//                return true;
//            }
//        } else if (boundsSelection == 4){
//            if (y==y1 && x<x1){
//                return true;
//            }
//        } else if (boundsSelection == 5){
//            if (y<y1 && x<x1){
//                return true;
//            }
//        } else if (boundsSelection == 6){
//            if (y<y1 && x==x1){
//                return true;
//            }
//        } else {
//            if (y<y1 && x>x1){
//                return true;
//            }
//        }
//        return false;
//
//
//    }

    //Convert other two to use these functions because they are the same just allow entity in them aswell you will ned to pass basespeed of the livingentitys projectile
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

    //Works to perfectly so in act will have to have some logic if time is to much to not shoot
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

    public static float f(LivingEntity source, LivingEntity target) {
        double sigma = (double) standardDeviation;
        double fx = (1 / (Math.sqrt(2 * Math.PI) * sigma)) * Math.pow(Math.E, -(Math.pow((source.distanceFrom(target) - (double) mean), 2) / (2 * Math.pow(sigma, 2))));
        return (float) fx * 160f;
    }

    //change to go over a loop of things rather than target
    public static float resultantAngle(LivingEntity source, LivingEntity target) {
        float f = f(source, target);
        float sigma = source.getAngleTowardsEntity(target) - convertToRealAngle(target.getAngle());

        float tp;
        float rp;

        if (sigma <= Math.PI / 2) {
            tp = (float) (f * Math.cos(sigma) + (1 - f));
            rp = (float) (-f * Math.sin(sigma));
        } else if (sigma <= Math.PI) {
            tp = (float) ((1 - f) - f * Math.sin(sigma - Math.PI / 2));
            rp = (float) (-f * Math.cos(sigma - Math.PI / 2));
        } else if (sigma <= (3 * Math.PI) / 2) {
            tp = (float) ((1 - f) - f * Math.cos(sigma - Math.PI));
            rp = (float) (f * Math.sin(sigma - Math.PI));
        } else {
            tp = (float) ((1 - f) + f * Math.sin(sigma - (3 * Math.PI) / 2));
            rp = (float) (f * Math.cos(sigma - (3 * Math.PI) / 2));
        }

        if (tp >= 0 && rp <= 0) {
            sigma = (float) Math.atan(-rp / tp);
        } else if (tp <= 0 && rp <= 0) {
            sigma = (float) (Math.PI / 2 + Math.atan(-tp / -rp));
        } else if (tp <= 0 && rp >= 0) {
            sigma = (float) (Math.PI + Math.atan(rp / -tp));
        } else {
            sigma = (float) ((3 * Math.PI) / 2 + Math.atan(tp / rp));
        }
        float rsigma = sigma + source.getAngleTowardsEntity(target);
        rsigma += f * (Math.PI);
        return rsigma;
    }
}
