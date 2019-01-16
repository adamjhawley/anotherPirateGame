package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.projectile.ProjectileType;

import java.util.List;
@Data
public class NPCBoss extends NPCBoat {

    private String name;

    public NPCBoss(Integer id, Texture texture, String name, float angle, float speed, float maxSpeed, Double health, Double maxHealth, Integer turningSpeed, boolean onFire, List<ProjectileType> projectileTypes, float range, float accuracy, College allied, float idealDistFromTarget, float gradientForNormalDist) {
        super(id, texture, angle, speed, maxSpeed, health, maxHealth, turningSpeed, onFire, projectileTypes, range, accuracy, allied, idealDistFromTarget, gradientForNormalDist);
        this.name = name;
    }
}
