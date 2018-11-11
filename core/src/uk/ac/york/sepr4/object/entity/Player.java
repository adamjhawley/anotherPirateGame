package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.entity.item.Item;
import uk.ac.york.sepr4.object.entity.projectile.ProjectileType;
import uk.ac.york.sepr4.screen.GameScreen;

import java.util.ArrayList;
import java.util.List;

@Data
public class Player extends LivingEntity {

    private Integer balance;
    private Integer xp;

    private float maxSpeed;

    private List<Item> inventory;
    private float angularSpeed;
    private boolean isAccelerating;

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

    private final InputListener inputListener = new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if(event.getKeyCode() == Input.Buttons.LEFT) {
                Gdx.app.log("test","click");
                float fireAngle = 0f;
                //TODO: Calc angle
                fire(fireAngle);
                return true;
            }
            return false;
        }

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

            for(ProjectileType projectileTypes : getProjectileTypes()) {
                Gdx.app.log("test",projectileTypes.getKeyCode()+"");
                if(event.getKeyCode() == projectileTypes.getKeyCode()) {
                    Gdx.app.log("test","switch");

                    switchWeapon(projectileTypes);
                    return true;
                }
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

    @Override
    public void fire(float angle) {
        ProjectileType projectileType = this.getSelectedProjectileType();
        if(projectileType.getCooldown() <= getCurrentCooldown()) {
            setCurrentCooldown(0f);
            GameScreen.getInstance().getProjectileManager().spawnProjectile(projectileType, this, getSpeed(), angle);
        }
    }
}
