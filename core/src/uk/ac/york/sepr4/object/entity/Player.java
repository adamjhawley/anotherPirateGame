package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.entity.item.Item;

import java.util.ArrayList;
import java.util.List;

@Data
public class Player extends LivingEntity {

    private Integer balance;
    private Integer xp;
    private Double health;
    private Double maxHealth;
    private float maxSpeed;

    private List<Item> inventory;
    private float angularSpeed;
    private boolean isAccelerating;

    public Player(){
        this(0, 0, 0, 0, 0, 20.0, 20.0, 120f, new ArrayList<Item>(), 0f, false);
    }

    public Player(Integer id, float angle, float speed, Integer balance, Integer xp, Double health, Double maxHealth, float maxSpeed, List<Item> inventory, float angularSpeed, boolean isAccelerating) {
        super(id, angle, speed, TextureManager.PLAYER);
        this.balance = balance;
        this.xp = xp;

        this.health = health;
        this.maxHealth = maxHealth;
        this.maxSpeed = maxSpeed;
        this.inventory = inventory;

        this.angularSpeed = angularSpeed;
        this.isAccelerating = isAccelerating;

        setPosition(50, 50);

        float shipSize = Gdx.graphics.getWidth() / 25;
        setSize(getTexture().getWidth(), getTexture().getHeight());
        addListener(inputListener);

    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        stage.setKeyboardFocus(this);
    }

    @Override
    public void act(float deltaTime){
        super.act(deltaTime);

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

        float alpha = 1;

        batch.setColor(getColor().r, getColor().g, getColor().b,
                alpha * getColor().a * parentAlpha);

        Texture playerTexture = getTexture();

        float angleDegrees = getAngle() * 360 / 2 / 3.14f;
        batch.draw(playerTexture, getX(), getY(), getWidth()/2, getHeight()/2,
                getWidth(), getHeight(), 1, 1, angleDegrees, 0, 0,
                playerTexture.getWidth(), playerTexture.getHeight(), false, false);


        //TODO: Render trail/bow wave?
    }

    private final InputListener inputListener = new InputListener() {

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if(event.getKeyCode() == Input.Keys.UP) {
                isAccelerating = true;
                return true;
            }

            if(event.getKeyCode() == Input.Keys.LEFT) {
                angularSpeed = 1;
                return true;
            }

            if(event.getKeyCode() == Input.Keys.RIGHT) {
                angularSpeed = -1;
                return true;
            }


            return false;
        }

        @Override
        public boolean keyUp(InputEvent event, int keycode) {
            if(event.getKeyCode() == Input.Keys.UP) {
                isAccelerating = false;
                return true;
            }

            if(event.getKeyCode() == Input.Keys.LEFT) {
                angularSpeed = 0;
                return true;
            }

            if(event.getKeyCode() == Input.Keys.RIGHT) {
                angularSpeed = 0;
                return true;
            }


            return false;
        }
    };

}
