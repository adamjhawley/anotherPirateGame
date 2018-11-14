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
        this.standardDeviation = 25f;
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
        setAccelerating(true);
        if(getPreferedAngle(player) > this.getAngle()){
            this.setAngularSpeed(getTurningSpeed());
        } else if (getPreferedAngle(player) < this.getAngle()) {
            this.setAngularSpeed(-getTurningSpeed());
        } else {
            setAngularSpeed(0);
        }
    }

    private float getAngleTowardsPlayer(Player player) {
        double d_angle = -Math.atan(((player.getX() - getX())/ (player.getY() - getY())));
        float angle = (float)d_angle;
        if((player.getY() - getY()) > 0f) {
            angle -= Math.PI;
        }
        return angle;
    }

    private float getDistanceToPlayer(Player player) {
        float dist = (float)Math.sqrt(Math.pow((player.getX() - getX()), 2) + Math.pow((player.getY() - getY()), 2));
        Gdx.app.log("test", Float.toString(dist));
        return dist;
    }

//    private float f(Player player){
//        //f(x) = 1/(sqrt(2 pi) sigma) e^-((x - mean)^2/(2 sigma^2))
//        double sigma = (double)this.standardDeviation;
//        double fx = (1/(Math.sqrt(2*Math.PI)*sigma))*Math.pow(Math.E, -(Math.pow(((double)getDistanceToPlayer(player) - (double)this.mean), 2)/(2*Math.pow(sigma, 2))));
//        return (float)fx*60f;
//    }

    private float getPreferedAngle(Player player) {
        if(getDistanceToPlayer(player) > 300) {
            return getAngleTowardsPlayer(player);
        } else if(getDistanceToPlayer(player) < 200) {
            return (getAngleTowardsPlayer(player) - (float)Math.PI);
        } else {
            return player.getAngle();
        }
    }

    //private float perfectShoot(Player player) {

    //}


}
