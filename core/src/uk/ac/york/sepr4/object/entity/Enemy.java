package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import lombok.Data;
import uk.ac.york.sepr4.screen.GameScreen;

@Data
public class Enemy extends LivingEntity {

    //Weightings for AI behaviour
    private float standardDeviation;
    private float mean;
    private float accuracy;


    public Enemy(Integer id, Texture texture){
        super(id, texture);
        this.standardDeviation = 50f;
        this.mean = 250f;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        //TODO: Render trail/bow wave?
    }

    public void act(float deltaTime) {
        Player player = GameScreen.getInstance().getEntityManager().getOrCreatePlayer();
        super.act(deltaTime);
        setAngle(resultantAngle(player));
        Gdx.app.log("re", Float.toString(resultantAngle(player)));
    }

    private float getAngleTowardsPlayer(Player player) {
        double d_angle = Math.atan(((player.getY() - getY()) / (player.getX() - getX())));
        if(player.getX() < getX()){
            d_angle += Math.PI;
        }
        float angle = (float)d_angle + (float)Math.PI/2;
        return angle;
    }

    private float getDistanceToPlayer(Player player) {
        float dist = (float)Math.sqrt(Math.pow((player.getX() - getX()), 2) + Math.pow((player.getY() - getY()), 2));
        return dist;
    }

    private float f(Player player){
        //f(x) = 1/(sqrt(2 pi) sigma) e^-((x - mean)^2/(2 sigma^2))
        double sigma = (double)this.standardDeviation;
        double fx = (1/(Math.sqrt(2*Math.PI)*sigma))*Math.pow(Math.E, -(Math.pow(((double)getDistanceToPlayer(player) - (double)this.mean), 2)/(2*Math.pow(sigma, 2))));
        return (float)fx*120f;
    }

    private float resultantAngle(Player player){
        float f = f(player);
        float sigma = getAngleTowardsPlayer(player) + convertToRealAngle(player.getAngle());

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

        float rsigma = sigma + getAngleTowardsPlayer(player);
        return  rsigma;
    }

    private float convertToRealAngle(float angle) {
        if(angle < 0){
            angle = -angle;
        }
        while(angle >= 0) {
            angle -= 2*Math.PI;
        }
        angle += 2*Math.PI;
        return angle;
    }

    //private float perfectShoot(Player player) {

    //}


}
