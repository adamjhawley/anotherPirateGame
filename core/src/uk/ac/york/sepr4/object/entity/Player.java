package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.math.Vector2;

public class Player extends LivingEntity {

    private Integer balance;
    private Integer xp;
    //private List<Item> inventory;
    private float angularSpeed;
    private boolean isAccelerating;

    public Player(Integer id, Vector2 pos, float angle, float speed){
        this(id, pos, angle, speed, 0, 0, 0f, false);
    }

    public Player(Integer id, Vector2 pos, float angle, float speed, Integer balance, Integer xp, float angularSpeed, boolean isAccelerating) {
        super(id, pos, angle, speed);
        this.balance = balance;
        this.xp = xp;
        this.angularSpeed = angularSpeed;
        this.isAccelerating = isAccelerating;
    }

    @Override
    public void act(float deltaTime){
        //do movement - change this
        super.act(deltaTime);
    }
}
