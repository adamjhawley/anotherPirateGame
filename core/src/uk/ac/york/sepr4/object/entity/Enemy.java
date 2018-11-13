package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import lombok.Data;
import uk.ac.york.sepr4.PirateGame;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.entity.item.Item;
import uk.ac.york.sepr4.object.entity.projectile.ProjectileType;
import uk.ac.york.sepr4.screen.GameScreen;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class Enemy extends LivingEntity {

    public Enemy(Integer id, Texture texture){
        super(id, texture);
        setSize(getTexture().getWidth(), getTexture().getHeight());
    }

    @Override
    public void fire(float angle) {
        ProjectileType projectileType = this.getSelectedProjectileType();
        if(projectileType.getCooldown() <= getCurrentCooldown()) {
            setCurrentCooldown(0f);
            GameScreen.getInstance().getProjectileManager().spawnProjectile(projectileType, this, getSpeed(), angle);
        }
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        //TODO: Render trail/bow wave?
    }

    public void act(float deltaTime) {
        Player player = GameScreen.getInstance().getEntityManager().getOrCreatePlayer();

        super.act(deltaTime);
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
        float dist = (float)Math.sqrt(Math.pow((player.getX() - getX()), 2) + Math.pow((player.getY() - getX()), 2));
        return dist;
    }

    //private float perfectShoot(Player player) {

    //}


}
