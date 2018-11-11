package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.entity.item.Item;
import uk.ac.york.sepr4.object.entity.projectile.ProjectileType;
import uk.ac.york.sepr4.screen.GameScreen;
import java.util.ArrayList;
import java.util.List;

@Data
public class Player extends LivingEntity implements InputProcessor {

    private Integer balance;
    private Integer xp;

    private float maxSpeed;

    private List<Item> inventory;
    private float angularSpeed;
    private boolean isAccelerating;
    private boolean isDeaccelerating;

    public Player(){
        this(0, 0, 0, 0, 0, 20.0, 20.0, 120f, new ArrayList<Item>(), 0f, false);
    }

    public Player(Integer id, float angle, float speed, Integer balance, Integer xp, Double health, Double maxHealth, float maxSpeed, List<Item> inventory, float angularSpeed, boolean isAccelerating) {
        super(id, angle, speed, health, maxHealth, TextureManager.PLAYER);
        this.balance = balance;
        this.xp = xp;

        this.maxSpeed = maxSpeed;
        this.inventory = inventory;

        this.angularSpeed = angularSpeed;
        this.isAccelerating = isAccelerating;

        setPosition(50, 50);

        setSize(getTexture().getWidth(), getTexture().getHeight());

        addProjectileType(GameScreen.getInstance().getProjectileManager().getDefaultWeaponType());
        setSelectedProjectileType(GameScreen.getInstance().getProjectileManager().getDefaultWeaponType());

    }

    @Override
    public void act(float deltaTime){
        super.act(deltaTime);
        setCurrentCooldown(getCurrentCooldown()+deltaTime);


        float speed = getSpeed();
        float angle = getAngle();

        if(isAccelerating) {
            if(speed > maxSpeed) {
                speed = maxSpeed;
            } else {
                speed += 80f * deltaTime;
            }
        } else {
            if(speed > 0) {
                speed -= 20f * deltaTime;
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
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        //TODO: Render trail/bow wave?
    }

    private void switchWeapon(ProjectileType projectileType) {
        this.setSelectedProjectileType(projectileType);
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
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.UP) {
            isAccelerating = true;
            return true;
        }

        if(keycode == Input.Keys.LEFT) {
            angularSpeed = 1;
            return true;
        }

        if(keycode == Input.Keys.RIGHT) {
            angularSpeed = -1;
            return true;
        }

        for(ProjectileType projectileTypes : getProjectileTypes()) {
            if(keycode == projectileTypes.getKeyCode()) {
                Gdx.app.log("Test Log","switching weapon to "+projectileTypes.getName());

                switchWeapon(projectileTypes);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.UP) {
            isAccelerating = false;
            return true;
        }

        if(keycode == Input.Keys.LEFT) {
            angularSpeed = 0;
            return true;
        }

        if(keycode == Input.Keys.RIGHT) {
            angularSpeed = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
