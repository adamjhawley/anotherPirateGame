package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.item.Item;
import uk.ac.york.sepr4.object.projectile.ProjectileType;
import uk.ac.york.sepr4.screen.GameScreen;
import java.util.ArrayList;
import java.util.List;

@Data
public class Player extends LivingEntity implements InputProcessor {

    private Integer balance;
    private Integer xp;
    private List<Item> inventory;
    private float angularSpeed;

    public Player(Vector2 pos) {
        super(0, TextureManager.PLAYER);
        this.balance = 0;
        this.xp = 0;
        this.inventory = new ArrayList<Item>();

        setPosition(pos.x, pos.y);

        //Setup default weapon
        setSelectedProjectileType(GameScreen.getInstance().getProjectileManager().getDefaultWeaponType());
        addProjectileType(GameScreen.getInstance().getProjectileManager().getDefaultWeaponType());
    }

    @Override
    public void act(float deltaTime) {
        float angle = getAngle();
        angle += ((angularSpeed * deltaTime) * (getSpeed() / getMaxSpeed())) % (float)(2*Math.PI);;

        setAngle(angle);
        super.act(deltaTime);
    }

    private void switchWeapon(ProjectileType projectileType) {
        this.setSelectedProjectileType(projectileType);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.UP) {
            setAccelerating(true);
            return true;
        }

        if(keycode == Input.Keys.DOWN) {
            setBraking(true);
            return true;
        }

        if(keycode == Input.Keys.LEFT) {
            setAngularSpeed(getTurningSpeed());
            return true;
        }

        if(keycode == Input.Keys.RIGHT) {
            setAngularSpeed(-getTurningSpeed());
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
            setAccelerating(false);
            return true;
        }

        if(keycode == Input.Keys.DOWN) {
            setBraking(false);
            return true;
        }

        if(keycode == Input.Keys.LEFT) {
            setAngularSpeed(0);
            return true;
        }

        if(keycode == Input.Keys.RIGHT) {
            setAngularSpeed(0);
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
