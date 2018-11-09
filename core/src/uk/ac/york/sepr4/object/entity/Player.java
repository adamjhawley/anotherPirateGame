package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;

@Data
public class Player extends LivingEntity {

    private Integer balance;
    private Integer xp;
    //private List<Item> inventory;
    private float angularSpeed;
    private boolean isAccelerating;

    public Player(){
        this(0, 0, 0, 0, 0, 0f, false);
    }

    public Player(Integer id, float angle, float speed, Integer balance, Integer xp, float angularSpeed, boolean isAccelerating) {
        super(id, angle, speed, TextureManager.PLAYER);
        this.balance = balance;
        this.xp = xp;
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
        float force = 0;
        float angle = getAngle();

        if(speed > 0.2) {
            force = getWidth() * speed * 0.05f;
            force = Math.max(force, getWidth() * 4);
            force = -force;
        }

        speed = Math.max(getSpeed(), 0);

        if(isAccelerating) {
            force += getWidth() * 100;
        }

        speed = force * deltaTime;


        float y = getY();
        float x = getX();
        y -= speed * deltaTime * Math.cos(angle);
        x += speed * deltaTime * Math.sin(angle);

        setPosition(x, y);

        angle += angularSpeed * deltaTime;

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


//        if(mIsAccelerating) {
//            Texture playerShipJetTexture = ResourceManager.getInstance().playerShipJetTexture;
//            float jetAlpha = (float) (Math.cos((mElapsedTime )* 40f));
//            batch.setColor(getColor().r, getColor().g, getColor().b,
//                    alpha * jetAlpha * getColor().a * parentAlpha);
//
//            batch.draw(playerShipJetTexture, getX(), getY(), getWidth()/2, getHeight()/2,
//                    getWidth(), getHeight(), 1, 1, angleDegrees, 0, 0,
//                    playerShipJetTexture.getWidth(), playerShipJetTexture.getHeight(), false, false);
//        }
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

//            if(event.getKeyCode() == Input.Keys.SPACE) {
//                //fire cannon?
//                return true;
//            }

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
