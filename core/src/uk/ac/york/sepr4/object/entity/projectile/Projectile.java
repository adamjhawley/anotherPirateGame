package uk.ac.york.sepr4.object.entity.projectile;

import lombok.Data;
import uk.ac.york.sepr4.object.entity.Entity;
import uk.ac.york.sepr4.object.entity.LivingEntity;

@Data
public class Projectile extends Entity {

    private LivingEntity shooter;
    private ProjectileType projectileType;

    public Projectile(Integer id, ProjectileType projectileType, LivingEntity shooter, float angle, float speed) {
        super(id, angle, speed+projectileType.getBaseSpeed(), projectileType.getTexture());
        this.shooter = shooter;
        this.projectileType = projectileType;

        setPosition(shooter.getX(), shooter.getY());
    }

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);

        //TODO: Cleanup
        float speed = getSpeed();
        float angle = getAngle();

        float y = getY();
        float x = getX();
        y -= speed * deltaTime * Math.cos(angle);
        x += speed * deltaTime * Math.sin(angle);

        setPosition(x, y);
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
