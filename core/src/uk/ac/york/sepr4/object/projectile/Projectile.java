package uk.ac.york.sepr4.object.projectile;

import com.badlogic.gdx.Gdx;
import lombok.Data;
import uk.ac.york.sepr4.object.entity.Entity;
import uk.ac.york.sepr4.object.entity.LivingEntity;

@Data
public class Projectile extends Entity {

    private LivingEntity shooter;
    private ProjectileType projectileType;

    private boolean active;

    public Projectile(Integer id, ProjectileType projectileType, LivingEntity shooter, float speed, float angle) {
        //Take speed from the shooter so bullet has a speed relative to the shooter
        //TODO: Make the speed direction dependant (how much of the force is in the direction of the projectile)
        super(id, angle, speed+projectileType.getBaseSpeed(), projectileType.getTexture());
        this.shooter = shooter;
        this.projectileType = projectileType;

        this.active = true;

        setSize(getTexture().getWidth(), getTexture().getHeight());
        setPosition(shooter.getCentre().x, shooter.getCentre().y);
    }

    @Override
    public void act(float deltaTime) {
        if(this.distanceFrom(shooter) > 1000) {
            Gdx.app.log("Test Log","Clearing up distant projectile!");
            this.active = false;
        } else {
            super.act(deltaTime);
        }
    }

    public void collide(LivingEntity entity){
        //TODO: Explosion image
        if(entity.getId() != shooter.getId()) {
            entity.setHealth(entity.getHealth() - projectileType.getDamage());
            if (entity.getHealth() <= 0) {
                entity.setDead(true);
            }
        }
    }
}
