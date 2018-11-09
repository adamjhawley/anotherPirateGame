package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import lombok.Data;

@Data
public abstract class Ability {

    private Double cooldown;
    private Integer keyCode;
    private String name;
    private Texture texture;

    public Ability(Double cooldown, Integer keyCode, String name, Texture texture) {
        this.cooldown = cooldown;
        this.keyCode = keyCode;
        this.name = name;
        this.texture = texture;
    }

    public abstract void action();
}
