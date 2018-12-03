package uk.ac.york.sepr4.utils;

import uk.ac.york.sepr4.object.entity.LivingEntity;
import uk.ac.york.sepr4.object.projectile.Projectile;

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

    public static boolean isProjectileOnCourseToHit(Projectile projectile) {

        return false;
    }
}
