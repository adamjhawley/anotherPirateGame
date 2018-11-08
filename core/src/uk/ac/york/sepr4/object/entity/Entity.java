package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Data;

@Data
public abstract class Entity extends Actor {

    private Integer id;
    private Vector2 pos;
    private float angle;
    private float speed;

    public Entity(Integer id, Vector2 pos, float angle, float speed) {
        this.id = id;
        this.pos = pos;
        this.angle = angle;
        this.speed = speed;
    }

}
