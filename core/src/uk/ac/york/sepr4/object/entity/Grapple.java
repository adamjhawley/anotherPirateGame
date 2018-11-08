package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import uk.ac.york.sepr4.object.entity.attribute.Ability;

public class Grapple extends Ability {
    public Grapple(Double cooldown, Integer keyCode, String name, Texture texture) {
        super(cooldown, keyCode, name, texture);
    }

    @Override
    public void action() {

    }
}
