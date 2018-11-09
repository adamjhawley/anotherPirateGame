package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Data;

@Data
public abstract class Entity extends Actor {

    private Integer id;
    private float angle;
    private float speed;
    private Texture texture;

    public Entity(Integer id, float angle, float speed, Texture texture) {
        this.id = id;
        this.angle = angle;
        this.speed = speed;
        this.texture = texture;

        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    }

}
