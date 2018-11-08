package uk.ac.york.sepr4.object.entity.attribute;

import lombok.Data;

/**
 * This class has a non-conventional name. The name has been used as it is more befitting of its purpose.
 */

@Data
public class Attributes {

    private Double health;
    private Double maxHealth;
    private float maxSpeed;
    private Double damageBoost;

    public Attributes(){
        this(20.0, 20.0, 20f, 1.0);
    }

    public Attributes(Double health, Double maxHealth, float maxSpeed, Double damageBoost) {
        this.health = health;
        this.maxHealth = maxHealth;
        this.maxSpeed = maxSpeed;
        this.damageBoost = damageBoost;
    }
}
