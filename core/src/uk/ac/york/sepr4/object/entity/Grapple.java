package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;

public class Grapple extends Ability {
    public Grapple(Double cooldown, Integer keyCode, String name, Texture texture) {
        super(cooldown, keyCode, name, texture);
    }

    @Override
    public void action() {

    }
}
