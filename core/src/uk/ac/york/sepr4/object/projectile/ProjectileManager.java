package uk.ac.york.sepr4.object.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Getter;
import uk.ac.york.sepr4.object.entity.EntityManager;
import uk.ac.york.sepr4.object.entity.LivingEntity;

public class ProjectileManager {

    //ToDo: Add a function that takes a square or circle and returns them
    @Getter
    private Array<ProjectileType> projectileTypeList;

    @Getter
    private Array<Projectile> projectileList;

    private EntityManager entityManager;

    public ProjectileManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.projectileList = new Array<Projectile>();

        Json json = new Json();
        projectileTypeList = json.fromJson(Array.class, ProjectileType.class, Gdx.files.internal("projectiles.json"));
    }

    public ProjectileType getDefaultWeaponType() {
        return projectileTypeList.peek();
    }

    private Integer getNextProjectileID(){
        return projectileList.size;
    }

    public void spawnProjectile(ProjectileType projectileType, LivingEntity livingEntity, float speed, float angle) {
        Projectile projectile = new Projectile(getNextProjectileID(), projectileType, livingEntity, speed, angle);
        projectileList.add(projectile);
    }

    public Array<Projectile> removeNonActiveProjectiles() {
        Array<Projectile> toRemove = new Array<Projectile>();
        for(Projectile projectile : projectileList) {
            if(!projectile.isActive()){
                toRemove.add(projectile);
            }
        }
        projectileList.removeAll(toRemove, true);
        return toRemove;
    }

}
