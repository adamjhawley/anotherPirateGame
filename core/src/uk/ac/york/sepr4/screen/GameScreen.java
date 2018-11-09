package uk.ac.york.sepr4.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import uk.ac.york.sepr4.PirateGame;
import uk.ac.york.sepr4.object.entity.EntityManager;
import uk.ac.york.sepr4.object.entity.Player;
import uk.ac.york.sepr4.object.entity.item.ItemManager;

public class GameScreen implements Screen {

    private PirateGame pirateGame;
    private Stage stage;
    private SpriteBatch batch;

    private OrthographicCamera orthographicCamera;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    private ItemManager itemManager;
    private EntityManager entityManager;

    public GameScreen(PirateGame pirateGame) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        this.pirateGame = pirateGame;

        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false, w,h);
        orthographicCamera.update();

        StretchViewport stretchViewport = new StretchViewport(w,h,orthographicCamera);
        batch = new SpriteBatch();
        stage = new Stage(stretchViewport, batch);

        tiledMap = new TmxMapLoader().load("FullSea.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/2f);

        this.itemManager = new ItemManager();
        this.entityManager = new EntityManager();

        stage.addActor(entityManager.getOrCreatePlayer());
        Gdx.input.setInputProcessor(stage);

    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Player player = entityManager.getOrCreatePlayer();

        orthographicCamera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getWidth() / 2,0);
        orthographicCamera.update();
        batch.setProjectionMatrix(orthographicCamera.combined);
        tiledMapRenderer.setView(orthographicCamera);
        tiledMapRenderer.render();

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        orthographicCamera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
