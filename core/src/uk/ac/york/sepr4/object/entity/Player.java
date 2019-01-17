package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.item.Item;
import uk.ac.york.sepr4.object.item.Reward;
import uk.ac.york.sepr4.screen.GameScreen;
import java.util.ArrayList;
import java.util.List;

@Data
public class Player extends LivingEntity implements InputProcessor {

    private Integer balance = 0, xp = 0;
    private List<Item> inventory = new ArrayList<>();

    private List<College> captured = new ArrayList<>();

    public Player(Vector2 pos) {
        super(TextureManager.PLAYER, pos);
        //face up
        setAngle((float)Math.PI);
    }

    @Override
    public void act(float deltaTime) {
        float angle = getAngle();
        angle += ((getAngularSpeed() * deltaTime) * (getSpeed() / getMaxSpeed())) % (float)(2*Math.PI);;

       // Gdx.app.log("", this.__str__());

        setAngle(angle);
        super.act(deltaTime);
    }

    public void capture(College college) {
        captured.add(college);
        Gdx.app.debug("Player", "Captured "+college.getName());
    }

    public Integer getLevel() {
        int i = 0, level = 0;
        while(i<=xp){
            i+=((level+1)*10);
            level++;
        }
        return level;
    }

    public Double levelProgress() {
        if(xp==0){
            return 0.0;
        }
        Integer level = getLevel();
        return((double)(xp-getXpRequired(level-1))/(getXpRequired(level)-getXpRequired(level-1)));
    }

    public Integer getXpRequired(Integer level) {
        Integer xpReq = 0;
        for(int i=1; i<=level; i++) {
            xpReq+=(i*10);

        }
        return xpReq;
    }

    public void issueReward(Reward reward) {
        addBalance(reward.getGold());
        addXP(reward.getXp());
        addItems(reward.getItems());
    }

    public void addBalance(Integer val) {
        balance+=val;
    }

    public void addXP(Integer val) {
        xp+=val;
    }
    public void addItems(List<Item> items) {
        inventory.addAll(items);
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
            GameScreen.getInstance().getOrthographicCamera().zoom = 3;
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
