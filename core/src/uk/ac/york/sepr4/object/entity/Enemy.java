package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import lombok.Data;
import uk.ac.york.sepr4.PirateGame;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.entity.item.Item;
import uk.ac.york.sepr4.object.entity.projectile.ProjectileType;
import uk.ac.york.sepr4.screen.GameScreen;
import java.util.ArrayList;
import java.util.List;

@Data
public class Enemy extends LivingEntity {

    private float maxSpeed;
    private Integer turningSpeed;

    private float angularSpeed;
    private boolean isAccelerating;
    private boolean isDeaccelerating;

    public Enemy() {
        this(1, 0, 0, 20.0, 20.0, 0f, false, false, 120f);
    }

    public Enemy(Integer id, float angle, float speed, Double health, Double maxHealth, float angularSpeed, boolean isAccelerating, boolean isDeaccelerating, float maxSpeed){
        super(id, angle, speed, health, maxHealth, TextureManager.ENEMY);
        setPosition(50, 50);
        setSize(getTexture().getWidth(), getTexture().getHeight());

        this.maxSpeed = maxSpeed;

        this.angularSpeed = angularSpeed;
        this.isAccelerating = isAccelerating;
        this.isDeaccelerating = isDeaccelerating;

        addProjectileType(GameScreen.getInstance().getProjectileManager().getDefaultWeaponType());
        setSelectedProjectileType(GameScreen.getInstance().getProjectileManager().getDefaultWeaponType());
    }

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);
        setCurrentCooldown(getCurrentCooldown()+deltaTime);
        AI();

        float speed = getSpeed();
        float angle = getAngle();

        if(isAccelerating) {
            if (speed > maxSpeed) {
                speed = maxSpeed;
            } else {
                speed += 39f * deltaTime;
            }
        } else if(isDeaccelerating) {
            if(speed > 0) {
                speed -= 79f * deltaTime;
            }
        } else {
            if(speed > 0) {
                speed -= 19f * deltaTime;
            }
        }

        float y = getY();
        float x = getX();
        y -= speed * deltaTime * Math.cos(angle);
        x += speed * deltaTime * Math.sin(angle);

        setPosition(x, y);

        angle += (angularSpeed * deltaTime) * (speed/maxSpeed);

        setSpeed(speed);
        setAngle(angle);
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

    private void AI() {
        //Player player =
    }
}
