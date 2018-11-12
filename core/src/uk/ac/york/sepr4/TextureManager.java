package uk.ac.york.sepr4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureManager {

    private static final String path = "textures/";

    public static Texture ENEMY = new Texture(Gdx.files.internal(path+"enemy.png"));
    public static Texture PLAYER = new Texture(Gdx.files.internal(path+"player.png"));

    public static Texture CANNONBALL = new Texture(Gdx.files.internal(path+"cannonball.png"));


    public static Texture EXPLOSION1 = new Texture(Gdx.files.internal(path+"explosion1.png"));
    public static Texture EXPLOSION2 = new Texture(Gdx.files.internal(path+"explosion2.png"));
    public static Texture EXPLOSION3 = new Texture(Gdx.files.internal(path+"explosion3.png"));

}
