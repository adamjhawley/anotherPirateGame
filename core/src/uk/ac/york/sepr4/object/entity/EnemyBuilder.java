package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.projectile.ProjectileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class EnemyBuilder {

    private float angle = 0f, speed, angularSpeed = 0f, maxSpeed = 100f, range = 500f, accuracy = 0.5f;
    private Double health = 20.0, maxHealth = 20.0;
    private List<ProjectileType> projectileTypes = new ArrayList<ProjectileType>();
    private boolean isDead = false, onFire = false, isAccelerating, isBraking = false;
    private Integer turningSpeed = 10;
    private Texture texture = TextureManager.ENEMY;
    private ProjectileType selectedProjectile;

    public EnemyBuilder(){}

    public Enemy buildEnemy(Integer id, Vector2 pos) {
        Enemy enemy = new Enemy(id, texture, angle, speed, maxSpeed, health, maxHealth, turningSpeed, onFire, projectileTypes, range, accuracy);
        enemy.setX(pos.x);
        enemy.setY(pos.y);

        if(selectedProjectile != null) {
            enemy.setSelectedProjectileType(this.selectedProjectile);
            enemy.addProjectileType(this.selectedProjectile);
        }
        return enemy;
    }

    public EnemyBuilder angle(float angle) {
        this.angle = angle;
        return this;
    }

    public EnemyBuilder texture(Texture texture) {
        this.texture = texture;
        return this;
    }

    public EnemyBuilder turningSpeed(Integer turningSpeed) {
        this.turningSpeed = turningSpeed;
        return this;
    }

    public EnemyBuilder maxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public EnemyBuilder selectedProjectile(ProjectileType projectileType) {
        this.selectedProjectile = projectileType;
        return this;
    }

    public EnemyBuilder projectileTypes(List<ProjectileType> projectileTypes) {
        this.projectileTypes = projectileTypes;
        return this;
    }

    public EnemyBuilder range(float range) {
        this.range =  range;
        return this;
    }

    public EnemyBuilder accuracy(float accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public EnemyBuilder health(Double health) {
        this.health = health;
        this.maxHealth = health;
        return this;
    }

    public Enemy generateRandomEnemy(Integer id, Vector2 pos, Double difficulty, List<ProjectileType> projectileTypes) {
        EnemyBuilder builder = new EnemyBuilder();
        Random random = new Random();
        builder.projectileTypes = projectileTypes;

        builder.maxSpeed((float)(difficulty*random.nextDouble())+maxSpeed);
        builder.turningSpeed((int)Math.round(difficulty*random.nextDouble())+turningSpeed);
        builder.health((float)(difficulty*random.nextDouble())+maxHealth);

        return builder.buildEnemy(id, pos);
    }

}
