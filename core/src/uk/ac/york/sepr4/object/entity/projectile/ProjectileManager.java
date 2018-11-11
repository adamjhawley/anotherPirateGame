package uk.ac.york.sepr4.object.entity.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Getter;
import uk.ac.york.sepr4.object.entity.EntityManager;
import uk.ac.york.sepr4.object.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class ProjectileManager {

    @Getter
    private Array<ProjectileType> projectileTypeList;

    @Getter
    private List<Projectile> projectileList;

    private EntityManager entityManager;

    public ProjectileManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.projectileList = new ArrayList<Projectile>();

        Json json = new Json();
        projectileTypeList = json.fromJson(Array.class, ProjectileType.class, Gdx.files.internal("projectiles.json"));
    }

    public ProjectileType getDefaultWeaponType() {
        return projectileTypeList.peek();
    }

    private Integer getNextProjectileID(){
        return projectileList.size();
    }

    public void spawnProjectile(ProjectileType projectileType, LivingEntity livingEntity, float speed, float angle) {
        Projectile projectile = new Projectile(getNextProjectileID(), projectileType, livingEntity, speed, angle);
        projectileList.add(projectile);
    }

}
