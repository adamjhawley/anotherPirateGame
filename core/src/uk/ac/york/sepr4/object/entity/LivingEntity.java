package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class LivingEntity extends Entity {

    private boolean onFire;
    private List<Ability> abilities;

    public LivingEntity(Integer id, float angle, float speed, Texture texture){
        this(id, angle, speed, texture, false, new ArrayList<Ability>());
    }

    public LivingEntity(Integer id, float angle, float speed, Texture texture, boolean onFire, List<Ability> abilities) {
        super(id, angle, speed, texture);

        this.onFire = onFire;
        this.abilities = abilities;
    }
}
