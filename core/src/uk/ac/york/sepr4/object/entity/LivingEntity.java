package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import lombok.Data;
import uk.ac.york.sepr4.object.entity.projectile.Projectile;
import uk.ac.york.sepr4.object.entity.projectile.ProjectileType;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class LivingEntity extends Entity {

    private boolean onFire;
    private List<ProjectileType> projectileTypes;
    private Double health;
    private Double maxHealth;
    private boolean isDead;

    private ProjectileType selectedProjectileType;
    private float currentCooldown;

    public LivingEntity(Integer id, float angle, float speed, Double health, Double maxHealth, Texture texture){
        this(id, angle, speed, health, maxHealth, texture, false, new ArrayList<ProjectileType>());
    }

    public LivingEntity(Integer id, float angle, float speed, Double health, Double maxHealth, Texture texture, boolean onFire, List<ProjectileType> projectileTypes) {
        super(id, angle, speed, texture);

        this.onFire = onFire;
        this.projectileTypes = projectileTypes;
        this.health = health;
        this.maxHealth = maxHealth;
        this.isDead = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float alpha = 1;

        batch.setColor(getColor().r, getColor().g, getColor().b,
                alpha * getColor().a * parentAlpha);

        float angleDegrees = getAngle() * 360 / 2 / 3.14f;
        batch.draw(getTexture(), getX(), getY(), getWidth()/2, getHeight()/2,
                getWidth(), getHeight(), 1, 1, angleDegrees, 0, 0,
                getTexture().getWidth(), getTexture().getHeight(), false, false);
    }

    public void addProjectileType(ProjectileType projectileType) {
        this.projectileTypes.add(projectileType);
    }

    public abstract void fire(float angle);
}
