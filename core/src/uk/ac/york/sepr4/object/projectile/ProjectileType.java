package uk.ac.york.sepr4.object.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import lombok.Data;

@Data
public class ProjectileType {

    private float cooldown;
    private String triggerKey;
    private String name;
    private Double damage;
    private String textureStr;
    private float baseSpeed;

    public ProjectileType() {}

    public ProjectileType(String name, Double damage, String triggerKey, String textureStr, float cooldown, float baseSpeed) {
        this.name = name;
        this.damage = damage;
        this.triggerKey = triggerKey;
        this.cooldown = cooldown;
        this.textureStr = textureStr;
        this.baseSpeed = baseSpeed;
    }

    public Integer getKeyCode() {
        return Input.Keys.valueOf(triggerKey);
    }

    public Texture getTexture() {
        return new Texture(Gdx.files.internal("textures/"+textureStr));
    }


}
