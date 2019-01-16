package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.item.Item;
import uk.ac.york.sepr4.screen.GameScreen;
import java.util.ArrayList;
import java.util.List;

@Data
public class Player extends LivingEntity implements InputProcessor {

    private Integer balance = 0, xp = 0;
    private List<Item> inventory = new ArrayList<>();
    private float angularSpeed;

    private List<College> captured = new ArrayList<>();

    public Player(Vector2 pos) {
        super(TextureManager.PLAYER, pos);
        //face up
        setAngle((float)Math.PI);
    }

    @Override
    public void act(float deltaTime) {
        float angle = getAngle();
        angle += ((angularSpeed * deltaTime) * (getSpeed() / getMaxSpeed())) % (float)(2*Math.PI);;

       // Gdx.app.log("", this.__str__());

        setAngle(angle);
        super.act(deltaTime);
    }


    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.W) {
            setAccelerating(true);
            return true;
        }

        if(keycode == Input.Keys.S) {
            setBraking(true);
            return true;
        }

        if(keycode == Input.Keys.A) {
            setAngularSpeed(getTurningSpeed());
            return true;
        }

        if(keycode == Input.Keys.D) {
            setAngularSpeed(-getTurningSpeed());
            return true;
        }
        if(keycode == Input.Keys.M) {
            //minimap
            GameScreen.getInstance().getOrthographicCamera().zoom = 5;
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.W) {
            setAccelerating(false);
            return true;
        }

        if(keycode == Input.Keys.S) {
            setBraking(false);
            return true;
        }

        if(keycode == Input.Keys.A) {
            setAngularSpeed(0);
            return true;
        }

        if(keycode == Input.Keys.D) {
            setAngularSpeed(0);
            return true;
        }
        if(keycode == Input.Keys.M) {
            //minimap
            GameScreen.getInstance().getOrthographicCamera().zoom = 1;
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
