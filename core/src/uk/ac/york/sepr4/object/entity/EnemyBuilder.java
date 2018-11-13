package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.TextureManager;
import uk.ac.york.sepr4.object.entity.projectile.ProjectileType;
import uk.ac.york.sepr4.screen.GameScreen;

import java.util.ArrayList;
import java.util.List;

@Data
public class EnemyBuilder {

    private float angle, speed, angularSpeed = 0f;
    private Double health, maxHealth = 20.0;
    private List<ProjectileType> projectileType = new ArrayList<ProjectileType>();
    private boolean isDead, onFire = false;
    private float maxSpeed = 100f;
    private boolean isAccelerating, isBraking = false;
    private Integer turningSpeed = 1;
    private Texture texture = TextureManager.ENEMY;

    public EnemyBuilder(){

    }

    public Enemy buildEnemy(Integer id, Vector2 pos) {
        Enemy enemy = new Enemy(id, texture);
        enemy.setX(pos.x);
        enemy.setY(pos.y);
        projectileType.add(GameScreen.getInstance().getProjectileManager().getDefaultWeaponType());
        enemy.setSelectedProjectileType(GameScreen.getInstance().getProjectileManager().getDefaultWeaponType());
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

    public EnemyBuilder maxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

}
