package uk.ac.york.sepr4.object.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Data;

@Data
public abstract class Entity extends Actor {

    private Integer id;
    private float angle;
    private float speed;
    private Texture texture;

    public Entity(Integer id, float angle, float speed, Texture texture) {
        this.id = id;
        this.angle = angle;
        this.speed = speed;
        this.texture = texture;

        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    }

    public Vector2 getCentre() {
        return new Vector2(getX()+(getTexture().getWidth()/2f), getY()+(getTexture().getHeight()/2f));
    }

    public double distanceFrom(Entity entity){
        return Math.sqrt(Math.pow((entity.getX() - this.getX()), 2) + Math.pow((entity.getY() - this.getY()), 2));
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

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());

    }

}
