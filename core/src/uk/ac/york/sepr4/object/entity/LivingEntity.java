package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.object.entity.attribute.Ability;
import uk.ac.york.sepr4.object.entity.attribute.Attributes;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class LivingEntity extends Entity {

    private boolean onFire;
    private Attributes attributes;
    private List<Ability> abilities;

    public LivingEntity(Integer id, Vector2 pos, float angle, float speed){
        this(id, pos, angle, speed, false, new Attributes(), new ArrayList<Ability>());
    }

    public LivingEntity(Integer id, Vector2 pos, float angle, float speed, boolean onFire, Attributes attributes, List<Ability> abilities) {
        super(id, pos, angle, speed);

        this.onFire = onFire;
        this.attributes = attributes;
        this.abilities = abilities;
    }
}
